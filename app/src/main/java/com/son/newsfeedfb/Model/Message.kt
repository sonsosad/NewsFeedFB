package com.son.newsfeedfb.Model

class Message {
     var to : String
     var notification: Notification

    constructor(to: String, notification: Notification) {
        this.to = to
        this.notification = notification
    }
    
}