package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.feature.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagersModule {
    @Provides
    @Singleton
    fun provideFirebaseManager(application: SalesApplication): FirebaseManager {
        return FirebaseManager(application = application)
    }

    @Provides
    @Singleton
    fun provideLocalPasscodeManager(
        application: SalesApplication,
        firebaseManager: FirebaseManager,
    ): LocalPasscodeManager {
        return LocalPasscodeManager(application, firebaseManager)
    }
}