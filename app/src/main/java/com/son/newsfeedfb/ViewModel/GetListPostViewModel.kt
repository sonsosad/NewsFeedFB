package com.son.newsfeedfb.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.MyApplication
import com.son.newsfeedfb.di.ClientComponent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GetListPostViewModel() : ViewModel() {
    @Inject
     lateinit var databaseReference: DatabaseReference
    @Inject
    lateinit var comment: Comment
    @Inject
    lateinit var postStt: Post
    @Inject
    lateinit var firebaseAuth : FirebaseAuth
    var admin = Admin
     var list : ArrayList<Post> = ArrayList()
    var post  = Post()
     var usersList = MutableLiveData<ArrayList<Post>>()
    init {
//        DaggerClientComponent.builder().build().inject(this)
        val clientComponent : ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)

    }
    fun getUsersList(): LiveData<ArrayList<Post>> {
        getDataFireBase()
        return usersList
    }

    private fun getDataFireBase() {
                databaseReference.addValueEventListener(object :ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
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
    fun getPost(title : String,viewType: Int,content : String,avatar : String,token : String){
        val uid = databaseReference.push().key
        val user = firebaseAuth.currentUser
        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())
        if (uid!=null){
            val comment = ArrayList<Comment>()
            postStt.authorID = user?.email?.replace(".", "-").toString()
            postStt.name = admin.getId().nameAmdin
            postStt.title = title
            postStt.id = uid.toString()
            postStt.avatar  = admin.getId().avatarAdmin
            postStt.token = token
            postStt.comment = comment
            postStt.content = content
            postStt.viewType = viewType
            postStt.createAt = currentDate
            postStt.like = 0
            list.add(postStt)
            databaseReference.child(uid).setValue(postStt).addOnCompleteListener{
            }.addOnCompleteListener {
//                usersList.postValue(list)
            }
        }
    }
}