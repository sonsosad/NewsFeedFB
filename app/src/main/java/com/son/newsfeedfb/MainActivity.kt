package com.son.newsfeedfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.son.newsfeedfb.ViewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),AuthViewModel.IdParent {
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener(View.OnClickListener {
            var email = userName.text.toString()
            var password = edtPassword.text.toString()
            authViewModel = AuthViewModel(this)
            authViewModel.login(email, password)
            authViewModel.getResultAuth(email, password).observe(this, Observer {
                Log.e("Tag","result"+it.toString())

                if (it == "successful") {
                    startActivity(Intent(this, TimeLine::class.java))

                }
            }

            )

        })
        btnRegister.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegisterUser::class.java))
        })

    }

    override fun idResult(id: String) {
    }

}