package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedDocument
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientReadSingleResponse
import com.peyess.salesapp.feature.service_order.model.Client
import com.peyess.salesapp.feature.service_order.model.Coloring
import com.peyess.salesapp.feature.service_order.model.Frames
import com.peyess.salesapp.feature.service_order.model.Lens
import com.peyess.salesapp.feature.service_order.model.Payment
import com.peyess.salesapp.feature.service_order.model.Prescription
import com.peyess.salesapp.feature.service_order.model.Treatment
import com.peyess.salesapp.features.service_order_fetcher.ServiceOrderFetchResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.screen.edit_service_order.model.ServiceOrder

data class EditServiceOrderState(
    val serviceOrderId: String = "",
    val saleId: String = "",

    val serviceOrderFetchResponseAsync: Async<ServiceOrderFetchResponse> = Uninitialized,

    val serviceOrderResponseAsync: Async<EditServiceOrderFetchResponse> = Uninitialized,
    val serviceOrder: ServiceOrder = ServiceOrder(),

    val userPickedResponseAsync: Async<EditClientPickedFetchResponse> = Uninitialized,
    val userPicked: Client = Client(),

    val responsiblePickedResponseAsync: Async<EditClientPickedFetchResponse> = Uninitialized,
    val responsiblePicked: Client = Client(),

    val witnessPickedResponseAsync: Async<EditClientPickedFetchResponse> = Uninitialized,
    val witnessPicked: Client = Client(),
    val hasWitness: Boolean = false,

    val prescriptionResponseAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
    val prescription: Prescription = Prescription(),

    val productPickedResponseAsync: Async<EditProductPickedFetchResponse> = Uninitialized,
    val productPicked: ProductPickedDocument = ProductPickedDocument(),

    val lensResponseAsync: Async<SingleLensResponse> = Uninitialized,
    val lens: Lens = Lens(),

    val coloringResponseAsync: Async<SingleColoringResponse> = Uninitialized,
    val coloring: Coloring = Coloring(),

    val treatmentResponseAsync: Async<SingleTreatmentResponse> = Uninitialized,
    val treatment: Treatment = Treatment(),

    val framesResponseAsync: Async<EditFramesFetchResponse> = Uninitialized,
    val frames: Frames = Frames(),

    val paymentsResponseAsync: Async<EditLocalPaymentFetchResponse> = Uninitialized,
    val payments: List<Payment> = emptyList(),
): MavericksState {
    val successfullyFetchedServiceOrder = serviceOrderFetchResponseAsync is Success
            && serviceOrderFetchResponseAsync.invoke().isRight()
    val errorWhileFetchingServiceOrder = serviceOrderFetchResponseAsync is Fail ||
        (serviceOrderFetchResponseAsync is Success
                && serviceOrderFetchResponseAsync.invoke().isLeft())
}
