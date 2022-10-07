package com.peyess.salesapp.data.repository.prescription

import com.peyess.salesapp.data.adapter.prescription.toFSPrescription
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import javax.inject.Inject

class PrescriptionRepositoryImpl @Inject constructor(
    private val prescriptionDao: PrescriptionDao,
): PrescriptionRepository {
    override suspend fun add(prescription: PrescriptionDocument) {
        val fsPositioning = prescription.toFSPrescription()

        prescriptionDao.add(fsPositioning)
    }
}
