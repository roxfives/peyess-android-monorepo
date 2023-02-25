package com.peyess.salesapp.di

import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.features.edit_service_order.updater.ServiceOrderUpdater
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    fun provideServiceOrderUpdater(
        authenticationRepository: AuthenticationRepository,
        purchaseRepository: PurchaseRepository,
        serviceOrderRepository: ServiceOrderRepository,
        positioningRepository: PositioningRepository,
        prescriptionRepository: PrescriptionRepository,
        measuringRepository: MeasuringRepository,
        localClientRepository: LocalClientRepository,
        localLensesRepository: LocalLensesRepository,
        editFramesRepository: EditFramesDataRepository,
        editPrescriptionRepository: EditPrescriptionRepository,
        editPositioningRepository: EditPositioningRepository,
        editClientPickedRepository: EditClientPickedRepository,
        editProductPickedRepository: EditProductPickedRepository,
        editPaymentRepository: EditLocalPaymentRepository,
        discountRepository: EditPaymentDiscountRepository,
        paymentFeeRepository: EditPaymentFeeRepository,
        editServiceOrderRepository: EditServiceOrderRepository,
    ): ServiceOrderUpdater {
        return ServiceOrderUpdater(
            authenticationRepository = authenticationRepository,
            purchaseRepository = purchaseRepository,
            serviceOrderRepository = serviceOrderRepository,
            positioningRepository = positioningRepository,
            prescriptionRepository = prescriptionRepository,
            measuringRepository = measuringRepository,
            localClientRepository = localClientRepository,
            localLensesRepository = localLensesRepository,
            editFramesRepository = editFramesRepository,
            editPrescriptionRepository = editPrescriptionRepository,
            editPositioningRepository = editPositioningRepository,
            editClientPickedRepository = editClientPickedRepository,
            editProductPickedRepository = editProductPickedRepository,
            editPaymentRepository = editPaymentRepository,
            discountRepository = discountRepository,
            paymentFeeRepository = paymentFeeRepository,
            editServiceOrderRepository = editServiceOrderRepository,
        )
    }
}
