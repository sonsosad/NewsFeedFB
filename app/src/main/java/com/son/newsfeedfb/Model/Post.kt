package com.son.newsfeedfb.Model

import io.realm.RealmObject

class Post {
    lateinit var authorID: String
    lateinit var id: String
    var viewType: Int = 1
    lateinit var avatar: String
    lateinit var name: String
    lateinit var title: String
    lateinit var content: String
    lateinit var createAt: String
    lateinit var token: String
    var like: Int = 20
    var comment: ArrayList<Comment>? = null
    override fun toString(): String {
        return "$authorID - $id - $viewType - $avatar - $name - $title - $content - $createAt - $token"
    }

//    override fun compareTo(other: Post): Int {
//    }
}