package com.peyess.salesapp.data.repository.edit_service_order.positioning

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.positioning.toEditPositioningEntity
import com.peyess.salesapp.data.adapter.edit_service_order.positioning.toLocalPositioningDocument
import com.peyess.salesapp.data.dao.edit_service_order.positioning.EditPositioningDao
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.InsertPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.ReadPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.UpdatePositioningError
import com.peyess.salesapp.typing.general.Eye
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditPositioningRepositoryImpl @Inject constructor(
    private val positioningDao: EditPositioningDao,
): EditPositioningRepository {
    override suspend fun addPositioning(
        positioning: LocalPositioningDocument,
    ): EditPositioningInsertResponse = Either.catch {
        positioningDao.addPositioning(positioning.toEditPositioningEntity())
    }.mapLeft {
        InsertPositioningError.Unexpected(
            description = "Error while inserting positioning $positioning"
        )
    }

    override suspend fun positioningForServiceOrder(
        serviceOrderId: String, eye: Eye
    ): EditPositioningFetchResponse = Either.catch {
        positioningDao
            .positioningForServiceOrder(serviceOrderId, eye)
            ?.toLocalPositioningDocument()
    }.mapLeft {
        ReadPositioningError.Unexpected(
            description = "Error while fetching positioning for service order $serviceOrderId and eye $eye",
            throwable = it,
        )
    }.leftIfNull {
        ReadPositioningError.PositioningNotFound(
            description = "No positioning found for service order $serviceOrderId and eye $eye",
        )
    }

    override fun streamPositioningForServiceOrder(
        serviceOrderId: String, eye: Eye
    ): EditPositioningStreamResponse {
        return positioningDao.streamPositioningForServiceOrder(serviceOrderId, eye)
            .map {
                if (it == null) {
                    ReadPositioningError.PositioningNotFound(
                        description = "No positioning found for service order $serviceOrderId and eye $eye",
                        throwable = it,
                    ).left()
                } else {
                    it.toLocalPositioningDocument().right()
                }
            }
    }

    override suspend fun updatePicture(
        serviceOrderId: String,
        eye: Eye,
        picture: Uri,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updatePicture(serviceOrderId, eye, picture)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update picture for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $picture"
        )
    }

    override suspend fun updateRotation(
        serviceOrderId: String,
        eye: Eye,
        rotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateRotation(serviceOrderId, eye, rotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update rotation for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $rotation"
        )
    }

    override suspend fun updateDevice(
        serviceOrderId: String,
        eye: Eye,
        device: String,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateDevice(serviceOrderId, eye, device)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update device for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $device"
        )
    }

    override suspend fun updateBaseLeft(
        serviceOrderId: String,
        eye: Eye,
        baseLeft: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBaseLeft(serviceOrderId, eye, baseLeft)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update baseLeft for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $baseLeft"
        )
    }

    override suspend fun updateBaseLeftRotation(
        serviceOrderId: String,
        eye: Eye,
        baseLeftRotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBaseLeftRotation(serviceOrderId, eye, baseLeftRotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update baseLeftRotation for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $baseLeftRotation"
        )
    }

    override suspend fun updateBaseRight(
        serviceOrderId: String,
        eye: Eye,
        baseRight: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBaseRight(serviceOrderId, eye, baseRight)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update baseRight for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $baseRight"
        )
    }

    override suspend fun updateBaseRightRotation(
        serviceOrderId: String,
        eye: Eye,
        baseRightRotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBaseRightRotation(serviceOrderId, eye, baseRightRotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update baseRightRotation for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $baseRightRotation"
        )
    }

    override suspend fun updateBaseTop(
        serviceOrderId: String,
        eye: Eye,
        baseTop: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBaseTop(serviceOrderId, eye, baseTop)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update baseTop for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $baseTop"
        )
    }

    override suspend fun updateBaseBottom(
        serviceOrderId: String,
        eye: Eye,
        baseBottom: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBaseBottom(serviceOrderId, eye, baseBottom)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update baseBottom for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $baseBottom"
        )
    }

    override suspend fun updateTopPointLength(
        serviceOrderId: String,
        eye: Eye,
        topPointLength: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateTopPointLength(serviceOrderId, eye, topPointLength)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update topPointLength for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $topPointLength"
        )
    }

    override suspend fun updateTopPointRotation(
        serviceOrderId: String,
        eye: Eye,
        topPointRotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateTopPointRotation(serviceOrderId, eye, topPointRotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update topPointRotation for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $topPointRotation"
        )
    }

    override suspend fun updateBottomPointLength(
        serviceOrderId: String,
        eye: Eye,
        bottomPointLength: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBottomPointLength(serviceOrderId, eye, bottomPointLength)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update bottomPointLength for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $bottomPointLength"
        )
    }

    override suspend fun updateBottomPointRotation(
        serviceOrderId: String,
        eye: Eye,
        bottomPointRotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBottomPointRotation(serviceOrderId, eye, bottomPointRotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update bottomPointRotation for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $bottomPointRotation"
        )
    }

    override suspend fun updateBridgePivot(
        serviceOrderId: String,
        eye: Eye,
        bridgePivot: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateBridgePivot(serviceOrderId, eye, bridgePivot)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update bridgePivot for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $bridgePivot"
        )
    }

    override suspend fun updateCheckBottom(
        serviceOrderId: String,
        eye: Eye,
        checkBottom: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckBottom(serviceOrderId, eye, checkBottom)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkBottom for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $checkBottom"
        )
    }

    override suspend fun updateCheckTop(
        serviceOrderId: String,
        eye: Eye,
        checkTop: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckTop(serviceOrderId, eye, checkTop)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkTop for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $checkTop"
        )
    }

    override suspend fun updateCheckLeft(
        serviceOrderId: String,
        eye: Eye,
        checkLeft: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckLeft(serviceOrderId, eye, checkLeft)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkLeft for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $checkLeft"
        )
    }

    override suspend fun updateCheckLeftRotation(
        serviceOrderId: String,
        eye: Eye,
        checkLeftRotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckLeftRotation(serviceOrderId, eye, checkLeftRotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkLeftRotation for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $checkLeftRotation"
        )
    }

    override suspend fun updateCheckMiddle(
        serviceOrderId: String,
        eye: Eye,
        checkMiddle: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckMiddle(serviceOrderId, eye, checkMiddle)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkMiddle for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $checkMiddle"
        )
    }

    override suspend fun updateCheckRight(
        serviceOrderId: String,
        eye: Eye,
        checkRight: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckRight(serviceOrderId, eye, checkRight)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkRight for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $checkRight"
        )
    }

    override suspend fun updateCheckRightRotation(
        serviceOrderId: String,
        eye: Eye,
        checkRightRotation: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateCheckRightRotation(serviceOrderId, eye, checkRightRotation)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update checkRightRotation for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $checkRightRotation"
        )
    }

    override suspend fun updateFramesBottom(
        serviceOrderId: String,
        eye: Eye,
        framesBottom: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateFramesBottom(serviceOrderId, eye, framesBottom)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update framesBottom for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $framesBottom"
        )
    }

    override suspend fun updateFramesLeft(
        serviceOrderId: String,
        eye: Eye,
        framesLeft: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateFramesLeft(serviceOrderId, eye, framesLeft)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update framesLeft for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $framesLeft"
        )
    }

    override suspend fun updateFramesRight(
        serviceOrderId: String,
        eye: Eye,
        framesRight: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateFramesRight(serviceOrderId, eye, framesRight)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update framesRight for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $framesRight"
        )
    }

    override suspend fun updateFramesTop(
        serviceOrderId: String,
        eye: Eye,
        framesTop: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateFramesTop(serviceOrderId, eye, framesTop)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update framesTop for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $framesTop"
        )
    }

    override suspend fun updateOpticCenterRadius(
        serviceOrderId: String,
        eye: Eye,
        centerRadius: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateOpticCenterRadius(serviceOrderId, eye, centerRadius)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update centerRadius for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $centerRadius"
        )
    }

    override suspend fun updateOpticCenterX(
        serviceOrderId: String,
        eye: Eye,
        centerX: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateOpticCenterX(serviceOrderId, eye, centerX)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update centerX for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $centerX"
        )
    }

    override suspend fun updateOpticCenterY(
        serviceOrderId: String,
        eye: Eye,
        centerY: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateOpticCenterY(serviceOrderId, eye, centerY)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update centerY for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $centerY"
        )
    }

    override suspend fun updateHeight(
        serviceOrderId: String,
        eye: Eye,
        height: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateHeight(serviceOrderId, eye, height)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update height for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $height"
        )
    }

    override suspend fun updateWidth(
        serviceOrderId: String,
        eye: Eye,
        width: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateWidth(serviceOrderId, eye, width)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update width for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $width"
        )
    }

    override suspend fun updateProportionToPictureHorizontal(
        serviceOrderId: String,
        eye: Eye,
        proportionToPictureHorizontal: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateProportionToPictureHorizontal(serviceOrderId, eye, proportionToPictureHorizontal)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update proportionToPictureHorizontal for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $proportionToPictureHorizontal"
        )
    }

    override suspend fun updateProportionToPictureVertical(
        serviceOrderId: String,
        eye: Eye,
        proportionToPictureVertical: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateProportionToPictureVertical(serviceOrderId, eye, proportionToPictureVertical)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update proportionToPictureVertical for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $proportionToPictureVertical"
        )
    }

    override suspend fun updateEulerAngleX(
        serviceOrderId: String,
        eye: Eye,
        eulerAngleX: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateEulerAngleX(serviceOrderId, eye, eulerAngleX)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update eulerAngleX for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $eulerAngleX"
        )
    }

    override suspend fun updateEulerAngleY(
        serviceOrderId: String,
        eye: Eye,
        eulerAngleY: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateEulerAngleY(serviceOrderId, eye, eulerAngleY)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update eulerAngleY for positioning with service order" +
                    "$serviceOrderId and eye $eye using value $eulerAngleY"
        )
    }

    override suspend fun updateEulerAngleZ(
        serviceOrderId: String,
        eye: Eye,
        eulerAngleZ: Double,
    ): EditPositioningUpdateResponse = Either.catch {
        positioningDao.updateEulerAngleZ(serviceOrderId, eye, eulerAngleZ)
    }.mapLeft {
        UpdatePositioningError.Unexpected(
            description = "Failed to update eulerAngleZ for positioning with service order " +
                    "$serviceOrderId and eye $eye using value $eulerAngleZ"
        )
    }
}
