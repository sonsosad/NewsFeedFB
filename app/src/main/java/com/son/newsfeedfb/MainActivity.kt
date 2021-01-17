package com.son.newsfeedfb
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.son.newsfeedfb.ViewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener(View.OnClickListener {
            val email = userName.text.toString()
            val password = edtPassword.text.toString()
            authViewModel = AuthViewModel()
            authViewModel.login(email, password,this )
            authViewModel.getResultAuth(email, password,this).observe(this, Observer {
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

}