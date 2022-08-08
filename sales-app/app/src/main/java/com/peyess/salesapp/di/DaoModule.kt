package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.store.OpticalStoreDao
import com.peyess.salesapp.dao.store.OpticalStoreDaoImpl
import com.peyess.salesapp.dao.users.CollaboratorsDao
import com.peyess.salesapp.dao.users.CollaboratorsDaoImpl
import com.peyess.salesapp.firebase.FirebaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Singleton
    @Provides
    fun provideCollaboratorsDao(
        firebaseManager: FirebaseManager,
        application: SalesApplication,
    ): CollaboratorsDao {
        return CollaboratorsDaoImpl(firebaseManager, application)
    }

    @Singleton
    @Provides
    fun provideStoreDao(
        firebaseManager: FirebaseManager,
        application: SalesApplication,
    ): OpticalStoreDao {
        return OpticalStoreDaoImpl(firebaseManager, application)
    }
}