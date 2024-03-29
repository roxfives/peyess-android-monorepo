package com.peyess.salesapp.data.repository.local_sale.prescription

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.error.LocalPrescriptionResponseError
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import kotlinx.coroutines.flow.Flow

typealias LocalPrescriptionInsertResponse = Either<LocalPrescriptionResponseError, Unit>

typealias LocalPrescriptionUpdateResponse = Either<LocalPrescriptionResponseError, Unit>

typealias LocalPrescriptionStreamExistsResponse =
        Flow<Either<LocalPrescriptionResponseError, Boolean>>

typealias LocalPrescriptionStreamResponse = Flow<LocalPrescriptionResponse>

typealias LocalPrescriptionResponse =
        Either<LocalPrescriptionResponseError, LocalPrescriptionDocument>

interface LocalPrescriptionRepository {
    suspend fun createPrescriptionForServiceOrder(
        serviceOrderId: String,
    ): LocalPrescriptionInsertResponse

    suspend fun updatePrescription(
        prescription: LocalPrescriptionDocument,
    ): LocalPrescriptionUpdateResponse

    suspend fun updateHasAddition(
        serviceOrderId: String,
        hasAddition: Boolean,
    ): LocalPrescriptionUpdateResponse

    suspend fun updateLensTypeCategory(
        serviceOrderId: String,
        lensTypeCategoryId: String,
        lensTypeCategory: LensTypeCategoryName,
    ): LocalPrescriptionUpdateResponse

    suspend fun updatePrescriptionObservation(
        serviceOrderId: String,
        observation: String,
    ): LocalPrescriptionUpdateResponse

    fun streamPrescriptionForServiceOrderExists(
        soId: String,
    ): LocalPrescriptionStreamExistsResponse

    fun streamPrescriptionForServiceOrder(
        soId: String,
    ): LocalPrescriptionStreamResponse

    suspend fun getPrescriptionForServiceOrder(soId: String): LocalPrescriptionResponse
}
