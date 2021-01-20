package com.son.newsfeedfb
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.son.newsfeedfb.ViewModel.AuthViewModel
import com.son.newsfeedfb.di.ClientComponent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnRegister
import kotlinx.android.synthetic.main.activity_register_user.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth : FirebaseAuth
    init {
        var clientComponent : ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val authViewModel: AuthViewModel = AuthViewModel(this)
        if (firebaseAuth.currentUser!=null){
            Toast.makeText(this,"user not null", Toast.LENGTH_LONG).show()
            authViewModel.getIdChild(firebaseAuth.currentUser!!.email.toString())
            startActivity(Intent(this,TimeLine::class.java))
        }else{
            Toast.makeText(this,"user null", Toast.LENGTH_LONG).show()
        }

        var proBar : ProgressBar = findViewById(R.id.proBar)
        btnLogin.setOnClickListener(View.OnClickListener {
            proBar.visibility = View.VISIBLE
            val email = userName.text.trim().toString()
            val password = edtPassword.text.trim().toString()
            authViewModel.login(email, password,this )
            authViewModel.getResultAuth(email, password,this).observe(this, Observer {
                if (it == "successful") {
                    startActivity(Intent(this, TimeLine::class.java))
                    proBar.visibility = View.GONE
                }
            }
            )
        })
        btnRegister.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegisterUser::class.java))
        })
    }
}