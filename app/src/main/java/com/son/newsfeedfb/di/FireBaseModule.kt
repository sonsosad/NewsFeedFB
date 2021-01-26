package com.son.newsfeedfb.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.son.newsfeedfb.Model.Comment
import com.son.newsfeedfb.Model.Post
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class FireBaseModule {
    @Singleton
    @Provides
    fun provideDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideDataRef(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("Post")
    }

    @Provides
    fun provideComment(): Comment {
        return Comment()
    }

    @Singleton
    @Provides
    fun provideStorageRef() : StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Provides
    fun providePost(): Post {
        return Post()

    }
    @Provides
    internal fun provideRealm(): Realm = Realm.getDefaultInstance()
}