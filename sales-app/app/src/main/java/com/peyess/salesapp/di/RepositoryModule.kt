package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.auth.store.OpticalStoreDao
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.feature.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepositoryImpl
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.sale.SaleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        application: SalesApplication,
        firebaseManager: FirebaseManager,
        localPasscodeManager: LocalPasscodeManager,
        collaboratorsDao: CollaboratorsDao,
        storeDao: OpticalStoreDao,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            application,
            firebaseManager,
            localPasscodeManager,
            collaboratorsDao,
            storeDao,
        )
    }

    @Provides
    @Singleton
    fun provideSaleRepository(
        application: SalesApplication,
        firebaseManager: FirebaseManager,
        authenticationRepository: AuthenticationRepository,
        activeSalesDao: ActiveSalesDao,
        activeSODao: ActiveSODao,
        prescriptionPictureDao: PrescriptionPictureDao,
    ): SaleRepository {
        return SaleRepositoryImpl(
            application,
            firebaseManager,
            authenticationRepository,
            activeSalesDao,
            activeSODao,
            prescriptionPictureDao,
        )
    }
}