package com.son.newsfeedfb.ViewModel
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.MyApplication
import com.son.newsfeedfb.di.ClientComponent
import com.son.newsfeedfb.di.DaggerClientComponent
import java.text.ParsePosition
import javax.inject.Inject

class GetListPostViewModel() : ViewModel() {
    @Inject
     lateinit var databaseReference: DatabaseReference
     var list : ArrayList<Post> = ArrayList()
    var post  = Post()
     var usersList = MutableLiveData<ArrayList<Post>>()
    init {
//        DaggerClientComponent.builder().build().inject(this)
        var clientComponent : ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)

    }
    fun getUsersList(): LiveData<ArrayList<Post>> {
        getDataFireBase()
        return usersList
    }

    private fun getDataFireBase() {
                databaseReference.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    post = it.getValue(Post::class.java)!!
                    list.add(post)
                }
                usersList.postValue(list)
            }
        })
    }
    fun getLike(refChild : String, countLike: Int){
        databaseReference = FirebaseDatabase.getInstance().getReference("Post")
        databaseReference.child(refChild).child("like").setValue(countLike)

    }
}