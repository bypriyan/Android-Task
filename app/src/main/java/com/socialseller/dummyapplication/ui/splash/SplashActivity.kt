package com.socialseller.dummyapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.socialseller.dummyapplication.ui.auth.AuthActivity
import com.socialseller.dummyapplication.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Use the injected FirebaseAuth instance
        val currentUser = firebaseAuth.currentUser

        val nextActivity = if (currentUser != null) {
            HomeActivity::class.java
        } else {
            AuthActivity::class.java
        }

        startActivity(Intent(this, nextActivity))
        finish()
    }
}
