package com.peyess.salesapp.data.dao.prescription

import arrow.core.Either
import com.peyess.salesapp.data.dao.prescription.error.ReadPrescriptionDaoError
import com.peyess.salesapp.data.model.prescription.FSPrescription

typealias ReadPrescriptionDaoResponse = Either<ReadPrescriptionDaoError, FSPrescription>

interface PrescriptionDao {
    suspend fun add(document: FSPrescription)

    suspend fun prescriptionById(prescriptionId: String): ReadPrescriptionDaoResponse
}
