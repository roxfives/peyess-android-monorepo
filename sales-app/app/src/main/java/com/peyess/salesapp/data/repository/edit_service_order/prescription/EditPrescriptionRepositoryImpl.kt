package com.peyess.salesapp.data.repository.edit_service_order.prescription

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.prescription.toEditPrescriptionEntity
import com.peyess.salesapp.data.adapter.edit_service_order.prescription.toLocalPrescriptionDocument
import com.peyess.salesapp.data.dao.edit_service_order.prescription.EditPrescriptionDao
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.InsertPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.ReadPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.prescription.error.DeletePrescriptionError
import com.peyess.salesapp.data.repository.edit_service_order.prescription.error.UpdatePrescriptionError
import com.peyess.salesapp.typing.prescription.PrismPosition
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import javax.inject.Inject

class EditPrescriptionRepositoryImpl @Inject constructor(
    private val prescriptionDao: EditPrescriptionDao,
) : EditPrescriptionRepository {
    override suspend fun addPrescription(
        prescription: LocalPrescriptionDocument,
    ): EditPrescriptionInsertResponse = Either.catch {
        prescriptionDao.addPrescription(prescription.toEditPrescriptionEntity())
    }.mapLeft {
        InsertPositioningError.Unexpected(
            description = "Error while inserting the prescription $prescription",
            throwable = it,
        )
    }

    override suspend fun prescriptionById(
        id: String,
    ): EditPrescriptionFetchResponse = Either.catch {
        prescriptionDao.prescriptionById(id)?.toLocalPrescriptionDocument()
    }.mapLeft {
        ReadPositioningError.Unexpected(
            description = "Error while fetching the prescription with id $id",
            throwable = it,
        )
    }.leftIfNull {
        ReadPositioningError.PositioningNotFound(
            description = "Prescription with id $id not found",
        )
    }

    override fun streamPrescriptionById(
        id: String,
    ): EditPrescriptionStreamResponse {
        return prescriptionDao.streamPrescriptionById(id).map {
            if (it == null) {
                ReadPositioningError.PositioningNotFound(
                    description = "Prescripton with id $id not found",
                ).left()
            } else {
                it.toLocalPrescriptionDocument().right()
            }
        }
    }

    override suspend fun prescriptionByServiceOrder(
        serviceOrderId: String
    ): EditPrescriptionFetchResponse = Either.catch {
        prescriptionDao
            .prescriptionByServiceOrder(serviceOrderId)
            ?.toLocalPrescriptionDocument()
    }.mapLeft {
        ReadPositioningError.Unexpected(
            description = "Error while fetching the prescription with service order id $serviceOrderId",
            throwable = it,
        )
    }.leftIfNull {
        ReadPositioningError.PositioningNotFound(
            description = "Prescription with service order id $serviceOrderId not found",
        )
    }

    override fun streamPrescriptionByServiceOrder(
        serviceOrderId: String,
    ): EditPrescriptionStreamResponse {
        return prescriptionDao.streamPrescriptionByServiceOrder(serviceOrderId).map {
            if (it == null) {
                ReadPositioningError.PositioningNotFound(
                    description = "Prescripton with service order id $serviceOrderId not found",
                ).left()
            } else {
                it.toLocalPrescriptionDocument().right()
            }
        }
    }

    override suspend fun updatePicture(
        id: String, picture: Uri
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePicture(id, picture)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with picture = $picture",
            throwable = it,
        )
    }

    override suspend fun updateProfessionalName(
        id: String, professionalName: String
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateProfessionalName(id, professionalName)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with professionalName = $professionalName",
            throwable = it,
        )
    }

    override suspend fun updateProfessionalId(
        id: String, professionalId: String
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateProfessionalId(id, professionalId)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with professionalId = $professionalId",
            throwable = it,
        )
    }

    override suspend fun updateIsCopy(
        id: String, isCopy: Boolean
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateIsCopy(id, isCopy)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with isCopy = $isCopy",
            throwable = it,
        )
    }

    override suspend fun updatePrescriptionDate(
        id: String, prescriptionDate: ZonedDateTime
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrescriptionDate(id, prescriptionDate)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prescriptionDate = $prescriptionDate",
            throwable = it,
        )
    }

    override suspend fun updateSphericalLeft(
        id: String, sphericalLeft: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateSphericalLeft(id, sphericalLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with sphericalLeft = $sphericalLeft",
            throwable = it,
        )
    }

    override suspend fun updateSphericalRight(
        id: String, sphericalRight: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateSphericalRight(id, sphericalRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with sphericalRight = $sphericalRight",
            throwable = it,
        )
    }

    override suspend fun updateCylindricalLeft(
        id: String, cylindricalLeft: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateCylindricalLeft(id, cylindricalLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with cylindricalLeft = $cylindricalLeft",
            throwable = it,
        )
    }

    override suspend fun updateCylindricalRight(
        id: String, cylindricalRight: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateCylindricalRight(id, cylindricalRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with cylindricalRight = $cylindricalRight",
            throwable = it,
        )
    }

    override suspend fun updateAxisLeft(
        id: String, axisLeft: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateAxisLeft(id, axisLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with axisLeft = $axisLeft",
            throwable = it,
        )
    }

    override suspend fun updateAxisRight(
        id: String, axisRight: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateAxisRight(id, axisRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with axisRight = $axisRight",
            throwable = it,
        )
    }

    override suspend fun updateHasAddition(
        id: String, hasAddition: Int
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateHasAddition(id, hasAddition)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with hasAddition = $hasAddition",
            throwable = it,
        )
    }

    override suspend fun updateAdditionLeft(
        id: String, additionLeft: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateAdditionLeft(id, additionLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with additionLeft = $additionLeft",
            throwable = it,
        )
    }

    override suspend fun updateAdditionRight(
        id: String, additionRight: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateAdditionRight(id, additionRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with additionRight = $additionRight",
            throwable = it,
        )
    }

    override suspend fun updateHasPrism(
        id: String,
        hasPrism: Boolean,
    ): EditPrescriptionUpdateResponse = Either.catch {
        val asInt = if (hasPrism) 1 else 0

        prescriptionDao.updateHasPrism(id, asInt)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with hasPrism = $hasPrism",
            throwable = it,
        )
    }

    override suspend fun updatePrismDegreeLeft(
        id: String, prismDegreeLeft: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrismDegreeLeft(id, prismDegreeLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prismDegreeLeft = $prismDegreeLeft",
            throwable = it,
        )
    }

    override suspend fun updatePrismDegreeRight(
        id: String, prismDegreeRight: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrismDegreeRight(id, prismDegreeRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prismDegreeRight = $prismDegreeRight",
            throwable = it,
        )
    }

    override suspend fun updatePrismAxisLeft(
        id: String, prismAxisLeft: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrismAxisLeft(id, prismAxisLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prismAxisLeft = $prismAxisLeft",
            throwable = it,
        )
    }

    override suspend fun updatePrismAxisRight(
        id: String, prismAxisRight: Double
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrismAxisRight(id, prismAxisRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prismAxisRight = $prismAxisRight",
            throwable = it,
        )
    }

    override suspend fun updatePrismPositionLeft(
        id: String, prismPositionLeft: PrismPosition
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrismPositionLeft(id, prismPositionLeft)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prismPositionLeft = $prismPositionLeft",
            throwable = it,
        )
    }

    override suspend fun updatePrismPositionRight(
        id: String, prismPositionRight: PrismPosition
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updatePrismPositionRight(id, prismPositionRight)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with prismPositionRight = $prismPositionRight",
            throwable = it,
        )
    }

    override suspend fun updateObservation(
        id: String,
        observation: String
    ): EditPrescriptionUpdateResponse = Either.catch {
        prescriptionDao.updateObservation(id, observation)
    }.mapLeft {
        UpdatePrescriptionError.Unexpected(
            description = "Error while updating prescription $id " +
                    "with observation = $observation",
            throwable = it,
        )
    }

    override suspend fun deletePrescriptionForServiceOrder(
        serviceOrderId: String,
    ): EditPrescriptionDeleteResponse = Either.catch {
        prescriptionDao.deletePrescriptionForServiceOrder(serviceOrderId)
    }.mapLeft {
        DeletePrescriptionError.Unexpected(
            description = "Error while deleting prescription for service order $serviceOrderId",
            throwable = it,
        )
    }
}
