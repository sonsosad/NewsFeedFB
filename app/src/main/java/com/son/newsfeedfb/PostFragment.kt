package com.son.newsfeedfb

import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.son.newsfeedfb.Adapter.ListPostAdapter
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.ViewModel.AuthViewModel
import com.son.newsfeedfb.ViewModel.GetListPostViewModel
import kotlinx.android.synthetic.main.post_fragment.view.*

class PostFragment : Fragment(), ListPostAdapter.Callback {
    lateinit var getListPostViewModel: GetListPostViewModel
    lateinit var listPostAdapter: ListPostAdapter
    private lateinit var popupWindow: PopupWindow
    var authViewModel = AuthViewModel()
    var userList: ArrayList<Post> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.post_fragment, container, false)
        return view
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
                authViewModel.logOut()
                Toast.makeText(context, "Logout is success", Toast.LENGTH_LONG).show()
                activity?.finish()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvPost = view.rvPost
        rvPost.layoutManager = LinearLayoutManager(this.context)
        listPostAdapter = ListPostAdapter(this.context, userList, this)
        rvPost.adapter = listPostAdapter
        getListPostViewModel = GetListPostViewModel()
        getListPostViewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            listPostAdapter.updateData(it)
        })

    }

    override fun onClickItem(list: ArrayList<Comment>, refChild: String) {
        val commentDialog = CommentDialog(list, refChild)
        val fm: FragmentManager = this.childFragmentManager
        commentDialog.show(fm, null)
    }

    fun popUp(list: ArrayList<Comment>) {
        val inflateview: View = layoutInflater.inflate(R.layout.cmt_popup_layout, null, false)
        val display: Display = activity?.windowManager!!.defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        popupWindow = PopupWindow(inflateview, size.x - 50, size.y - 400, true)
        popupWindow.setBackgroundDrawable(context?.let { getDrawable(it, R.drawable.fb_popup_bg) })
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 100)
    }
//    private fun loadCommnent(list: ArrayList<Comment>) {
//        commentApdater = CommentApdater(this.context, list)
//        rvCm.adapter = commentApdater
//    }

}