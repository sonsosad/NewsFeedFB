package com.son.newsfeedfb.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FireBaseModule {
    @Singleton
    @Provides
        fun provideDatabase(): FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }
    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun provideDataRef(): DatabaseReference{
        return FirebaseDatabase.getInstance().getReference("Post")
    }
    @Provides
    fun provideComment(): Comment{
        return Comment()
    }
    @Provides
    fun providePost(): Post{
        return  Post()
    }
}