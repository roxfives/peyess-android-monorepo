package com.peyess.salesapp.data.repository.local_sale.prescription

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.error.LocalPrescriptionResponseError

typealias LocalPrescriptionResponse =
        Either<LocalPrescriptionResponseError, LocalPrescriptionDocument>

interface LocalPrescriptionRepository {
    suspend fun getPrescriptionForServiceOrder(soId: String): LocalPrescriptionResponse
}
