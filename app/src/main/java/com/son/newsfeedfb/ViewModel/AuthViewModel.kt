package com.son.newsfeedfb.ViewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.son.newsfeedfb.MainActivity
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.MyApplication
import com.son.newsfeedfb.RegisterUser
import com.son.newsfeedfb.TimeLine
import com.son.newsfeedfb.di.ClientComponent
import com.son.newsfeedfb.di.DaggerClientComponent
import kotlinx.android.synthetic.main.activity_register_user.*
import javax.inject.Inject

class AuthViewModel() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    var resultAuth = MutableLiveData<String>("error")

    @Inject
    lateinit var databaseReference: DatabaseReference

    @Inject
    lateinit var comment: Comment

    @Inject
    lateinit var post: Post
    lateinit var id: String
    var admin = Admin
    var flag: Boolean = true

    init {
        var clientComponent: ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }

    fun login(email: String, password: String,activity: MainActivity) {
        flag = true
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful&&flag) {
                        flag =false
                        databaseReference.orderByChild("authorID").equalTo(email.replace(".", "-"))
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    resultAuth.value = "successful"
                                    snapshot.children.forEach {
                                        Log.e("Tag", "parent: ${it.key}")
                                        id = it.key.toString()
                                        admin.getId().idChild = it.key.toString()
                                    }
                                }

                            })
                    } else {
                        Log.e("TAg", "Error")
                    }
                })

        } else {
            Log.e("TAg", "Error nhap")

        }
    }

    fun getResultAuth(email: String, password: String,activity: MainActivity): LiveData<String> {
        login(email, password,activity)
        return resultAuth
    }

    fun getResultRegister(email: String, password: String, name: String,activity: RegisterUser): LiveData<String> {
        registerUser(email, password, name,activity)
        return resultAuth
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun registerUser(email: String, password: String, name: String, activity: RegisterUser) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            Log.e("Tag", "tokennn ${it.result.toString()}")
            post.token = it.result.toString()
        })
        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val uid = databaseReference.push().key
                        admin.getId().idChild = uid.toString()
                        if (uid != null) {
                            val comment = ArrayList<Comment>()
                            comment.add(
                                Comment(
                                    "I love U 3000",
                                    "Captain America",
                                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTUk3MyP-RT-r6PoZtKOIK8yBj-4rPEYPbW8g&usqp=CAU"
                                )
                            )
                            comment.add(
                                Comment(
                                    "Happy new year",
                                    "Spider men",
                                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTUk3MyP-RT-r6PoZtKOIK8yBj-4rPEYPbW8g&usqp=CAU"
                                )
                            )
                            comment.add(
                                Comment(
                                    "Cuộc sống sẻ chia, vị bia kết nối",
                                    "Spider men",
                                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTUk3MyP-RT-r6PoZtKOIK8yBj-4rPEYPbW8g&usqp=CAU"
                                )
                            )
                            post.authorID = user?.email?.replace(".", "-").toString()
                            post.name = name
                            post.like = 47
                            post.viewType = 2
                            post.title = "ABCXYZ"
                            post.content = "https://i.postimg.cc/nz3jFn1N/khabanh.jpg"
                            post.id = uid.toString()
                            post.avatar =
                                "https://i.pinimg.com/originals/62/19/87/6219878a5bee02e840796a354beb2fff.png"
                            post.createAt = "18-05-2020"
                            post.comment = comment
                            databaseReference.child(uid).setValue(post)

                        }
                        resultAuth.value = "successful"
                    } else {
                        Log.e("Tag", "Error")
                    }
                })
        } else {
            Log.e("Tag", "Error")
        }
    }

}