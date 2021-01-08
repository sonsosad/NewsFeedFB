package com.son.newsfeedfb.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.di.DaggerClientComponent
import javax.inject.Inject

class CommentViewModel :AuthViewModel.IdParent{
    @Inject
    lateinit var databaseReference: DatabaseReference
    var listPost: ArrayList<Post> = ArrayList()
    var list = MutableLiveData<ArrayList<Post>>()
    init {
        DaggerClientComponent.builder().build().inject(this)
    }
    fun getUsersList(): LiveData<ArrayList<Post>> {
        getObjectCurrent()
        return list
    }

    private fun getObjectCurrent() {
    }

    override fun idResult(id: String) {
        databaseReference.child(id).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        listPost.add(it.getValue(Post::class.java)!!)
                    }
                list.postValue(listPost)
            }

        })
    }

}