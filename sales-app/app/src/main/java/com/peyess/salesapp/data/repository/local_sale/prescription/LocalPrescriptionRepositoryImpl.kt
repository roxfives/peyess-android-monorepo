package com.peyess.salesapp.data.repository.local_sale.prescription

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.local_sale.prescription.toLocalPrescriptionDocument
import com.peyess.salesapp.data.adapter.local_sale.prescription.toPrescriptionEntity
import com.peyess.salesapp.data.dao.local_sale.local_prescription.LocalPrescriptionDao
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.error.PrescriptionNotFound
import com.peyess.salesapp.data.repository.local_sale.prescription.error.Unexpected
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalPrescriptionRepositoryImpl @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val localPrescriptionDao: LocalPrescriptionDao,
): LocalPrescriptionRepository {
    override suspend fun createPrescriptionForServiceOrder(
        serviceOrderId: String,
    ): LocalPrescriptionInsertResponse = Either.catch {
        val exists = (localPrescriptionDao.exitsForServiceOrder(serviceOrderId) ?: 0) > 0
        if (exists) {
            return@catch
        }

        val uniqueId = firebaseManager.uniqueId()
        val prescription = PrescriptionEntity(
            id = uniqueId,
            soId = serviceOrderId,
        )

        localPrescriptionDao.insert(prescription)
    }.mapLeft {
        Unexpected(
            description = "Unexpected error while inserting prescription for service order $serviceOrderId",
            error = it,
        )
    }

    override suspend fun updatePrescription(
        prescription: LocalPrescriptionDocument
    ): LocalPrescriptionUpdateResponse = Either.catch {
        localPrescriptionDao.update(prescription.toPrescriptionEntity())
    }.mapLeft {
        Unexpected(
            description = "Unexpected error while updating prescription for service order ${prescription.soId}",
            error = it,
        )
    }

    override suspend fun updateHasAddition(
        serviceOrderId: String,
        hasAddition: Boolean
    ): LocalPrescriptionUpdateResponse = either {
        // TODO: remove the creation from this method
        val exists = (localPrescriptionDao.exitsForServiceOrder(serviceOrderId) ?: 0) > 0
        if (!exists) {
            createPrescriptionForServiceOrder(serviceOrderId).bind()
        }

        val asInt = if (hasAddition) 1 else 0
        Either.catch { localPrescriptionDao.updateHasAddition(serviceOrderId, asInt) }
            .mapLeft {
                Unexpected(
                    description = "Unexpected error while updating prescription for service order $serviceOrderId",
                    error = it,
                )
            }.bind()
    }

    override suspend fun updateLensTypeCategory(
        serviceOrderId: String,
        lensTypeCategoryId: String,
        lensTypeCategory: LensTypeCategoryName
    ): LocalPrescriptionUpdateResponse = Either.catch {
        localPrescriptionDao.updateLensTypeCategory(
            serviceOrderId = serviceOrderId,
            lensTypeCategoryId = lensTypeCategoryId,
            lensTypeCategory = lensTypeCategory.toName(),
        )
    }.mapLeft {
        Unexpected(
            description = "Unexpected error while updating prescription for service order $serviceOrderId",
            error = it,
        )
    }

    override suspend fun updatePrescriptionObservation(
        serviceOrderId: String,
        observation: String
    ): LocalPrescriptionUpdateResponse = Either.catch {
        localPrescriptionDao.updateObservation(
            serviceOrderId = serviceOrderId,
            observation = observation,
        )
    }.mapLeft {
        Unexpected(
            description = "Unexpected error while updating prescription for service order $serviceOrderId",
            error = it,
        )
    }

    override fun streamPrescriptionForServiceOrderExists(
        soId: String,
    ): LocalPrescriptionStreamExistsResponse {
        return localPrescriptionDao
            .streamExists(soId)
            .map { (it > 0).right() }
    }

    override fun streamPrescriptionForServiceOrder(
        soId: String,
    ): LocalPrescriptionStreamResponse {
        return localPrescriptionDao
            .getById(soId)
            .map {
                if (it == null) {
                    PrescriptionNotFound(
                        description = "Prescription not found for service order $soId: null response",
                    ).left()
                } else {
                    it.toLocalPrescriptionDocument().right()
                }
            }
    }

    override suspend fun getPrescriptionForServiceOrder(
        soId: String,
    ): LocalPrescriptionResponse  = Either.catch {
            localPrescriptionDao
                .getPrescriptionForServiceOrder(soId)
                ?.toLocalPrescriptionDocument()
    }.mapLeft {
        Unexpected(
            description = "Unexpected error while getting prescription for service order $soId",
            error = it,
        )
    }.leftIfNull {
        PrescriptionNotFound(
            description = "Prescription not found for service order $soId: null response",
        )
    }
}
