package com.son.newsfeedfb

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.son.newsfeedfb.Adapter.ListPostAdapter
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.ViewModel.CommentViewModel
import com.son.newsfeedfb.ViewModel.GetListPostViewModel
import com.son.newsfeedfb.di.ClientComponent
import kotlinx.android.synthetic.main.post_fragment.*
import kotlinx.android.synthetic.main.post_fragment.view.*
import kotlinx.android.synthetic.main.post_header.*
import kotlinx.android.synthetic.main.post_header.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PostFragment : Fragment(), ListPostAdapter.Callback, CommentDialog.PutData {
    lateinit var getListPostViewModel: GetListPostViewModel
    lateinit var listPostAdapter: ListPostAdapter
    lateinit var player: SimpleExoPlayer
    lateinit var commentViewModel: CommentViewModel
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    var VIEW_TYPE : Int = 1
    private  lateinit var filePath : Uri
    private var nameOfFile = ""
    private var directUrl = ""
    var admin = Admin
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var storageRef : StorageReference
    var userList: ArrayList<Post> = ArrayList()
    var post = Post()

    init {
        var clientComponent: ClientComponent = MyApplication.clientComponent
        clientComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                firebaseAuth.signOut()
//                authViewModel?.logOut()
                Toast.makeText(context, "Logout is success", Toast.LENGTH_LONG).show()
                (activity as TimeLine).onBackPressed()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvPost = view.rvPost
        rvPost.layoutManager = LinearLayoutManager(this.context)
        rvPost.addItemDecoration(DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL))
        player =
            this.context?.let { ExoPlayerFactory.newSimpleInstance(it, DefaultTrackSelector()) }!!
        listPostAdapter = ListPostAdapter(this.context, userList, this, player,post)
        rvPost.adapter = listPostAdapter
        rvPost.setHasFixedSize(true)
        getListPostViewModel = GetListPostViewModel()
        getListPostViewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            shimerPost.stopShimmer()
            shimerPost.visibility = View.GONE
            rvPost.visibility = View.VISIBLE
            listPostAdapter.updateData(it)
        })
        commentViewModel = CommentViewModel()
        commentViewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            it.forEach {
                listPostAdapter.updatePost(it)
                admin.getId().nameAmdin = it.name
            }
        })
    }
    override fun onStop() {
        super.onStop()
        pausePlayer(player)
        player.release()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer(player)
    }

    override fun onResume() {
        super.onResume()
        startPlayer(player)
    }

    private fun pausePlayer(exoPlayer: SimpleExoPlayer) {
        exoPlayer.playWhenReady = false
        player.playbackState
    }

    private fun startPlayer(exoPlayer: SimpleExoPlayer) {
        exoPlayer.playWhenReady = true
        player.playbackState
    }

    override fun onClickItem(
        refChild: String,
        name: String,
        token: String, position: Int
    ) {
        val commentDialog = CommentDialog(refChild, name, token, this, position)
        val fm: FragmentManager = this.childFragmentManager
        commentDialog.show(fm, null)
    }

    override fun getImage() {
        checkPermission()
    }

    override fun upload(content: String, avatar: String,token: String) {
        upLoad(content, avatar,token)
    }

    override fun getStt(content: String, avatar: String,token: String) {
    }

    override fun sendListComment(list: ArrayList<Comment>, position: Int) {
        Log.e("Tag", "commentTest + ${list.size}")
        listPostAdapter.updateComment(position, list)
    }
    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M){
            if ( super.getContext()?.let { ActivityCompat.checkSelfPermission(it,Manifest.permission.READ_EXTERNAL_STORAGE) } ==
                PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                chooseImage()
            }
        }
        else{
            chooseImage()
        }
    }

    private fun chooseImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent,"Select Picture: "), IMAGE_PICK_CODE)
    }
    private fun upLoad(content: String, avatar: String, token: String){
        val sdf = SimpleDateFormat("dd/M/yyyy-hh:mm:ss")
        val currentDate = sdf.format(Date())
        nameOfFile = currentDate
        Log.e("Tag","nameFile: $nameOfFile")
        if (VIEW_TYPE==1){
            getListPostViewModel.getPost(content,VIEW_TYPE,content,avatar,token)
            edtContent.setText("")
            Toast.makeText(context, "Post is success", Toast.LENGTH_LONG).show()
            rvPost.postDelayed(Runnable {
                kotlin.run {
//                                rvPost.scrollToPosition(rvPost.adapter!!.itemCount-1)
                    listPostAdapter.notifyDataSetChanged()
                }
            },1000)

        }else {
            progressBar.visibility = View.VISIBLE
            val ref: StorageReference =
                storageRef.child(StringBuilder("images/").append(nameOfFile).toString())
            ref.putFile(filePath).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    directUrl = it.toString()
                    Log.e("Tag", "link: ${it}")
                }
                    .addOnFailureListener {
                        Toast.makeText(
                            this.context,
                            "upload Failed: ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnCompleteListener {
                        getListPostViewModel.getPost(content, VIEW_TYPE, directUrl, avatar, token)
                        rvPost.postDelayed(Runnable {
                            kotlin.run {
//                                rvPost.scrollToPosition(rvPost.adapter!!.itemCount-1)
                                listPostAdapter.notifyDataSetChanged()
                            }
                        },1000)
                    }

            }
                .addOnProgressListener {
                    val progress: Double = (100.0 * it.bytesTransferred / it
                        .totalByteCount)
                    progressBar.progress = progress.toInt()
                }
                .addOnCompleteListener {
                    progressBar.visibility = View.GONE
                    imgPreView.visibility = View.GONE
                    llAllMulti.visibility = View.VISIBLE
                    edtContent.setText("")

                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    chooseImage()
                }
                else{
                    Log.e("Tag","Permisson denied")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE ){
            if (data != null) {
                VIEW_TYPE = 2
                filePath = data.data!!
            }else{
                VIEW_TYPE = 1
            }
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
            llAllMulti.visibility = View.GONE
            imgPreView.visibility = View.VISIBLE
            imgPreView.setImageBitmap(bitmap)
            Toast.makeText(this.context,"select success",Toast.LENGTH_LONG).show()

        }
    }
}
