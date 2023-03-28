package com.peyess.salesapp.data.repository.prescription

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import com.peyess.salesapp.data.repository.prescription.error.GetPictureUrlPrescriptionRepositoryError
import com.peyess.salesapp.data.repository.prescription.error.ReadPrescriptionRepositoryError
import com.peyess.salesapp.data.repository.prescription.error.UpdatePrescriptionRepositoryError

typealias ReadPrescriptionRepositoryResponse =
        Either<ReadPrescriptionRepositoryError, PrescriptionDocument>

typealias UpdatePrescriptionResponse = Either<UpdatePrescriptionRepositoryError, Unit>

typealias GetPictureUrlPrescriptionRepositoryResponse =
        Either<GetPictureUrlPrescriptionRepositoryError, Uri>

interface PrescriptionRepository {
    suspend fun add(prescription: PrescriptionDocument)

    suspend fun prescriptionById(prescriptionId: String): ReadPrescriptionRepositoryResponse

    suspend fun updatePrescription(
        prescriptionId: String,
        prescription: PrescriptionUpdateDocument,
    ): UpdatePrescriptionResponse

    suspend fun getPictureUri(
        prescription: PrescriptionDocument,
    ): GetPictureUrlPrescriptionRepositoryResponse
}