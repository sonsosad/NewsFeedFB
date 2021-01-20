package com.son.newsfeedfb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.ViewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.activity_register_user.btnRegister

class RegisterUser : AppCompatActivity() {
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        btnRegister.setOnClickListener(View.OnClickListener {
            val email = edtEmailRegister.text.trim().toString()
            val password = edtPasswordRegister.text.trim().toString()
            val name = edtName.text.trim().toString()
            authViewModel = AuthViewModel(this)
            authViewModel.registerUser(email, password, name,this)
            authViewModel.getResultRegister(email, password, name,this).observe(this, Observer {
                if (it == "successful") {
                    startActivity(Intent(this, TimeLine::class.java))
                }
            })
        })
    }

}