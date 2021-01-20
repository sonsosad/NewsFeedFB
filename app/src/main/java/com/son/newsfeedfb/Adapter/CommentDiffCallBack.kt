package com.son.newsfeedfb.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.son.newsfeedfb.Model.Comment

class CommentDiffCallBack(
    private val oldList : List<Comment>,
    private val newList: List<Comment>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] ==newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}