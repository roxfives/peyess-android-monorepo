package com.peyess.salesapp.data.repository.prescription

import arrow.core.continuations.either
import com.peyess.salesapp.data.adapter.prescription.toFSPrescription
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.dao.prescription.error.ReadPrescriptionDaoError
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.repository.prescription.adapter.toPrescriptionDocument
import com.peyess.salesapp.data.repository.prescription.error.ReadPrescriptionRepositoryError
import javax.inject.Inject

class PrescriptionRepositoryImpl @Inject constructor(
    private val prescriptionDao: PrescriptionDao,
): PrescriptionRepository {
    override suspend fun add(prescription: PrescriptionDocument) {
        val fsPositioning = prescription.toFSPrescription()

        prescriptionDao.add(fsPositioning)
    }

    override suspend fun prescriptionById(
        prescriptionId: String,
    ): ReadPrescriptionRepositoryResponse = either {
        val response = prescriptionDao
            .prescriptionById(prescriptionId)
            .mapLeft {
                when(it) {
                    is ReadPrescriptionDaoError.NotFound ->
                        ReadPrescriptionRepositoryError.NotFound(
                            description = it.description,
                            throwable = it.throwable
                        )
                    is ReadPrescriptionDaoError.Unexpected ->
                        ReadPrescriptionRepositoryError.Unexpected(
                            description = it.description,
                            throwable = it.throwable
                        )
                }
            }.bind()

        response.toPrescriptionDocument()
    }
}
