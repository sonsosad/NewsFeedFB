package com.son.newsfeedfb.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentApdater(var context: Context?, var commentList: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentApdater.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCommentContent: TextView = itemView.txtCommentContent
        val txtName: TextView = itemView.txtName
        val imgAvatar : CircleImageView = itemView.imgAvatarComment

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtName.text = commentList[position].seader
            holder.txtCommentContent.text = commentList[position].content
            Glide.with(context!!).load(commentList[position].avatar).into(holder.imgAvatar)
    }
    fun updateData(list: ArrayList<Comment>){
        this.commentList= list
        notifyDataSetChanged()
    }

}