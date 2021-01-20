package com.son.newsfeedfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed( Runnable {
            kotlin.run {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        },1000)
    }
}