package com.artemla.passwordmanager.ui.passwordpage

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.artemla.passwordmanager.CryptoManager
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.databinding.FragmentPasswordModalBinding
import com.artemla.passwordmanager.db.PasswordDB
import com.artemla.passwordmanager.dt.Password
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordModalFragment(private val password: Password?) : DialogFragment() {

    companion object {
        fun newInstance(password: Password?) = PasswordModalFragment(password)
        const val TAG = "PasswordModalFragment"
    }

    private lateinit var viewModel: PasswordModalViewModel
    private var _binding: FragmentPasswordModalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPasswordModalBinding.inflate(layoutInflater)
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(requireContext())
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            val cryptoManager = CryptoManager()
            if (password != null) {
                binding.passwordModalPassword.setText(password.password)
                binding.passwordModalWebsite.setText(password.website)
            }
            binding.passwordModalRandom.setOnClickListener {
                binding.passwordModalPassword.setText(generatePassword())
            }
            builder.setView(binding.root)
                // Add action buttons.
                .setPositiveButton("Save",
                    DialogInterface.OnClickListener { _, _ ->
                        val db = PasswordDB.getDatabase(requireContext())
                        if (password == null){
                            if (binding.passwordModalPassword.toString().trim().isNotEmpty() && binding.passwordModalWebsite.text.toString().trim().isNotEmpty()) {
                                coroutineScope.launch {
                                    db.passwordsDao().addPassword(Password(id = null,website = binding.passwordModalWebsite.text.toString().trim(),
                                        password = cryptoManager.encrypt(binding.passwordModalPassword.text.toString().trim())))
                                }
                            }
                        } else {
                            if (binding.passwordModalPassword.toString().trim().isNotEmpty() && binding.passwordModalWebsite.text.toString().trim().isNotEmpty()) {
                                coroutineScope.launch {
                                    db.passwordsDao().updatePassword(Password(id = password.id,website = binding.passwordModalWebsite.text.toString().trim(),
                                        password = cryptoManager.encrypt(binding.passwordModalPassword.text.toString().trim())))
                                }
                            }
                        }
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { _, _ ->
                        dialog!!.cancel()
                    })
            setDialogWindow()
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun generatePassword(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_')
        return (1..15)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun setDialogWindow() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}