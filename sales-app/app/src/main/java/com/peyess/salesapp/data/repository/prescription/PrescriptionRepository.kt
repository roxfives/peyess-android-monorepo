package com.peyess.salesapp.data.repository.prescription

import arrow.core.Either
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.repository.prescription.error.ReadPrescriptionRepositoryError

typealias ReadPrescriptionRepositoryResponse =
        Either<ReadPrescriptionRepositoryError, PrescriptionDocument>

interface PrescriptionRepository {
    suspend fun add(prescription: PrescriptionDocument)

    suspend fun prescriptionById(prescriptionId: String): ReadPrescriptionRepositoryResponse
}