package com.son.newsfeedfb

import android.app.Application
import com.son.newsfeedfb.di.ClientComponent
import com.son.newsfeedfb.di.DaggerClientComponent
import com.son.newsfeedfb.di.FireBaseModule
import com.son.newsfeedfb.di.RetrofitModule

class MyApplication : Application() {
    companion object{
        lateinit var clientComponent: ClientComponent
    }

    fun getComponent(): ClientComponent {
        return clientComponent
    }

    override fun onCreate() {
        super.onCreate()
        clientComponent = initDaggerComponent()

    }
    fun initDaggerComponent() : ClientComponent{
        clientComponent =
            DaggerClientComponent.builder().fireBaseModule(FireBaseModule()).retrofitModule(
                RetrofitModule()
            ).build()
        return clientComponent
    }
}