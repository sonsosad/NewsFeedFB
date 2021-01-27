package com.son.newsfeedfb

import android.app.Application
import com.son.newsfeedfb.di.ClientComponent
import com.son.newsfeedfb.di.DaggerClientComponent
import com.son.newsfeedfb.di.FireBaseModule
import com.son.newsfeedfb.di.RetrofitModule
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    companion object{
        lateinit var clientComponent: ClientComponent
        val realm: Realm
            get() = clientComponent.realm()
    }

    fun getComponent(): ClientComponent {
        return clientComponent
    }

    override fun onCreate() {
        super.onCreate()
        clientComponent = initDaggerComponent()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().build())
    }
    private fun initDaggerComponent() : ClientComponent{
        clientComponent =
            DaggerClientComponent.builder().fireBaseModule(FireBaseModule()).retrofitModule(
                RetrofitModule()
            ).build()
        return clientComponent
    }
}