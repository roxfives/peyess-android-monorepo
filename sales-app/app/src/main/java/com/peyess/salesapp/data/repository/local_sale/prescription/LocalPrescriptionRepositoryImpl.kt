package com.peyess.salesapp.data.repository.local_sale.prescription

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.data.adapter.local_sale.prescription.toLocalPrescriptionDocument
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.repository.local_sale.prescription.error.PrescriptionDataNotFound
import com.peyess.salesapp.data.repository.local_sale.prescription.error.PrescriptionPictureNotFound
import javax.inject.Inject

class LocalPrescriptionRepositoryImpl @Inject constructor(
    private val prescriptionDataDao: PrescriptionDataDao,
    private val prescriptionPictureDao: PrescriptionPictureDao,
) : LocalPrescriptionRepository {

    private suspend fun prescriptionDataForServiceOrder(
        soId: String,
    ): Either<PrescriptionDataNotFound, PrescriptionDataEntity?> = Either.catch {
        prescriptionDataDao.getPrescriptionForServiceOrder(soId)
    }.mapLeft {
        PrescriptionDataNotFound(
            message = "Prescription data not found for service order $soId",
            error = it,
        )
    }

    private suspend fun prescriptionPictureForServiceOrder(
        soId: String,
    ): Either<PrescriptionPictureNotFound, PrescriptionPictureEntity?> = Either.catch {
        prescriptionPictureDao.getPrescriptionForServiceOrder(soId)
    }.mapLeft {
        PrescriptionPictureNotFound(
            message = "Prescription picture not found for service order $soId",
            error = it,
        )
    }

    override suspend fun getPrescriptionForServiceOrder(soId: String): LocalPrescriptionResponse =
        either {
            val prescriptionData = prescriptionDataForServiceOrder(soId).bind()
            val prescriptionPicture = prescriptionPictureForServiceOrder(soId).bind()

            ensureNotNull(prescriptionData) {
                PrescriptionDataNotFound(
                    message = "Prescription data not found for service order $soId: null response",
                )
            }

            ensureNotNull(prescriptionPicture) {
                PrescriptionPictureNotFound(
                    message = "Prescription picture not found for service order $soId: null response",
                )
            }

            toLocalPrescriptionDocument(
                prescriptionDataEntity = prescriptionData,
                prescriptionPictureEntity = prescriptionPicture,
            )
        }
}
