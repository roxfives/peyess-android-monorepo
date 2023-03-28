package com.peyess.salesapp.data.dao.prescription

import arrow.core.Either
import com.peyess.salesapp.data.dao.prescription.error.ReadPrescriptionDaoError
import com.peyess.salesapp.data.dao.prescription.error.UpdatePrescriptionDaoError
import com.peyess.salesapp.data.model.prescription.FSPrescription
import com.peyess.salesapp.data.model.prescription.FSPrescriptionUpdate

typealias ReadPrescriptionDaoResponse = Either<ReadPrescriptionDaoError, FSPrescription>

typealias UpdatePrescriptionDaoResponse = Either<UpdatePrescriptionDaoError, Unit>

interface PrescriptionDao {
    suspend fun add(document: FSPrescription)

    suspend fun prescriptionById(prescriptionId: String): ReadPrescriptionDaoResponse

    suspend fun updatePrescription(
        prescriptionId: String,
        prescriptionUpdate: FSPrescriptionUpdate,
    ): UpdatePrescriptionDaoResponse
}
