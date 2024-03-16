package com.artemla.passwordmanager.ui.passwordpage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.databinding.FragmentPasswordModalBinding
import com.artemla.passwordmanager.domain.entities.Password
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
        viewModel = ViewModelProvider(
            this,
            PasswordModalModelFactory(requireContext())
        )[PasswordModalViewModel::class.java]
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(requireContext())
            if (password != null) {
                populateFields(password)
            }
            setRandomPasswordListener()
            builder.setView(binding.root)
                // Add action buttons.
                .setPositiveButton("Save"
                ) { _, _ ->
                    handleSaveButtonAction(password)
                }
                .setNegativeButton("Cancel"
                ) { _, _ ->
                    dialog!!.cancel()
                }
            setDialogWindow()
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private fun populateFields(password: Password) {
        binding.passwordModalPassword.setText(password.password)
        binding.passwordModalWebsite.setText(password.website)
        binding.passwordModalLogin.setText(password.login)
    }

    private fun setRandomPasswordListener() {
        binding.passwordModalRandom.setOnClickListener {
            binding.passwordModalPassword.setText(viewModel.generatePassword())
        }
    }

    private fun handleSaveButtonAction(password: Password?) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        if (password == null) {
            handleNewPasswordSave(coroutineScope)
        } else {
            handleExistingPasswordUpdate(password, coroutineScope)
        }
    }

    private fun handleNewPasswordSave(coroutineScope: CoroutineScope) {
        if (validateFields()) {
            coroutineScope.launch {
                viewModel.addNewPasswordToDatabase(
                    website = binding.passwordModalWebsite.text.toString(),
                    login = binding.passwordModalLogin.text.toString(),
                    password = binding.passwordModalPassword.text.toString()
                )
            }
        }
    }

    private fun handleExistingPasswordUpdate(
        password: Password,
        coroutineScope: CoroutineScope
    ) {
        if (validateFields()) {
            coroutineScope.launch {
                viewModel.updatePasswordInDatabase(
                    password = password,
                    website = binding.passwordModalWebsite.text.toString(),
                    login = binding.passwordModalLogin.text.toString(),
                    newPassword = binding.passwordModalPassword.text.toString()
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        return binding.passwordModalPassword.toString().trim().isNotEmpty() &&
                binding.passwordModalWebsite.text.toString().trim().isNotEmpty() &&
                binding.passwordModalLogin.text.trim().isNotEmpty()
    }

    private fun setDialogWindow() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCanceledOnTouchOutside(true)
    }
}