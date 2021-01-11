package com.son.newsfeedfb.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.di.DaggerClientComponent
import javax.inject.Inject

class CommentViewModel {
    @Inject
    lateinit var databaseReference: DatabaseReference
    var listPost: ArrayList<Post> = ArrayList()
    var list = MutableLiveData<ArrayList<Post>>()
    var admin =Admin
    var user: MutableLiveData<Post> = MutableLiveData()
    var post = Post()
    var usersId = MutableLiveData<ArrayList<Post>>()
    val id = admin.getId().idChild
    init {
        DaggerClientComponent.builder().build().inject(this)
    }
    fun getUsersList(): LiveData<ArrayList<Post>> {
        getObjectCurrent()
        return usersId
    }

    private fun getObjectCurrent() {
        databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    post = snapshot.getValue(Post::class.java)!!
                    listPost.add(post)
                usersId.postValue(listPost)
            }

        })
    }
    fun setCommnet(refChild : String,list: List<Comment>){
            databaseReference.child(refChild).child("comment").setValue(list)

    }




}