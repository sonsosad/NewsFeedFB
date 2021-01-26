package com.son.newsfeedfb.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devbrackets.android.exomedia.core.listener.ExoPlayerListener
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.son.newsfeedfb.Model.Admin
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import com.son.newsfeedfb.PostFragment
import com.son.newsfeedfb.R
import com.son.newsfeedfb.ViewModel.CommentViewModel
import com.son.newsfeedfb.ViewModel.GetListPostViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.android.synthetic.main.item_video.view.imgAvatar
import kotlinx.android.synthetic.main.item_video.view.txtCreateAt
import kotlinx.android.synthetic.main.post_header.view.*

class ListPostAdapter(
    var context: Context?,
    var userList: ArrayList<Post>,
    var callback: Callback, var player: SimpleExoPlayer,
    var postUser: Post
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val View_TYPE_HEADER = 0
    private val VIEW_TYPE_ONE = 1
    private val VIEW_TYPE_TWO = 2
    private val VIEW_TYPE_THREE = 3
    private val listCmt = mutableListOf<Comment>()
    private lateinit var post: Post
    private var listComment = ArrayList<Comment>()
    private var commentO = Comment()

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView
        var txtName: TextView
        var txtCreateAt: TextView
        var txtContent: TextView
        var txtCountLike: TextView
        var txtComment: TextView
        var tgLike: ImageView
        var cvComment: CardView
        var getListPostViewModel: GetListPostViewModel
        var commentViewModel: CommentViewModel
        var admin = Admin
        var imgAvatarComment: CircleImageView
        var txtNameBottom: TextView
        var txtCommentBottom: TextView
        var llBottom: LinearLayout

        init {
            imgAvatar = itemView.findViewById(R.id.imgAvatar)
            txtName = itemView.findViewById(R.id.txtName)
            txtCreateAt = itemView.findViewById(R.id.txtCreateAt)
            txtContent = itemView.findViewById(R.id.txtContent)
            txtCountLike = itemView.findViewById(R.id.txtCountLike)
            txtComment = itemView.findViewById(R.id.txtComment)
            tgLike = itemView.findViewById(R.id.tgLike)
            cvComment = itemView.findViewById(R.id.cvComment)
            getListPostViewModel = GetListPostViewModel()
            commentViewModel = CommentViewModel()
            commentViewModel.getObjectCurrent()
            imgAvatarComment = itemView.findViewById(R.id.imgAvatarComment)
            txtNameBottom = itemView.findViewById(R.id.txtNameBottom)
            txtCommentBottom = itemView.findViewById(R.id.txtCommentBottom)
            llBottom = itemView.findViewById(R.id.llBottom)
        }

        internal fun bind(
            userList: List<Post>,
            position: Int,
            context: Context?,
            callback: Callback
        ) {
            Log.e("Tag", "userL ${userList.size}")
            Log.e("Tag", "list realtime ${userList[position].comment?.size}")
            txtName.text = userList[position].name
            txtCreateAt.text = userList[position].createAt
            txtContent.text = userList[position].content
            txtCountLike.text = userList[position].like.toString()
            if (context != null) {
                Glide.with(context).load(userList[position].avatar).into(imgAvatar)
            }
            if (userList[position].comment?.size == 0 || userList[position].comment?.size == null) {
                txtComment.text = "0 comments"
                llBottom.visibility = View.GONE
            } else {
                val pos = userList[position].comment?.count()?.minus(1)
                llBottom.visibility = View.VISIBLE
                txtComment.text =
                    userList[position].comment!!.size.toString() + " comments"
                txtCommentBottom.text = pos?.let { userList[position].comment?.get(it)?.content }
                txtNameBottom.text = pos?.let { userList[position].comment?.get(it)?.seader }
                if (context != null) {
                    Glide.with(context)
                        .load(pos?.let { userList[position].comment?.get(it)?.avatar })
                        .into(imgAvatarComment)
                }
            }

            tgLike.setOnClickListener {
                if (tgLike.drawable.level == 0) {
                    tgLike.setImageLevel(1)
                    userList[position].like += 1
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id, userList[position].like)
                    commentViewModel.sendNotification(
                        userList[position].token,
                        "Thông báo Like",
                        "${admin.getId().nameAmdin} đã thích bài của ${userList[position].name}"
                    )
                } else {
                    tgLike.setImageLevel(0)
                    userList[position].like -= 1
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id, userList[position].like)
                }
            }
            cvComment.setOnClickListener(View.OnClickListener {
                callback.onClickItem(
                    userList[position].id,
                    userList[position].name,
                    userList[position].token, position
                )
            })
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
        var tgLike: ImageView
        var cvComment: CardView
        var getListPostViewModel: GetListPostViewModel
        var commentViewModel: CommentViewModel
        var admin = Admin
        var imgAvatarComment: CircleImageView
        var txtNameBottom: TextView
        var txtCommentBottom: TextView
        var llBottom: LinearLayout

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
            commentViewModel = CommentViewModel()
            commentViewModel.getObjectCurrent()
            imgAvatarComment = itemView.findViewById(R.id.imgAvatarComment)
            txtNameBottom = itemView.findViewById(R.id.txtNameBottom)
            txtCommentBottom = itemView.findViewById(R.id.txtCommentBottom)
            llBottom = itemView.findViewById(R.id.llBottom)
        }

        @SuppressLint("SetTextI18n")
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

            if (context != null) {
                Glide.with(context).load(userList[position].avatar).into(imgAvatar)
            }
            if (context != null) {
                Glide.with(context).load(userList[position].content).into(imgPost)
            }
            if (userList[position].comment?.size == 0 || userList[position].comment?.size == null) {
                txtComment.text = "0 comments"
                llBottom.visibility = View.GONE
            } else {
                val pos = userList[position].comment?.count()?.minus(1)
                llBottom.visibility = View.VISIBLE
                txtComment.text =
                    userList[position].comment?.size.toString() + " comments"
                txtCommentBottom.text = pos?.let { userList[position].comment?.get(it)?.content }
                txtNameBottom.text = pos?.let { userList[position].comment?.get(it)?.seader }
                if (context != null) {
                    Glide.with(context)
                        .load(pos?.let { userList[position].comment?.get(it)?.avatar })
                        .into(imgAvatarComment)
                }
            }
            tgLike.setOnClickListener {
                if (tgLike.drawable.level == 0) {
                    tgLike.setImageLevel(1)
                    userList[position].like += 1
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id, userList[position].like)
                    commentViewModel.sendNotification(
                        userList[position].token,
                        "Thông báo Like",
                        "${admin.getId().nameAmdin} đã thích bài của ${userList[position].name}"
                    )
                } else {
                    tgLike.setImageLevel(0)
                    userList[position].like -= 1
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id, userList[position].like)
                }
            }
            cvComment.setOnClickListener(View.OnClickListener {
                callback.onClickItem(
                    userList[position].id,
                    userList[position].name,
                    userList[position].token, position
                )
            })
        }
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView
        private var txtName: TextView
        private var txtCreateAt: TextView
        private var vdPost: PlayerView
        private var txtCountLike: TextView
        private var txtComment: TextView
        private var txtTitle: TextView
        private var tgLike: ImageView
        private var getListPostViewModel: GetListPostViewModel
        private var cvComment: CardView
        var admin = Admin
        private var commentViewModel: CommentViewModel
        private var imgAvatarComment: CircleImageView
        private var txtNameBottom: TextView
        private var txtCommentBottom: TextView

        init {
            imgAvatar = itemView.findViewById(R.id.imgAvatar)
            txtName = itemView.findViewById(R.id.txtName)
            txtCreateAt = itemView.txtCreateAt
            vdPost = itemView.vdPost
            txtCountLike = itemView.findViewById(R.id.txtCountLike)
            txtComment = itemView.findViewById(R.id.txtComment)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            tgLike = itemView.findViewById(R.id.tgLike)
            getListPostViewModel = GetListPostViewModel()
            cvComment = itemView.findViewById(R.id.cvComment)
            commentViewModel = CommentViewModel()
            commentViewModel.getObjectCurrent()
            imgAvatarComment = itemView.findViewById(R.id.imgAvatarComment)
            txtNameBottom = itemView.findViewById(R.id.txtNameBottom)
            txtCommentBottom = itemView.findViewById(R.id.txtCommentBottom)
        }

        @SuppressLint("CheckResult")
        fun bind(
            userList: List<Post>,
            position: Int,
            context: Context?,
            holder: RecyclerView.ViewHolder,
            callback: Callback,
            player: SimpleExoPlayer
        ) {
            val pos = userList[position].comment!!.count() - 1
            txtName.text = userList[position].name
            txtCreateAt.text = userList[position].createAt
            txtCountLike.text = userList[position].like.toString()
            txtTitle.text = userList[position].title
            txtComment.text =
                userList[position].comment!!.size.toString() + " comments"
            if (context != null) {
                Glide.with(context).load(userList[position].avatar).into(imgAvatar)
                Glide.with(context).load(userList[position].comment!![pos].avatar)
                    .into(imgAvatarComment)
            }
            txtCommentBottom.text = userList[position].comment!![pos].content
            txtNameBottom.text = userList[position].comment!![pos].seader
            holder.itemView.apply {
                val defaultDataSourceFactory = DefaultDataSourceFactory(
                    this.context,
                    com.google.android.exoplayer2.util.Util.getUserAgent(this.context, "newfeedd")
                )
                val dataSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                    .createMediaSource(Uri.parse(userList[position].content))
                vdPost.requestFocus()
                vdPost.player = player
                player.prepare(dataSource)
                player.playWhenReady = true
                vdPost.setKeepContentOnPlayerReset(true)
//                if (player!!.playbackState == Player.STATE_READY){
//                    player!!.play()
//                }
            }
            tgLike.setOnClickListener {
                if (tgLike.drawable.level == 0) {
                    tgLike.setImageLevel(1)
                    userList[position].like += 1
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id, userList[position].like)
                    commentViewModel.sendNotification(
                        userList[position].token,
                        "Thông báo Like",
                        "${admin.getId().nameAmdin} đã thích bài của ${userList[position].name}"
                    )
                } else {
                    tgLike.setImageLevel(0)
                    userList[position].like -= 1
                    txtCountLike.text = userList[position].like.toString()
                    getListPostViewModel.getLike(userList[position].id, userList[position].like)
                }
            }
            cvComment.setOnClickListener(View.OnClickListener {
                callback.onClickItem(
                    userList[position].id,
                    userList[position].name,
                    userList[position].token,
                    position
                )


            })

        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: CircleImageView
        var llImage: LinearLayout
        var imgPost: ImageView
        val edtContent: EditText
        var getListPostViewModel: GetListPostViewModel

        init {
            imgAvatar = itemView.imgAvatar
            imgPost = itemView.imgPost
            llImage = itemView.llImage
            edtContent = itemView.edtContent
            getListPostViewModel = GetListPostViewModel()
        }

        fun bin(
            post: Post,
            context: Context?,
            callback: Callback
        ) {
            if (context != null) {
                Glide.with(context).load(post.avatar).into(imgAvatar)
                llImage.setOnClickListener(View.OnClickListener {
                    callback.getImage()
                })
                imgPost.setOnClickListener(View.OnClickListener {
                    callback.upload(edtContent.text.toString(), post.avatar, post.token)
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return when (viewType) {
            0 -> HeaderViewHolder(
                inflater.inflate(R.layout.post_header, parent, false)
            )
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
            is HeaderViewHolder -> (holder as HeaderViewHolder).bin(postUser, context, callback)
            is ContentViewHolder -> (holder as ContentViewHolder).bind(
                userList,
                position,
                context,
                callback
            )
            is ImageViewHolder -> (holder as ImageViewHolder).bind(
                userList,
                position,
                context,
                callback
            )
            else -> (holder as VideoViewHolder).bind(
                userList,
                position,
                context,
                holder,
                callback,
                player
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            holder.itemView.txtComment.text = "${listComment.size} comments"
            holder.itemView.txtCommentBottom.text = commentO.content
            holder.itemView.txtNameBottom.text = commentO.seader
            context?.let {
                Glide.with(it).load(commentO.avatar).into(holder.itemView.imgAvatarComment)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (positonHeader(position))
            return View_TYPE_HEADER
        return when (userList[position].viewType) {
            1 -> VIEW_TYPE_ONE
            2 -> VIEW_TYPE_TWO
            else -> VIEW_TYPE_THREE
        }

    }

    private fun positonHeader(position: Int): Boolean {
        return position == 0
    }

    fun updateComment(position: Int, list: ArrayList<Comment>) {
        var comment: Comment = list.get(list.size - 1)
        post = userList[position]
        post.comment = list
        notifyItemChanged(position, list.toMutableList())
        notifyItemChanged(position, comment)
        listComment = list
        commentO = comment
    }

    fun updateData(list: ArrayList<Post>) {
        this.userList = list
        notifyDataSetChanged()
    }

    fun updatePost(post: Post) {
        this.postUser = post
        notifyDataSetChanged()
    }

    interface Callback {
        fun onClickItem(
            refChild: String, name: String, token: String, position: Int
        )

        fun getImage()
        fun upload(content: String, avatar: String, token: String)
        fun getStt(
            content: String, avatar: String, token: String
        )
    }

    fun swap(listComment: List<Comment>) {
        val diffCallBack = CommentDiffCallBack(this.listCmt, listComment)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        this.listCmt.clear()
        this.listCmt.addAll(listComment)
        diffResult.dispatchUpdatesTo(this)
    }

}
