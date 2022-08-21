package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.auth.store.OpticalStoreDao
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.dao.products.firestore.lens_categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispDao
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpDao
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.feature.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepositoryImpl
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.products.ProductRepositoryImpl
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
        lensTypeCategoryDao: LensTypeCategoryDao,
        prescriptionPictureDao: PrescriptionPictureDao,
        prescriptionDataDao: PrescriptionDataDao,
        framesDataDao: FramesDataDao,
        positioningDao: PositioningDao,
    ): SaleRepository {
        return SaleRepositoryImpl(
            application,
            firebaseManager,
            authenticationRepository,
            activeSalesDao,
            activeSODao,
            lensTypeCategoryDao,
            prescriptionPictureDao,
            prescriptionDataDao,
            framesDataDao,
            positioningDao,
        )
    }

    @Provides
    @Singleton
    fun provideProductsRepository(
        localLensDao: LocalLensDao,
        lensDispDao: LocalLensDispDao,
        lensProductExpDao: LocalProductExpDao,
        saleRepository: SaleRepository,
    ): ProductRepository {
        return ProductRepositoryImpl(
            localLensDao,
            lensDispDao,
            lensProductExpDao,
            saleRepository,
        )
    }
}