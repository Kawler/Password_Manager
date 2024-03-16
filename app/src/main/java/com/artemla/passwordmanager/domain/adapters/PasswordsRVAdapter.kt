package com.artemla.passwordmanager.domain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.data.db.PasswordDB
import com.artemla.passwordmanager.databinding.RvHomePasswordsItemBinding
import com.artemla.passwordmanager.domain.entities.Password
import com.artemla.passwordmanager.ui.passwordpage.PasswordModalFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PasswordsRVAdapter : RecyclerView.Adapter<PasswordsRVAdapter.PasswordsViewHolder>() {

    private val HIDDEN_PASSWORD = "*********"

    class PasswordsViewHolder(val binding: RvHomePasswordsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var data: Array<Password> = emptyArray()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvHomePasswordsItemBinding.inflate(inflater, parent, false)

        return PasswordsViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PasswordsViewHolder, position: Int) {
        val item = data[position]
        val context = holder.itemView.context

        with(holder.binding) {
            loadPicasso(context, item.website, rvHomePasswordsItemImg)
            rvHomePasswordsItemLogin.text = item.login
            hidePassword(rvHomePasswordsItemShow, item.password, rvHomePasswordsItemPassword)
            setPopupMenuClickListener(
                context,
                item,
                rvHomePasswordsItemMore,
                rvHomePasswordsItemPassword
            )
        }
    }

    private fun loadPicasso(context: Context, website: String, targetView: ImageView) {
        val urlString = "https://www.google.com/s2/favicons?domain=$website&sz=128"
        val picasso = Picasso.Builder(context)
            .listener { _, _, exception ->
                exception?.printStackTrace()
                println("Picasso loading failed : ${exception?.message}")
            }
            .build()

        picasso
            .load(urlString)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(targetView, object : Callback {
                override fun onSuccess() {
                }

                override fun onError(e: java.lang.Exception?) {
                    picasso
                        .load(urlString)
                        .into(targetView)
                }

            })
    }

    private fun hidePassword(button: AppCompatImageView, password: String, textField: TextView) {
        button.setOnClickListener {
            if (textField.text != HIDDEN_PASSWORD) {
                textField.text = HIDDEN_PASSWORD
            } else textField.text = password
        }
    }

    private fun setPopupMenuClickListener(
        context: Context,
        item: Password,
        button: AppCompatImageView,
        textField: TextView
    ) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        button.setOnClickListener {
            val popup = PopupMenu(context, button)
            popup.inflate(R.menu.home_popup_options)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popup_edit -> {
                        try {
                            val activity = context as AppCompatActivity
                            PasswordModalFragment.newInstance(item).show(
                                activity.supportFragmentManager,
                                PasswordModalFragment.TAG
                            )
                        } catch (e: Exception) {
                            print(e)
                        }
                        true
                    }

                    R.id.popup_delete -> {
                        val db = PasswordDB.getDatabase(context)
                        textField.text = HIDDEN_PASSWORD
                        coroutineScope.launch {
                            db.passwordsDao().delete(item)
                        }
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }
    }
}