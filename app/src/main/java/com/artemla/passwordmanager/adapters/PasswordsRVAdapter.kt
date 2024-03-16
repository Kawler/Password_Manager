package com.artemla.passwordmanager.adapters

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.databinding.RvHomePasswordsItemBinding
import com.artemla.passwordmanager.db.PasswordDB
import com.artemla.passwordmanager.dt.Password
import com.artemla.passwordmanager.ui.passwordpage.PasswordModalFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class PasswordsRVAdapter : RecyclerView.Adapter<PasswordsRVAdapter.PasswordsViewHolder>() {
    class PasswordsViewHolder(val binding: RvHomePasswordsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var data: List<Password> = emptyList()
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
        val urlString = "http://icons.duckduckgo.com/ip2/"+item.website+".ico"
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        with(holder.binding) {

            val picasso = Picasso.Builder(context).listener { picasso, uri, exception ->
                exception?.printStackTrace()
                println("Picasso loading failed : ${exception?.message}")
            }.build()

            picasso
                .load(urlString)
                .resize(40,40)
                .centerCrop()
                .into(rvHomePasswordsItemImg)
            rvHomePasswordsItemSite.text = item.website
            rvHomePasswordsItemShow.setOnClickListener {
                if (rvHomePasswordsItemPassword.text != "*********") {
                    rvHomePasswordsItemPassword.text = "*********"
                } else {
                    rvHomePasswordsItemPassword.text = item.password
                }
            }
            rvHomePasswordsItemMore.setOnClickListener {
                val popup = PopupMenu(context, rvHomePasswordsItemMore)
                popup.inflate(R.menu.home_popup_options)
                popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.popup_edit ->                         //handle menu1 click
                            {
                                try {
                                    val activity = context as AppCompatActivity
                                    PasswordModalFragment.newInstance(item).show(activity.supportFragmentManager, PasswordModalFragment.TAG)
                                } catch (e: Exception) {
                                    print(e)
                                }
                                true
                            }

                            R.id.popup_delete -> {
                                val db = PasswordDB.getDatabase(context)
                                coroutineScope.launch {
                                    db.passwordsDao().delete(item)
                                }
                                true
                            }                        //handle menu2 click
                            else -> false
                        }
                }
                popup.show()
            }
        }
    }
}