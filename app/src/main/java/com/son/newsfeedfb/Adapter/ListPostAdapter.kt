package com.son.newsfeedfb.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.PostFragment
import com.son.newsfeedfb.R
import com.son.newsfeedfb.ViewModel.GetListPostViewModel
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.android.synthetic.main.item_video.view.txtCreateAt

class ListPostAdapter(var context: Context?, var userList: ArrayList<Post>, var callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal val VIEW_TYPE_ONE = 1
    internal val VIEW_TYPE_TWO = 2
    internal val VIEW_TYPE_THREE = 3
    lateinit var getListPostViewModel: GetListPostViewModel

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView
        var txtName: TextView
        var txtCreateAt: TextView
        var txtContent: TextView
        var txtCountLike: TextView
        var txtComment: TextView
        var tgLike: ToggleButton

        init {
            imgAvatar = itemView.findViewById(R.id.imgAvatar)
            txtName = itemView.findViewById(R.id.txtName)
            txtCreateAt = itemView.findViewById(R.id.txtCreateAt)
            txtContent = itemView.findViewById(R.id.txtContent)
            txtCountLike = itemView.findViewById(R.id.txtCountLike)
            txtComment = itemView.findViewById(R.id.txtComment)
            tgLike = itemView.findViewById(R.id.tgLike)

        }

        internal fun bind(userList: List<Post>, position: Int, context: Context?) {
            txtName.text = userList[position].name
            txtCreateAt.text = userList[position].createAt
            txtContent.text = userList[position].content
            txtCountLike.text = userList[position].like.toString()
            txtComment.text =
                userList[position].comment.size.toString() + " comments"
            if (context != null) {
                Glide.with(context).load(userList[position].avatar).into(imgAvatar)
            }
            tgLike.isChecked = false
            tgLike.background =
                context?.let { getDrawable(it,
                    R.drawable.ic_baseline_favorite_border_24
                ) }
            tgLike.setOnCheckedChangeListener { _, b ->
                if (b) {
                    tgLike.background =
                        context?.let { getDrawable(it,
                            R.drawable.ic_baseline_favorite_24
                        ) }
                } else
                    tgLike.background =
                        context?.let { getDrawable(it,
                            R.drawable.ic_baseline_favorite_border_24
                        ) }
            }
        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView
        var txtName: TextView
        var txtCreateAt: TextView
        var imgPost: ImageView
        var txtCountLike: TextView
        var txtComment: TextView
        var txtTitle: TextView
        var tgLike: ToggleButton
        var cvComment : CardView
        var getListPostViewModel: GetListPostViewModel

        init {
            imgAvatar = itemView.findViewById(R.id.imgAvatar)
            txtName = itemView.findViewById(R.id.txtName)
            txtCreateAt = itemView.findViewById(R.id.txtCreateAt)
            imgPost = itemView.findViewById(R.id.imgPost)
            txtCountLike = itemView.findViewById(R.id.txtCountLike)
            txtComment = itemView.findViewById(R.id.txtComment)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            tgLike = itemView.findViewById(R.id.tgLike)
            cvComment = itemView.findViewById(R.id.cvComment)
            getListPostViewModel = GetListPostViewModel()
        }

        internal fun bind(
            userList: List<Post>,
            position: Int,
            context: Context?,
            callback: Callback
        ) {
            txtName.text = userList[position].name
            txtCreateAt.text = userList[position].createAt
            txtCountLike.text = userList[position].like.toString()
            txtTitle.text = userList[position].title
            txtComment.text =
                userList[position].comment.size.toString() + " comments"
            if (context != null) {
                Glide.with(context).load(userList[position].avatar).into(imgAvatar)
            }
            if (context != null) {
                Glide.with(context).load(userList[position].content).into(imgPost)
            }
            tgLike.isChecked = false
            tgLike.background =
                context?.let { getDrawable(it,
                    R.drawable.ic_baseline_favorite_border_24
                ) }
            tgLike.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    tgLike.background =
                        context?.let { getDrawable(it,
                            R.drawable.ic_baseline_favorite_24
                        ) }
                    userList[position].like += 2
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id,userList[position].like)
                    Log.e("Tag", "cl" + userList[position].like.toString())
                } else
                    tgLike.background = context?.let {
                        getDrawable(it,
                            R.drawable.ic_baseline_favorite_border_24
                        )
                    }
                userList[position].like -= 1
                txtCountLike.text = userList[position].like.toString()
                getListPostViewModel.getLike(userList[position].id,userList[position].like)
                Log.e("Tag", "cl2" + userList[position].like.toString())
            }

            cvComment.setOnClickListener(View.OnClickListener {
                callback.onClickItem(userList[position].comment)

            })
        }
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView
        var txtName: TextView
        var txtCreateAt: TextView
        var vdPost: PlayerView
        var txtCountLike: TextView
        var txtComment: TextView
        var txtTitle: TextView
        var tgLike: ToggleButton
        var player: SimpleExoPlayer? = null

        init {
            imgAvatar = itemView.findViewById(R.id.imgAvatar)
            txtName = itemView.findViewById(R.id.txtName)
            txtCreateAt = itemView.txtCreateAt
            vdPost = itemView.vdPost
            txtCountLike = itemView.findViewById(R.id.txtCountLike)
            txtComment = itemView.findViewById(R.id.txtComment)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            tgLike = itemView.findViewById(R.id.tgLike)
        }

        internal fun bind(
            userList: List<Post>,
            position: Int,
            context: Context?,
            holder: RecyclerView.ViewHolder
        ) {
            txtName.text = userList[position].name
            txtCreateAt.text = userList[position].createAt
            txtCountLike.text = userList[position].like.toString()
            txtTitle.text = userList[position].title
            txtComment.text =
                userList[position].comment.size.toString() + " comments"
            if (context != null) {
                Glide.with(context).load(userList[position].avatar).into(imgAvatar)
            }
            holder.itemView.apply {
                player = ExoPlayerFactory.newSimpleInstance(this.context, DefaultTrackSelector())
                var defaultDataSourceFactory = DefaultDataSourceFactory(
                    this.context,
                    com.google.android.exoplayer2.util.Util.getUserAgent(this.context, "newfeedd")
                )
                var dataSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                    .createMediaSource(Uri.parse(userList[position].content))
                player!!.prepare(dataSource)
                player!!.playWhenReady = true
                vdPost.player = player
                vdPost.requestFocus()
            }
            tgLike.isChecked = false
            tgLike.background =
                context?.let { getDrawable(it,
                    R.drawable.ic_baseline_favorite_border_24
                ) }
            tgLike.setOnCheckedChangeListener { _, b ->
                if (b) {
                    tgLike.background =
                        context?.let { getDrawable(it,
                            R.drawable.ic_baseline_favorite_24
                        ) }
                } else
                    tgLike.background =
                        context?.let { getDrawable(it,
                            R.drawable.ic_baseline_favorite_border_24
                        ) }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return when (viewType) {
            1 -> ContentViewHolder(
                inflater.inflate(
                    R.layout.item_content,
                    parent,
                    false
                )
            )
            2 -> ImageViewHolder(
                inflater.inflate(
                    R.layout.item_image,
                    parent,
                    false
                )
            )
            else -> VideoViewHolder(
                inflater.inflate(
                    R.layout.item_video,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContentViewHolder -> (holder as ContentViewHolder).bind(userList, position, context)
            is ImageViewHolder -> (holder as ImageViewHolder).bind(userList, position, context,callback)
            else -> (holder as VideoViewHolder).bind(userList, position, context, holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (userList[position].viewType) {
            1 -> VIEW_TYPE_ONE
            2 -> VIEW_TYPE_TWO
            else -> VIEW_TYPE_THREE
        }
    }

    fun updateData(list: ArrayList<Post>) {
        this.userList = list
        notifyDataSetChanged()
    }
    interface Callback{
        fun onClickItem(list : ArrayList<Comment>
        )
    }

    fun nofti(position: Int) {
        notifyItemChanged(position)
    }


}
