package com.artemla.passwordmanager.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.databinding.FragmentSettingsBinding
import com.artemla.passwordmanager.domain.utils.CryptoManager
import com.artemla.passwordmanager.domain.utils.PreferencesUtils

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
        setupMasterPasswordButton(preferences)
        setupFingerprintCheckBox(preferences)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupMasterPasswordButton(preferences: PreferencesUtils) {
        binding.btnMasterPasswordSave.setOnClickListener {
            if (binding.etMasterPassword.text.toString().trim().length > 5) {
                saveMasterPassword(preferences)
            } else {
                showPasswordWarning()
            }
        }
    }

    private fun saveMasterPassword(preferences: PreferencesUtils) {
        val cryptoManager = CryptoManager()
        preferences.setMasterPassword(cryptoManager.encrypt(binding.etMasterPassword.text.toString()))
        binding.etMasterPassword.setText("")
    }

    private fun showPasswordWarning() {
        binding.settingsMasterPasswordWarning.text =
            getString(R.string.password_must_contain_more_than_5_characters)
        binding.etMasterPassword.setText("")
    }

    private fun setupFingerprintCheckBox(preferences: PreferencesUtils) {
        if (preferences.getFingerprintAvailable()) {
            binding.cbFingerprint.isChecked = preferences.getFingerprintState()
            binding.cbFingerprint.setOnCheckedChangeListener { _, isChecked ->
                preferences.setFingerprintState(isChecked)
            }
        } else {
            hideFingerprintCheckBox()
        }
    }

    private fun hideFingerprintCheckBox() {
        binding.fingerprintCheckBox.visibility = View.GONE
    }
}