package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.auth.store.OpticalStoreDao
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.dao.client.firestore.ClientDao
import com.peyess.salesapp.dao.client.room.ClientPickedDao
import com.peyess.salesapp.dao.payment_methods.PaymentMethodDao
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDao
import com.peyess.salesapp.data.model.lens.description.LensDescriptionDao
import com.peyess.salesapp.data.model.lens.groups.LensGroupDao
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensMaterialDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSpecialtyDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTechDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTypeDao
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringDao
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispDao
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpDao
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentDao
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.dao.sale.payment.SalePaymentDao
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.data.dao.service_order.ServiceOrderDao
import com.peyess.salesapp.data.dao.address_lookup.AddressLookupDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.card_flag.CardFlagDao
import com.peyess.salesapp.data.dao.client.ClientLegalDao
import com.peyess.salesapp.data.dao.discount.OverallDiscountDao
import com.peyess.salesapp.data.dao.lenses.StoreLensesDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensCategoryDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensDescriptionDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensFamilyDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensGroupDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensMaterialCategoryDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensMaterialDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensSpecialtyDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensSupplierDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensTechDao
import com.peyess.salesapp.data.dao.lenses.room.LocalLensTypeDao
import com.peyess.salesapp.data.dao.measuring.MeasuringDao
import com.peyess.salesapp.data.dao.payment_fee.PaymentFeeDao
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.dao.products_table_state.ProductsTableStateDao
import com.peyess.salesapp.data.dao.purchase.PurchaseDao
import com.peyess.salesapp.data.dao.purchase.discount.DiscountGroupDao
import com.peyess.salesapp.data.repository.address_lookup.AddressLookupRepository
import com.peyess.salesapp.data.repository.address_lookup.AddressLookupRepositoryImpl
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepositoryImpl
import com.peyess.salesapp.feature.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepositoryImpl
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.client.ClientRepositoryImpl
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepositoryImpl
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryImpl
import com.peyess.salesapp.data.repository.lenses.StoreLensesRepository
import com.peyess.salesapp.data.repository.lenses.StoreLensesRepositoryImpl
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepositoryImpl
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.measuring.MeasuringRepositoryImpl
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepositoryImpl
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryImpl
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.positioning.PositioningRepositoryImpl
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepositoryImpl
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepositoryImpl
import com.peyess.salesapp.data.repository.purchase.DiscountGroupRepository
import com.peyess.salesapp.data.repository.purchase.DiscountGroupRepositoryImpl
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.payments.PaymentMethodRepositoryImpl
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.products.ProductRepositoryImpl
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.sale.SaleRepositoryImpl
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepositoryImpl
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
    fun provideCollaboratorRepository(
        collaboratorsDao: CollaboratorsDao,
    ): CollaboratorsRepository {
        return CollaboratorsRepositoryImpl(collaboratorsDao)
    }

    @Provides
    @Singleton
    fun provideClientRepository(
        salesApplication: SalesApplication,
        firebaseManager: FirebaseManager,
        cacheCreateClientDao: CacheCreateClientDao,
        clientDao: ClientDao,
        clientLegalDao: ClientLegalDao,
        authenticationRepository: AuthenticationRepository,
    ): ClientRepository {
        return ClientRepositoryImpl(
            salesApplication,
            firebaseManager,
            cacheCreateClientDao,
            clientDao,
            clientLegalDao,
            authenticationRepository,
        )
    }

    @Provides
    @Singleton
    fun provideServiceOrderRepository(
        serviceOrderDao: ServiceOrderDao,
    ): ServiceOrderRepository {
        return ServiceOrderRepositoryImpl(serviceOrderDao)
    }

    @Provides
    @Singleton
    fun provideOverallDiscountRepository(
        overallDiscountDao: OverallDiscountDao,
    ): OverallDiscountRepository {
        return OverallDiscountRepositoryImpl(overallDiscountDao)
    }

    @Provides
    @Singleton
    fun provideDiscountGroupRepository(
        discountGroupDao: DiscountGroupDao,
    ): DiscountGroupRepository {
        return DiscountGroupRepositoryImpl(discountGroupDao)
    }

    @Provides
    @Singleton
    fun providePaymentFeeRepository(
        paymentFeeDao: PaymentFeeDao,
    ): PaymentFeeRepository {
        return PaymentFeeRepositoryImpl(paymentFeeDao)
    }

    @Provides
    @Singleton
    fun provideCardFlagRepository(
        cardFlagDao: CardFlagDao,
    ): CardFlagRepository {
        return CardFlagRepositoryImpl(cardFlagDao)
    }

    @Provides
    @Singleton
    fun providePositioningRepository(
        positioningDao: com.peyess.salesapp.data.dao.positioning.PositioningDao,
    ): PositioningRepository {
        return PositioningRepositoryImpl(positioningDao)
    }

    @Provides
    @Singleton
    fun providePrescriptionRepository(
        prescriptionDao: PrescriptionDao,
    ): PrescriptionRepository {
        return PrescriptionRepositoryImpl(prescriptionDao)
    }

    @Provides
    @Singleton
    fun provideMeasuringRepository(
        measuringDao: MeasuringDao,
    ): MeasuringRepository {
        return MeasuringRepositoryImpl(measuringDao)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        clientDao: PaymentMethodDao
    ): PaymentMethodRepository {
        return PaymentMethodRepositoryImpl(clientDao)
    }

    @Provides
    @Singleton
    fun providePurchaseRepository(
        purchaseDao: PurchaseDao
    ): PurchaseRepository {
        return PurchaseRepositoryImpl(purchaseDao)
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
        comparisonDao: LensComparisonDao,
        productPickedDao: ProductPickedDao,
        clientPickedDao: ClientPickedDao,
        salePaymentDao: SalePaymentDao,
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
            comparisonDao,
            productPickedDao,
            clientPickedDao,
            salePaymentDao,
        )
    }

    @Provides
    @Singleton
    fun provideProductsRepository(
        salesApplication: SalesApplication,
        localLensDao: LocalLensDao,
        localTreatmentDao: LocalTreatmentDao,
        localColoringDao: LocalColoringDao,
        lensDispDao: LocalLensDispDao,
        lensProductExpDao: LocalProductExpDao,
        lensGroupDao: LensGroupDao,
        lensSpecialtyDao: FilterLensSpecialtyDao,
        filterLensTypeDao: FilterLensTypeDao,
        lensSupplierDao: FilterLensSupplierDao,
        lensMaterialDao: FilterLensMaterialDao,
        lensTechDao: FilterLensTechDao,
        lensFamilyDao: FilterLensFamilyDao,
        lensDescriptionDao: LensDescriptionDao,
        saleRepository: SaleRepository,
    ): ProductRepository {
        return ProductRepositoryImpl(
            salesApplication,
            localLensDao,
            localTreatmentDao,
            localColoringDao,
            lensDispDao,
            lensProductExpDao,
            lensGroupDao,
            lensSpecialtyDao,
            filterLensTypeDao,
            lensSupplierDao,
            lensMaterialDao,
            lensTechDao,
            lensFamilyDao,
            lensDescriptionDao,
            saleRepository,
        )
    }

    @Provides
    @Singleton
    fun provideAddressLookupRepository(
        addressLookupDao: AddressLookupDao,
    ): AddressLookupRepository {
        return AddressLookupRepositoryImpl(addressLookupDao)
    }

    @Singleton
    @Provides
    fun provideProductsTableStateRepository(productsTableStateDao: ProductsTableStateDao): ProductsTableStateRepository {
        return ProductsTableStateRepositoryImpl(productsTableStateDao)
    }

    @Singleton
    @Provides
    fun provideStoreLensesRepository(
        storeLensesDao: StoreLensesDao,
    ): StoreLensesRepository {
        return StoreLensesRepositoryImpl(
            storeLensesDao = storeLensesDao,
        )
    }

    @Singleton
    @Provides
    fun provideLocalLensesRepository(
        localLensFamilyDao: LocalLensFamilyDao,
        localLensDescriptionDao: LocalLensDescriptionDao,
        localLensSupplierDao: LocalLensSupplierDao,
        localLensGroupDao: LocalLensGroupDao,
        localLensSpecialtyDao: LocalLensSpecialtyDao,
        localLensTechDao: LocalLensTechDao,
        localLensTypeDao: LocalLensTypeDao,
        localLensCategoryDao: LocalLensCategoryDao,
        localLensMaterialDao: LocalLensMaterialDao,
        localLensMaterialCategoryDao: LocalLensMaterialCategoryDao,
    ): LocalLensesRepository {
        return LocalLensesRepositoryImpl(
            localLensFamilyDao = localLensFamilyDao,
            localLensDescriptionDao = localLensDescriptionDao,
            localLensSupplierDao = localLensSupplierDao,
            localLensGroupDao = localLensGroupDao,
            localLensSpecialtyDao = localLensSpecialtyDao,
            localLensTechDao = localLensTechDao,
            localLensTypeDao = localLensTypeDao,
            localLensCategoryDao = localLensCategoryDao,
            localLensMaterialDao = localLensMaterialDao,
            localLensMaterialCategoryDao = localLensMaterialCategoryDao,
        )
    }
}