package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.feature.service_order.model.Prescription
import com.peyess.salesapp.features.service_order_fetcher.ServiceOrderFetchResponse
import com.peyess.salesapp.screen.edit_service_order.model.ServiceOrder

data class EditServiceOrderState(
    val serviceOrderId: String = "",
    val saleId: String = "",

    val serviceOrderFetchResponseAsync: Async<ServiceOrderFetchResponse> = Uninitialized,

    val serviceOrderResponseAsync: Async<EditServiceOrderFetchResponse> = Uninitialized,
    val serviceOrder: ServiceOrder = ServiceOrder(),

    val prescriptionResponseAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
    val prescription: Prescription = Prescription(),
): MavericksState {
    val successfullyFetchedServiceOrder = serviceOrderFetchResponseAsync is Success
            && serviceOrderFetchResponseAsync.invoke().isRight()
    val errorWhileFetchingServiceOrder = serviceOrderFetchResponseAsync is Fail ||
        (serviceOrderFetchResponseAsync is Success
                && serviceOrderFetchResponseAsync.invoke().isLeft())
}
