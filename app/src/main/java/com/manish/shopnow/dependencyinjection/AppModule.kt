package com.manish.shopnow.dependencyinjection

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.manish.shopnow.firebase.FirebaseCommon
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseCommon(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FirebaseCommon {
        return FirebaseCommon(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference
}