package com.artemla.passwordmanager.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artemla.passwordmanager.CryptoManager
import com.artemla.passwordmanager.PreferencesUtils
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.databinding.FragmentPasswordModalBinding
import com.artemla.passwordmanager.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val preferences = PreferencesUtils.getInstance(requireContext())
        val cryptoManager = CryptoManager()
        binding.btnMasterPasswordSave.setOnClickListener {
            if (binding.etMasterPassword.text.toString().trim().length > 5) {
                preferences.setMasterPassword(cryptoManager.encrypt(binding.etMasterPassword.text.toString()))
                binding.settingsMasterPasswordWarning.text = ""
            } else {
                binding.settingsMasterPasswordWarning.text =
                    getString(R.string.password_must_contain_more_than_5_characters)
                binding.etMasterPassword.setText("")
            }
        }

        if (preferences.getFingerprintAvailable()) {
            binding.cbFingerprint.isChecked = preferences.getFingerprintState()

            binding.cbFingerprint.setOnCheckedChangeListener { _, isChecked ->
                preferences.setFingerprintState(isChecked)
            }
        } else {
            binding.fingerprintCheckBox.visibility = View.GONE
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}