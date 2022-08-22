package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.auth.store.OpticalStoreDao
import com.peyess.salesapp.dao.auth.store.OpticalStoreDaoImpl
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.dao.auth.users.CollaboratorsDaoImpl
import com.peyess.salesapp.dao.products.firestore.lens_categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.products.firestore.lens_categories.LensCategoryDaoImpl
import com.peyess.salesapp.dao.products.firestore.lens_groups.LensGroupDao
import com.peyess.salesapp.dao.products.firestore.lens_groups.LensGroupDaoImpl
import com.peyess.salesapp.dao.products.room.join_lens_coloring.JoinLensColoringDao
import com.peyess.salesapp.dao.products.room.join_lens_treatment.JoinLensTreatmentDao
import com.peyess.salesapp.dao.products.room.local_alt_height.LocalAltHeightDao
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringDao
import com.peyess.salesapp.dao.products.room.local_coloring_display.LocalColoringDisplayDao
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
import com.peyess.salesapp.dao.products.room.local_lens_base.LocalLensBaseDao
import com.peyess.salesapp.dao.products.room.local_lens_category_type.LocalLensCategoryTypeDao
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispDao
import com.peyess.salesapp.dao.products.room.local_lens_disp_manufacturer.LocalLensDispManufacturerDao
import com.peyess.salesapp.dao.products.room.local_lens_material_type.LocalLensMaterialTypeDao
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpDao
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentDao
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.database.room.ProductsDatabase
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

    @Singleton
    @Provides
    fun provideLensTypeDao(
        firebaseManager: FirebaseManager,
        application: SalesApplication,
    ): LensTypeCategoryDao {
        return LensCategoryDaoImpl(application, firebaseManager)
    }


    @Singleton
    @Provides
    fun provideLensGroupDao(
        firebaseManager: FirebaseManager,
        application: SalesApplication,
    ): LensGroupDao {
        return LensGroupDaoImpl(application, firebaseManager)
    }

    @Singleton
    @Provides
    fun provideActiveSalesDao(appDatabase: ActiveSalesDatabase): ActiveSalesDao {
        return appDatabase.activeSalesDao()
    }

    @Singleton
    @Provides
    fun provideSODao(appDatabase: ActiveSalesDatabase): ActiveSODao {
        return appDatabase.activeSODao()
    }

    @Singleton
    @Provides
    fun provideJoinLensTreatmentDao(appDatabase: ProductsDatabase): JoinLensTreatmentDao {
        return appDatabase.joinLensTreatmentDao()
    }

    @Singleton
    @Provides
    fun provideJoinLensColoringDao(appDatabase: ProductsDatabase): JoinLensColoringDao {
        return appDatabase.joinLensColoringDao()
    }

    @Singleton
    @Provides
    fun provideLocalAltHeightDao(appDatabase: ProductsDatabase): LocalAltHeightDao {
        return appDatabase.localAltHeightDao()
    }

    @Singleton
    @Provides
    fun provideLocalColoringDao(appDatabase: ProductsDatabase): LocalColoringDao {
        return appDatabase.localColoringDao()
    }

    @Singleton
    @Provides
    fun provideLocalColoringDisplayDao(appDatabase: ProductsDatabase): LocalColoringDisplayDao {
        return appDatabase.localColoringDisplayDao()
    }

    @Singleton
    @Provides
    fun provideLocalLensDao(appDatabase: ProductsDatabase): LocalLensDao {
        return appDatabase.localLensDao()
    }

    @Singleton
    @Provides
    fun provideLocalLensBaseDao(appDatabase: ProductsDatabase): LocalLensBaseDao {
        return appDatabase.localLensBaseDao()
    }

    @Singleton
    @Provides
    fun provideLocalLensCategoryTypeDao(appDatabase: ProductsDatabase): LocalLensCategoryTypeDao {
        return appDatabase.localLensCategoryTypeDao()
    }

    @Singleton
    @Provides
    fun provideLocalLensDispManufacturerDao(appDatabase: ProductsDatabase): LocalLensDispManufacturerDao {
        return appDatabase.localLensDispManufacturerDao()
    }

    @Singleton
    @Provides
    fun provideLocalLensMaterialTypeDao(appDatabase: ProductsDatabase): LocalLensMaterialTypeDao {
        return appDatabase.localLensMaterialTypeDao()
    }

    @Singleton
    @Provides
    fun provideLocalProductExpDao(appDatabase: ProductsDatabase): LocalProductExpDao {
        return appDatabase.localProdExpDao()
    }

    @Singleton
    @Provides
    fun provideLocalTreatmentDao(appDatabase: ProductsDatabase): LocalTreatmentDao {
        return appDatabase.localTreatmentDao()
    }

    @Singleton
    @Provides
    fun provideLensDispDao(productsDatabase: ProductsDatabase): LocalLensDispDao {
        return productsDatabase.localLensDispEntityDao()
    }

    @Singleton
    @Provides
    fun providePrescriptionPictureDao(saleDatabase: ActiveSalesDatabase): PrescriptionPictureDao {
        return saleDatabase.prescriptionPictureDao()
    }

    @Singleton
    @Provides
    fun providePrescriptionDataDao(saleDatabase: ActiveSalesDatabase): PrescriptionDataDao {
        return saleDatabase.prescriptionDataDao()
    }

    @Singleton
    @Provides
    fun provideFramesDataDao(saleDatabase: ActiveSalesDatabase): FramesDataDao {
        return saleDatabase.framesDataDao()
    }

    @Singleton
    @Provides
    fun providePositioningDao(saleDatabase: ActiveSalesDatabase): PositioningDao {
        return saleDatabase.positioningDao()
    }
}