package com.peyess.salesapp.data.repository.edit_service_order.prescription

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.InsertPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.ReadPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.prescription.error.DeletePrescriptionError
import com.peyess.salesapp.data.repository.edit_service_order.prescription.error.UpdatePrescriptionError
import com.peyess.salesapp.typing.prescription.PrismPosition
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

typealias EditPrescriptionInsertResponse = Either<InsertPositioningError, Unit>

typealias EditPrescriptionFetchResponse = Either<ReadPositioningError, LocalPrescriptionDocument>
typealias EditPrescriptionStreamResponse = Flow<EditPrescriptionFetchResponse>

typealias EditPrescriptionUpdateResponse = Either<UpdatePrescriptionError, Unit>

typealias EditPrescriptionDeleteResponse = Either<DeletePrescriptionError, Unit>

interface EditPrescriptionRepository {
    suspend fun addPrescription(
        prescription: LocalPrescriptionDocument,
    ): EditPrescriptionInsertResponse

    suspend fun prescriptionById(id: String): EditPrescriptionFetchResponse
    fun streamPrescriptionById(id: String): EditPrescriptionStreamResponse
    suspend fun prescriptionByServiceOrder(serviceOrderId: String): EditPrescriptionFetchResponse
    fun streamPrescriptionByServiceOrder(serviceOrderId: String): EditPrescriptionStreamResponse

    suspend fun updatePicture(
        id: String,
        picture: Uri,
    ): EditPrescriptionUpdateResponse
    suspend fun updateProfessionalName(
        id: String,
        professionalName: String,
    ): EditPrescriptionUpdateResponse
    suspend fun updateProfessionalId(
        id: String,
        professionalId: String,
    ): EditPrescriptionUpdateResponse
    suspend fun updateIsCopy(
        id: String,
        isCopy: Boolean,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrescriptionDate(
        id: String,
        prescriptionDate: ZonedDateTime,
    ): EditPrescriptionUpdateResponse
    suspend fun updateSphericalLeft(
        id: String,
        sphericalLeft: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateSphericalRight(
        id: String,
        sphericalRight: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateCylindricalLeft(
        id: String,
        cylindricalLeft: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateCylindricalRight(
        id: String,
        cylindricalRight: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateAxisLeft(
        id: String,
        axisLeft: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateAxisRight(
        id: String,
        axisRight: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateHasAddition(
        id: String,
        hasAddition: Int,
    ): EditPrescriptionUpdateResponse
    suspend fun updateAdditionLeft(
        id: String,
        additionLeft: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateAdditionRight(
        id: String,
        additionRight: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updateHasPrism(
        id: String,
        hasPrism: Int,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrismDegreeLeft(
        id: String,
        prismDegreeLeft: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrismDegreeRight(
        id: String,
        prismDegreeRight: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrismAxisLeft(
        id: String,
        prismAxisLeft: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrismAxisRight(
        id: String,
        prismAxisRight: Double,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrismPositionLeft(
        id: String,
        prismPositionLeft: PrismPosition,
    ): EditPrescriptionUpdateResponse
    suspend fun updatePrismPositionRight(
        id: String,
        prismPositionRight: PrismPosition,
    ): EditPrescriptionUpdateResponse

    suspend fun deletePrescriptionForServiceOrder(
        serviceOrderId: String,
    ): EditPrescriptionDeleteResponse
}
