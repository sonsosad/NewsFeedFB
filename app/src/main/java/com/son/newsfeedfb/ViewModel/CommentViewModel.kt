package com.son.newsfeedfb.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.son.newsfeedfb.Model.*
import com.son.newsfeedfb.MyApplication
import com.son.newsfeedfb.di.ClientComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CommentViewModel {
    @Inject
    lateinit var databaseReference: DatabaseReference
    @Inject
    lateinit var apiService: APIService
    var listPost: ArrayList<Post> = ArrayList()
     var admin =Admin
    var post = Post()
    var comment = Comment()
    var usersId = MutableLiveData<ArrayList<Post>>()
    val id = admin.getId().idChild
    var listCmt = MutableLiveData<ArrayList<Comment>>()
    var listComment: ArrayList<Comment> = ArrayList()
    init {
        var clientComponent : ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }
    fun getDataComment(refChild: String): LiveData<ArrayList<Comment>>{
        dataComment(refChild)
        return listCmt
    }

    fun getUsersList(): LiveData<ArrayList<Post>> {
        getObjectCurrent()
        return usersId
    }
     fun getObjectCurrent() {
        databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    post = snapshot.getValue(Post::class.java)!!
                    listPost.add(post)
                usersId.postValue(listPost)
                admin.getId().nameAmdin = post.name
                admin.getId().avatarAdmin = post.avatar
                Log.e("Tag", post.toString())
            }

        })
    }
    private fun dataComment(refChild: String){
        databaseReference.child(refChild).child("comment").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                        Log.e("Tag","no cmt")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listComment.clear()
                snapshot.children.forEach{
                    comment = it.getValue(Comment::class.java)!!
                    listComment.add(comment)
                    listCmt.postValue(listComment)
                }

            }

        })
    }
    fun setCommnet(refChild : String,list: List<Comment>){
            databaseReference.child(refChild).child("comment").setValue(list)
    }
    fun sendNotification(token : String,title : String, body: String){
        getObjectCurrent()
        val notification =  Notification(title,body)
        val message = Message(token,notification)
        apiService.sendNotifcation(message).enqueue(object : Callback<Message>{
            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.e("Tag","eror send FCM")
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    Log.e("Tag","ok Post")
            }
        })
    }




}