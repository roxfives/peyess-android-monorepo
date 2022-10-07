package com.peyess.salesapp.data.repository.prescription

import com.peyess.salesapp.data.model.prescription.PrescriptionDocument

interface PrescriptionRepository {
    suspend fun add(prescription: PrescriptionDocument)
}