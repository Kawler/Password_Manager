package com.artemla.passwordmanager.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.artemla.passwordmanager.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment


class PasswordAppIntro : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(
            AppIntroFragment.newInstance(
                "Convenient and secure way to store your passwords",
                "Discover a seamless and fortified solution for safeguarding your passwords with ease." +
                        " Effortlessly store and manage passwords for all your accounts " +
                        "with enhanced security features at your fingertips.",
                R.drawable.intro_bg_lock,
                Color.BLACK
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                "All data is encrypted",
                "Experience top-tier security with end-to-end encryption ensuring all your sensitive data," +
                        " including passwords, remains shielded and confidential." +
                        " Rest assured knowing that each password is encrypted before being securely " +
                        "stored in your local database for maximum protection.\"",
                R.drawable.intro_bg_encryption,
                Color.BLACK
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                "Effortless Password Access with Fingerprint Security",
                "Enhance your password management experience by effortlessly accessing your passwords " +
                        "with the simple touch of your fingerprint." +
                        " Enjoy seamless and secure authentication for convenient and reliable password protection.",
                R.drawable.intro_bg_fingerprint,
                Color.BLACK
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}