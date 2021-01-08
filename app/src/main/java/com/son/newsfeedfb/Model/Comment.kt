package com.son.newsfeedfb.Model

class Comment (){
    lateinit var content: String
    lateinit var seader : String
    lateinit var avatar: String

    constructor(content: String, seader: String,avatar: String) :this(){
        this.content = content
        this.seader = seader
        this.avatar = avatar
    }
}