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
import com.son.newsfeedfb.di.DaggerClientComponent
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
    var usersId = MutableLiveData<ArrayList<Post>>()
    val id = admin.getId().idChild
    init {
//        DaggerClientComponent.builder().build().inject(this)
        var clientComponent : ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }
    fun getUsersList(): LiveData<ArrayList<Post>> {
        getObjectCurrent()
        return usersId
    }

     fun getObjectCurrent() {
        databaseReference.child(id).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    post = snapshot.getValue(Post::class.java)!!
                    listPost.add(post)
                usersId.postValue(listPost)
                admin.getId().nameAmdin = post.name
            }

        })
    }

    fun setCommnet(refChild : String,list: List<Comment>){
            databaseReference.child(refChild).child("comment").setValue(list)

    }
    fun sendNotification(token : String,title : String, body: String){
        getObjectCurrent()
        var notification =  Notification(title,body)
        var message = Message(token,notification)
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