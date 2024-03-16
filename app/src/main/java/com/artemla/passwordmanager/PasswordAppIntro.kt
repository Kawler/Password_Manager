package com.artemla.passwordmanager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment


class PasswordAppIntro : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        addSlide(AppIntroFragment.newInstance("Convenient and secure way to store your passwords", "Allows to store passwords for any website",R.drawable.intro_bg_lock, Color.BLACK));

        // below line is for creating second slide.
        addSlide(AppIntroFragment.newInstance("All passwords are encrypted", "Every password is encrypted before going to a local database",R.drawable.intro_bg_encryption, Color.BLACK));

        // below line is use to create third slide.
        addSlide(AppIntroFragment.newInstance("Fingerprint protection", "You can access you passwords simply using you fingerprint",R.drawable.intro_bg_fingerprint, Color.BLACK));
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}