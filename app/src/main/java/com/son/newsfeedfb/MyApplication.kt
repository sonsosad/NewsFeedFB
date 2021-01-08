package com.son.newsfeedfb

import android.app.Application
import com.son.newsfeedfb.di.ClientComponent
import com.son.newsfeedfb.di.DaggerClientComponent
import com.son.newsfeedfb.di.FireBaseModule

class MyApplication: Application() {
    lateinit var clientComponent: ClientComponent
    fun getComponent(): ClientComponent{
        return clientComponent
    }
    override fun onCreate() {
        super.onCreate()
        clientComponent = DaggerClientComponent.builder().fireBaseModule(FireBaseModule()).build()
    }
}