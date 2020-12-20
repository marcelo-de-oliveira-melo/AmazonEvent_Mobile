package com.AmazonEvent

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar!!.hide()
        val handle = Handler()
        handle.postDelayed({ verficaLogin() }, 2000)
    }

    protected fun verficaLogin() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this@SplashScreen, Principal::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashScreen, Login::class.java))
            finish()
        }
    }
}