package com.son.newsfeedfb.Model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAgTdTe3Q:APA91bEIujTCyvP05uNFWyrevY2WEdMZKlpQv_x7tYlqH1_b0djC6PMrz5lfVf9zE4rR85oz3L4mq8o98-lOws8j0vXVJ8j6jc__xbFEj-xxKkfuFlNkYpr-toFXG7AwcIMZW-EhxB-0"
    )
    @POST("fcm/send")
    fun sendNotifcation(@Body message: Message): Call<Message>
}