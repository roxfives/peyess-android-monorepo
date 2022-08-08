package com.peyess.salesapp.di

import com.peyess.salesapp.dao.store.OpticalStoreDao
import com.peyess.salesapp.dao.users.CollaboratorsDao
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        firebaseManager: FirebaseManager,
        collaboratorsDao: CollaboratorsDao,
        storeDao: OpticalStoreDao,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(firebaseManager, collaboratorsDao, storeDao)
    }
}