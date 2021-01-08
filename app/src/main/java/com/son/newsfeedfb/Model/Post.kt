package com.son.newsfeedfb.Model

class   Post {
    lateinit var authorID: String
    lateinit var id : String
    var viewType: Int = 1
    lateinit var avatar: String
    lateinit var name : String
    lateinit var title : String
    lateinit var content : String
    lateinit var createAt : String
     var like: Int = 20
    lateinit var comment: ArrayList<Comment>

}