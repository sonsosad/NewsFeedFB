package com.son.newsfeedfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.son.newsfeedfb.ViewModel.AuthViewModel
import com.son.newsfeedfb.di.ClientComponent
import javax.inject.Inject


class SplashScreen : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
     lateinit var  authViewModel: AuthViewModel
    init {
        val clientComponent : ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = AuthViewModel(this)
        if (firebaseAuth.currentUser != null){
            authViewModel.getIdChild(firebaseAuth.currentUser?.email.toString())
        }else{
            Log.e("Tag","aaa")
        }
        Handler().postDelayed(Runnable {
            kotlin.run {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 2500)

    }
}