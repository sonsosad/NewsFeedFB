package com.son.newsfeedfb.ViewModel

import android.app.Activity
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
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.TimeLine
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
    lateinit var id:String
    var admin = Admin

    init {
        DaggerClientComponent.builder().build().inject(this)
    }

    fun login(email: String, password: String) {
        if (!email.isEmpty() && !password.isEmpty()) {
            this.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Activity(), OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
//                        admin.nameAmdin = email
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
//                                            admin.idChild = it.key.toString()
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

    fun getResultAuth(email: String, password: String): LiveData<String> {
        login(email, password)
        return resultAuth
    }

    fun getResultRegister(email: String, password: String, name: String): LiveData<String> {
        registerUser(email, password, name)
        return resultAuth
    }
    fun logOut(){
        firebaseAuth.signOut()
    }

    fun registerUser(email: String, password: String, name: String) {
        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Activity(), OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val uid = databaseReference.push().key
                        if (uid != null) {
                            var comment = ArrayList<Comment>()
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
                            post.viewType = 3
                            post.title = "ABCXYZ"
                            post.content = "https://www.youtube.com/watch?v=OPBjYZPWn_c"
                            post.avatar =
                                "https://i.pinimg.com/originals/62/19/87/6219878a5bee02e840796a354beb2fff.png"
                            post.createAt = "11-05-2017"
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