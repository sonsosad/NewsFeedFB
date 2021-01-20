package com.son.newsfeedfb

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.firebase.auth.FirebaseAuth
import com.son.newsfeedfb.Adapter.ListPostAdapter
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.ViewModel.AuthViewModel
import com.son.newsfeedfb.ViewModel.GetListPostViewModel
import com.son.newsfeedfb.di.ClientComponent
import kotlinx.android.synthetic.main.post_fragment.view.*
import javax.inject.Inject

class PostFragment : Fragment(), ListPostAdapter.Callback, CommentDialog.PutData {
    lateinit var getListPostViewModel: GetListPostViewModel
    lateinit var listPostAdapter: ListPostAdapter
    lateinit var player: SimpleExoPlayer

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    var userList: ArrayList<Post> = ArrayList()

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
        listPostAdapter = ListPostAdapter(this.context, userList, this, player)
        rvPost.adapter = listPostAdapter
        rvPost.setHasFixedSize(true)
        getListPostViewModel = GetListPostViewModel()
        getListPostViewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            listPostAdapter.updateData(it)
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
        list: ArrayList<Comment>,
        refChild: String,
        name: String,
        token: String, position: Int
    ) {
        val commentDialog = CommentDialog(list, refChild, name, token, this, position)
        val fm: FragmentManager = this.childFragmentManager
        commentDialog.show(fm, null)
    }

    override fun sendListComment(list: ArrayList<Comment>, position: Int) {
        Log.e("Tag", "commentTest + ${list.size}")
        listPostAdapter.updateComment(position, list)
    }

}