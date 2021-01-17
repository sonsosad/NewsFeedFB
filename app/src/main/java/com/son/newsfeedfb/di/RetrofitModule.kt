package com.son.newsfeedfb.di

import com.son.newsfeedfb.Model.APIService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
class RetrofitModule {
    private  val BASE_URL = "https://fcm.googleapis.com/"
    @Singleton
    @Provides
    fun retrofitModule() : APIService{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}