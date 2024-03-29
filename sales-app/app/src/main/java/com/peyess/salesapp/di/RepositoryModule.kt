package com.peyess.salesapp.di

import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.auth.store.OpticalStoreDao
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.data.dao.client.ClientDao
import com.peyess.salesapp.data.dao.local_sale.client_picked.ClientPickedDao
import com.peyess.salesapp.data.dao.payment_method.PaymentMethodDao
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.data.dao.local_sale.positioning.PositioningDao
import com.peyess.salesapp.data.dao.local_sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.data.dao.local_sale.payment.LocalPaymentDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.data.dao.service_order.ServiceOrderDao
import com.peyess.salesapp.data.dao.address_lookup.AddressLookupDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.card_flag.CardFlagDao
import com.peyess.salesapp.data.dao.client_legal.ClientLegalDao
import com.peyess.salesapp.data.dao.discount.OverallDiscountDao
import com.peyess.salesapp.data.dao.edit_service_order.client_picked.EditClientPickedDao
import com.peyess.salesapp.data.dao.edit_service_order.frames.EditFramesDataDao
import com.peyess.salesapp.data.dao.edit_service_order.lens_comparison.EditLensComparisonDao
import com.peyess.salesapp.data.dao.edit_service_order.payment.EditLocalPaymentDao
import com.peyess.salesapp.data.dao.edit_service_order.payment_discount.EditOverallDiscountDao
import com.peyess.salesapp.data.dao.edit_service_order.payment_fee.EditPaymentFeeDao
import com.peyess.salesapp.data.dao.edit_service_order.positioning.EditPositioningDao
import com.peyess.salesapp.data.dao.edit_service_order.prescription.EditPrescriptionDao
import com.peyess.salesapp.data.dao.edit_service_order.product_picked.EditProductPickedDao
import com.peyess.salesapp.data.dao.edit_service_order.sale.EditSaleDao
import com.peyess.salesapp.data.dao.edit_service_order.service_order.EditServiceOrderDao
import com.peyess.salesapp.data.dao.lenses.StoreLensesDao
import com.peyess.salesapp.data.dao.local_client.LocalClientDao
import com.peyess.salesapp.data.dao.local_sale.local_prescription.LocalPrescriptionDao
import com.peyess.salesapp.data.dao.management_picture_upload.PictureUploadDao
import com.peyess.salesapp.data.dao.measuring.MeasuringDao
import com.peyess.salesapp.data.dao.payment_fee.PaymentFeeDao
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.dao.products_table_state.ProductsTableStateDao
import com.peyess.salesapp.data.dao.purchase.PurchaseDao
import com.peyess.salesapp.data.dao.purchase.discount.DiscountGroupDao
import com.peyess.salesapp.data.repository.address_lookup.AddressLookupRepository
import com.peyess.salesapp.data.repository.address_lookup.AddressLookupRepositoryImpl
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepositoryImpl
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepositoryImpl
import com.peyess.salesapp.screen.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepositoryImpl
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.client.ClientRepositoryImpl
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepositoryImpl
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.EditLensComparisonRepository
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.EditLensComparisonRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.sale.EditSaleRepository
import com.peyess.salesapp.data.repository.edit_service_order.sale.EditSaleRepositoryImpl
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepositoryImpl
import com.peyess.salesapp.data.repository.lenses.StoreLensesRepository
import com.peyess.salesapp.data.repository.lenses.StoreLensesRepositoryImpl
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepositoryImpl
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.client_picked.ClientPickedRepository
import com.peyess.salesapp.data.repository.local_sale.client_picked.ClientPickedRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.lens_comparison.LensComparisonRepository
import com.peyess.salesapp.data.repository.local_sale.lens_comparison.LensComparisonRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.measuring.LocalMeasuringRepository
import com.peyess.salesapp.data.repository.local_sale.measuring.LocalMeasuringRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentRepository
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningRepository
import com.peyess.salesapp.data.repository.local_sale.positioning.error.LocalPositioningRepositoryImpl
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepositoryImpl
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepository
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepositoryImpl
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
        salesApplication: SalesApplication,
        firebaseManager: FirebaseManager,
        serviceOrderDao: ServiceOrderDao,
    ): ServiceOrderRepository {
        return ServiceOrderRepositoryImpl(
            salesApplication = salesApplication,
            firebaseManager = firebaseManager,
            serviceOrderDao = serviceOrderDao,
        )
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
        salesApplication: SalesApplication,
        firebaseManager: FirebaseManager,
        positioningDao: com.peyess.salesapp.data.dao.positioning.PositioningDao,
    ): PositioningRepository {
        return PositioningRepositoryImpl(
            salesApplication = salesApplication,
            firebaseManager = firebaseManager,
            positioningDao = positioningDao,
        )
    }

    @Provides
    @Singleton
    fun providePrescriptionRepository(
        salesApplication: SalesApplication,
        firebaseManager: FirebaseManager,
        prescriptionDao: PrescriptionDao,
    ): PrescriptionRepository {
        return PrescriptionRepositoryImpl(
            salesApplication = salesApplication,
            firebaseManager = firebaseManager,
            prescriptionDao = prescriptionDao,
        )
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
        salesApplication: SalesApplication,
        paymentMethodDao: PaymentMethodDao,
    ): PaymentMethodRepository {
        return PaymentMethodRepositoryImpl(
            salesApplication = salesApplication,
            paymentMethodDao = paymentMethodDao,
        )
    }

    @Provides
    @Singleton
    fun providePurchaseRepository(
        salesApplication: SalesApplication,
        firebaseManager: FirebaseManager,
        purchaseDao: PurchaseDao
    ): PurchaseRepository {
        return PurchaseRepositoryImpl(
            salesApplication = salesApplication,
            firebaseManager = firebaseManager,
            purchaseDao = purchaseDao,
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
        prescriptionPictureDao: LocalPrescriptionDao,
        framesDataDao: FramesDataDao,
        positioningDao: PositioningDao,
        productPickedDao: ProductPickedDao,
        clientPickedDao: ClientPickedDao,
        localPaymentDao: LocalPaymentDao,
    ): SaleRepository {
        return SaleRepositoryImpl(
            application,
            firebaseManager,
            authenticationRepository,
            activeSalesDao,
            activeSODao,
            lensTypeCategoryDao,
            prescriptionPictureDao,
            framesDataDao,
            positioningDao,
            productPickedDao,
            clientPickedDao,
            localPaymentDao,
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
        localLensDao: com.peyess.salesapp.data.dao.lenses.room.LocalLensDao,
    ): LocalLensesRepository {
        return LocalLensesRepositoryImpl(
            localLensDao = localLensDao,
        )
    }

    @Singleton
    @Provides
    fun provideLocalPrescriptionRepository(
        firebaseManager: FirebaseManager,
        prescriptionPictureDao: LocalPrescriptionDao,
    ): LocalPrescriptionRepository {
        return LocalPrescriptionRepositoryImpl(
            firebaseManager = firebaseManager,
            localPrescriptionDao = prescriptionPictureDao,
        )
    }

    @Singleton
    @Provides
    fun provideLocalFramesRepository(
        framesDataDao: FramesDataDao,
    ): LocalFramesRepository {
        return LocalFramesRepositoryImpl(
            framesDataDao = framesDataDao,
        )
    }

    @Singleton
    @Provides
    fun provideLocalMeasuringRepository(
        positioningDao: PositioningDao,
    ): LocalMeasuringRepository {
        return LocalMeasuringRepositoryImpl(
            positioningDao = positioningDao,
        )
    }

    @Singleton
    @Provides
    fun provideLensComparisonRepository(
        lensComparisonDao: LensComparisonDao,
    ): LensComparisonRepository {
        return LensComparisonRepositoryImpl(lensComparisonDao)
    }

    @Singleton
    @Provides
    fun provideLocalPaymentRepository(
        localPaymentDao: LocalPaymentDao,
    ): LocalPaymentRepository {
        return LocalPaymentRepositoryImpl(localPaymentDao)
    }

    @Singleton
    @Provides
    fun provideClientPickedRepository(
        clientPickedDao: ClientPickedDao,
    ): ClientPickedRepository {
        return ClientPickedRepositoryImpl(
            clientPickedDao = clientPickedDao,
        )
    }

    @Singleton
    @Provides
    fun providePictureUploadRepository(
        uploadPictureDao: PictureUploadDao,
    ): PictureUploadRepository {
        return PictureUploadRepositoryImpl(
            uploadPictureDao = uploadPictureDao,
        )
    }

    @Singleton
    @Provides
    fun provideLocalPositioningRepository(
        positioningDao: PositioningDao,
    ): LocalPositioningRepository {
        return LocalPositioningRepositoryImpl(
            positioningDao = positioningDao,
        )
    }

    @Singleton
    @Provides
    fun provideLocalClientRepository(
        localClientDao: LocalClientDao,
    ): LocalClientRepository {
        return LocalClientRepositoryImpl(
            localClientDao = localClientDao,
        )
    }

    @Singleton
    @Provides
    fun provideCacheCreateClientRepository(
        firebaseManager: FirebaseManager,
        cacheCreateClientDao: CacheCreateClientDao,
    ): CacheCreateClientRepository {
        return CacheCreateClientRepositoryImpl(
            firebaseManager = firebaseManager,
            cacheCreateClientDao = cacheCreateClientDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditSaleRepository(
        editSaleDao: EditSaleDao,
    ): EditSaleRepository {
        return EditSaleRepositoryImpl(
            saleDao = editSaleDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditServiceOrderRepository(
        editServiceOrderDao: EditServiceOrderDao,
    ): EditServiceOrderRepository {
        return EditServiceOrderRepositoryImpl(
            serviceOrderDao = editServiceOrderDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditFramesDataRepository(
        editFramesDataDao: EditFramesDataDao,
    ): EditFramesDataRepository {
        return EditFramesDataRepositoryImpl(
            editFramesDataDao = editFramesDataDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditLensComparisonRepository(
        editLensComparisonDao: EditLensComparisonDao,
    ): EditLensComparisonRepository {
        return EditLensComparisonRepositoryImpl(
            lensComparisonDao = editLensComparisonDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditLocalPaymentRepository(
        editLocalPaymentDao: EditLocalPaymentDao,
        localClientDao: LocalClientDao,
    ): EditLocalPaymentRepository {
        return EditLocalPaymentRepositoryImpl(
            editLocalPaymentDao = editLocalPaymentDao,
            localClientDao = localClientDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditPaymentFeeRepository(
        editPaymentFeeDao: EditPaymentFeeDao,
    ): EditPaymentFeeRepository {
        return EditPaymentFeeRepositoryImpl(
            paymentFeeDao = editPaymentFeeDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditOverallDiscountRepository(
        editOverallDiscountDao: EditOverallDiscountDao,
    ): EditPaymentDiscountRepository {
        return EditPaymentDiscountRepositoryImpl(
            paymentDiscountDao = editOverallDiscountDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditPositioningRepository(
        editPositioningDao: EditPositioningDao,
    ): EditPositioningRepository {
        return EditPositioningRepositoryImpl(
            positioningDao = editPositioningDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditPrescriptionRepository(
        editPrescriptionDao: EditPrescriptionDao,
    ): EditPrescriptionRepository {
        return EditPrescriptionRepositoryImpl(
            prescriptionDao = editPrescriptionDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditProductPickedRepository(
        editProductPickedDao: EditProductPickedDao,
    ): EditProductPickedRepository {
        return EditProductPickedRepositoryImpl(
            productPickedDao = editProductPickedDao,
        )
    }

    @Singleton
    @Provides
    fun provideEditClientPickedRepository(
        editClientPickedDao: EditClientPickedDao,
    ): EditClientPickedRepository {
        return EditClientPickedRepositoryImpl(
            clientPickedDao = editClientPickedDao,
        )
    }
}
