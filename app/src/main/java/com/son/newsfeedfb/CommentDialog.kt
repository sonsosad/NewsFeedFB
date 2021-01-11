package com.son.newsfeedfb

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
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.ViewModel.CommentViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.cmt_popup_layout.*
import kotlinx.android.synthetic.main.cmt_popup_layout.view.*

class CommentDialog(var list: ArrayList<Comment>, var refChild: String) : DialogFragment() {
    lateinit var rvCm: RecyclerView
    private lateinit var commentApdater: CommentApdater
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var edtWriteComment: EditText
    lateinit var imgSend: ImageView
    var comment = Comment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.cmt_popup_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCm = view.findViewById(R.id.rvComment)
        edtWriteComment = view.findViewById(R.id.edtWriteComment)
        imgSend = view.findViewById(R.id.imgSend)
        rvCm.layoutManager = LinearLayoutManager(this.context)
        commentApdater = CommentApdater(this.context, list)
        rvCm.adapter = commentApdater
        val txtTitleComment: TextView = view.txtTitleComment
        val imgAvtar: CircleImageView = view.imgAvatarAdmin
        txtTitleComment.text = "Spider men and ${list.size - 1} Others Comment this"
        commentViewModel = CommentViewModel()
        commentViewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            it.forEach {
                Glide.with(this).load(it.avatar).into(imgAvtar)
                comment.avatar = it.avatar
                comment.seader = it.name
            }
        })

        imgSend.setOnClickListener(View.OnClickListener {
            comment.content = edtWriteComment.text.toString()
            list.add(comment)
            commentApdater.updateData(list)
            commentViewModel.setCommnet(refChild, list)
            edtWriteComment.text.clear()
        })
    }
}