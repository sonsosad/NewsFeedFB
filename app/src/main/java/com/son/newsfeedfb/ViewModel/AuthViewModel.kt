package com.son.newsfeedfb.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.son.newsfeedfb.MainActivity
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.Model.User
import com.son.newsfeedfb.MyApplication
import com.son.newsfeedfb.RegisterUser
import com.son.newsfeedfb.di.ClientComponent
import io.reactivex.Observable
import io.reactivex.Observable.create
import java.lang.StringBuilder
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

class AuthViewModel(context: Context) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    var resultAuth = MutableLiveData<String>("error")
    var rs: String = ""

//        var observable: Observable<String?>? = Observable.just(rs)

    @Inject
    lateinit var databaseReference: DatabaseReference

    @Inject
    lateinit var comment: Comment
    var user = User("error")

    @Inject
    lateinit var post: Post
    lateinit var id: String
    var admin = Admin
    var flag: Boolean = true
    lateinit var tokenId: String
    var a: Int = 0

    init {
        val clientComponent: ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            Log.e("Tag", "tokennn ${it.result.toString()}")
            tokenId = it.result.toString()
        })
    }

    fun getIdChild(email: String) {
        databaseReference.orderByChild("authorID").equalTo(email.replace(".", "-"))
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        admin.getId().idChild = it.key.toString()
                        Log.e("Tag", "parent: ${it.key}")
                        id = it.key.toString()
                    }
                }
            })
    }

    fun login(email: String, password: String, activity: MainActivity): Observable<String> {
        val obser = create<String> {
            flag = true
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful && flag) {
                            flag = false
                            databaseReference.orderByChild("authorID")
                                .equalTo(email.replace(".", "-"))
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
//                                    observable = Observable.just("successful")
//                                        user.result = "successful"
                                        it.onNext("successful")
                                        resultAuth.value = "successful"
                                        snapshot.children.forEach {
                                            id = it.key.toString()
                                            admin.getId().idChild = it.key.toString()
                                            Log.e("Tag", "parentzzz: ${admin.getId().idChild}")
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
        return obser
//        return Observable.defer {
//            return@defer Observable.just(user.result)
//        }
    }


    //    fun getObservable(email: String, password: String, activity: MainActivity): Observable<String?>? {
//        return Observable.defer{
//            login(email, password, activity)
//            return@defer Observable.just(user.result)
//        }
//    }
    fun getObs(): Observable<LiveData<String>> {
        return Observable.just(resultAuth)
    }

    fun getResultRegister(
        email: String,
        password: String,
        name: String,
        activity: RegisterUser
    ): LiveData<String> {
        registerUser(email, password, name, activity)
        return resultAuth
    }

    fun logOut() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
        }
    }

    fun registerUser(email: String, password: String, name: String, activity: RegisterUser) {
        flag = true
        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, OnCompleteListener { task ->
                    if (task.isSuccessful && flag) {
                        flag = false
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
                            post.viewType = 1
                            post.title = "Hiii "
                            post.content = "Chao mn"
                            post.id = uid.toString()
                            post.avatar =
                                "https://wallpapercave.com/wp/wp5184789.jpg"
                            post.createAt = "18-05-2020"
                            post.token = tokenId
                            post.comment = comment
                            databaseReference.child(uid).setValue(post)

                        }
                        resultAuth.value = "successful"
                    } else {
                        Log.e("Tag", "Error register")
                    }
                })
        } else {
            Log.e("Tag", "Error")
        }
    }

}