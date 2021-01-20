package com.son.newsfeedfb

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.son.newsfeedfb.Adapter.CommentApdater
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.ViewModel.CommentViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.cmt_popup_layout.*
import kotlinx.android.synthetic.main.cmt_popup_layout.view.*
import kotlinx.android.synthetic.main.post_fragment.*

class CommentDialog(
    var list: ArrayList<Comment>,
    var refChild: String,
    var name: String,
    var token: String,
    var putData: PutData,var position : Int
) : DialogFragment() {
    lateinit var rvCm: RecyclerView
    private lateinit var commentApdater: CommentApdater
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var edtWriteComment: EditText
    lateinit var imgSend: ImageView
    var listDialog: ArrayList<Comment> = ArrayList()
    var comment = Comment()
    var listComment = ArrayList<Comment>()
    lateinit var contentComment: String
    lateinit var seaderComment: String
    lateinit var avatarComment: String
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.cmt_popup_layout, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCm = view.findViewById(R.id.rvComment)
        edtWriteComment = view.findViewById(R.id.edtWriteComment)
        imgSend = view.findViewById(R.id.imgSend)
        layoutManager = LinearLayoutManager(this.context)
        rvCm.layoutManager = LinearLayoutManager(this.context)
        commentApdater = CommentApdater(this.context, listDialog)
        rvCm.adapter = commentApdater
        rvCm.setHasFixedSize(true)
        val txtTitleComment: TextView = view.txtTitleComment
        val imgAvtar: CircleImageView = view.imgAvatarAdmin
        txtTitleComment.text = "Spider men and ${list.size - 1} Others Comment this"
        commentViewModel = CommentViewModel()
        commentViewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            it.forEach {
                Glide.with(this).load(it.avatar).into(imgAvtar)
                seaderComment = it.name
                avatarComment = it.avatar

            }
        })
        commentViewModel.getDataComment(refChild).observe(viewLifecycleOwner, Observer {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            rvCm.visibility = View.VISIBLE
            commentApdater.updateData(it)
            listDialog = it
            putData.sendListComment(it,this.position)

        })

        imgSend.setOnClickListener(View.OnClickListener {
            if (edtWriteComment.text.toString() != "") {
                contentComment = edtWriteComment.text.toString()
                comment.content = edtWriteComment.text.toString()
                listComment.clear()
                listComment.add(Comment(contentComment, seaderComment, avatarComment))
                listDialog.addAll(listComment)
                edtWriteComment.text.clear()
                commentViewModel.setCommnet(refChild, listDialog)
                rvCm.postDelayed(Runnable {
                    kotlin.run {
                        rvCm.scrollToPosition(rvCm.adapter!!.itemCount-1)
                    }
                },1000)
                commentViewModel.sendNotification(
                    token,
                    "Thông báo",
                    "${seaderComment} đã comment bài của $name"
                )
                putData.sendListComment(listDialog,this.position)
            }
        })

    }
    interface PutData{
        fun sendListComment(list: ArrayList<Comment>, position: Int)
    }
}