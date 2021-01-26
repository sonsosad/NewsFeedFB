package com.son.newsfeedfb

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import io.reactivex.Observer
//import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import com.son.newsfeedfb.ViewModel.AuthViewModel
import com.son.newsfeedfb.di.ClientComponent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnRegister
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    init {
        val clientComponent: ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }

    private val lenghtGreaterThanSix  = ObservableTransformer<String,String>{ observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() }
                .filter{it.length >= 6}
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException){
                        Single.error(Exception("Length should be greater than 6"))
                    }else{
                        Single.error(it)
                    }
                }
                .toObservable()
        }
    }
    private val verifyEmailPattern = ObservableTransformer<String,String> {
        it.flatMap {
            Observable.just(it).map {
                it.trim()
            }
                .filter{
                    Patterns.EMAIL_ADDRESS.matcher(it).matches()
                }
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException){
                        Single.error(Exception("Email not valid"))
                    }else{
                        Single.error(it)
                    }
                }.toObservable()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailField()
        passwordField()
        val authViewModel: AuthViewModel = AuthViewModel(this)
        if (firebaseAuth.currentUser != null) {
//            authViewModel.getIdChild(firebaseAuth.currentUser!!.email.toString())
            Toast.makeText(this, "user not null", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, TimeLine::class.java))

        } else {
            Toast.makeText(this, "user null", Toast.LENGTH_LONG).show()
        }
        val proBar: ProgressBar = findViewById(R.id.proBar)
        btnLogin.setOnClickListener(View.OnClickListener {
            proBar.visibility = View.VISIBLE
            val email = userName.text.trim().toString()
            val password = edtPassword.text.trim().toString()
//            authViewModel.login(email, password, this)
            authViewModel.login(email, password, this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    if (it == "successful"){
                        proBar.visibility = View.GONE
                        startActivity(Intent(this@MainActivity,TimeLine::class.java))
                        Toast.makeText(this@MainActivity,"Ok",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this@MainActivity,"Try Again",Toast.LENGTH_LONG).show()
                        proBar.visibility = View.GONE

                    }
                }
//                .subscribe(object : Observer<String>{
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//
//                    }
//
//                    override fun onNext(t: String) {
//                        if (t =="successful"){
//                            startActivity(Intent(this@MainActivity,TimeLine::class.java))
//                            Toast.makeText(this@MainActivity,"ok",Toast.LENGTH_LONG).show()
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//
//                    }
//
//                })
//            authViewModel.getResultAuth(email, password, this).observe(this, Observer {
//                if (it == "successful") {
//                    startActivity(Intent(this, TimeLine::class.java))
//                    proBar.visibility = View.GONE
//                }
//            }
//            )
        })

        btnRegister.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegisterUser::class.java))
        })

    }
    private fun emailField(){
        RxTextView.afterTextChangeEvents(userName)
            .skipInitialValue()
            .map {
                emailWrapper.error = null
                it.view().text.toString()
            }
            .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(lenghtGreaterThanSix)
            .compose(verifyEmailPattern)
            .compose(retryWhenError {
                emailWrapper.error = it.message
            })
            .subscribe()
    }
    private fun passwordField(){
        RxTextView.afterTextChangeEvents(edtPassword)
            .skipInitialValue()
            .map {
                passwordWrapper.error = null
                it.view().text.toString()
            }
            .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(lenghtGreaterThanSix)
            .compose(retryWhenError {
                passwordWrapper.error = it.message
            })
            .subscribe()
    }
    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
        observable.retryWhen { errors ->
            errors.flatMap {
                onError(it)
                Observable.just("")
            }
        }
    }
}