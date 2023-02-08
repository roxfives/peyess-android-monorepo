package com.peyess.salesapp.data.repository.edit_service_order.positioning

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.InsertPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.ReadPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.UpdatePositioningError
import com.peyess.salesapp.typing.general.Eye
import kotlinx.coroutines.flow.Flow

typealias EditPositioningInsertResponse = Either<InsertPositioningError, Unit>

typealias EditPositioningFetchResponse = Either<ReadPositioningError, LocalPositioningDocument>
typealias EditPositioningStreamResponse = Flow<EditPositioningFetchResponse>

typealias EditPositioningUpdateResponse = Either<UpdatePositioningError, Unit>

interface EditPositioningRepository {
    suspend fun addPositioning(positioning: LocalPositioningDocument): EditPositioningInsertResponse

    suspend fun positioningForServiceOrder(
        serviceOrderId: String,
        eye: Eye,
    ): EditPositioningFetchResponse
    fun streamPositioningForServiceOrder(
        serviceOrderId: String,
        eye: Eye,
    ): EditPositioningStreamResponse

    suspend fun updatePicture(
        serviceOrderId: String,
        eye: Eye,
        picture: Uri,
    ): EditPositioningUpdateResponse
    suspend fun updateRotation(
        serviceOrderId: String,
        eye: Eye,
        rotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateDevice(
        serviceOrderId: String,
        eye: Eye,
        device: String,
    ): EditPositioningUpdateResponse
    suspend fun updateBaseLeft(
        serviceOrderId: String,
        eye: Eye,
        baseLeft: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBaseLeftRotation(
        serviceOrderId: String,
        eye: Eye,
        baseLeftRotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBaseRight(
        serviceOrderId: String,
        eye: Eye,
        baseRight: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBaseRightRotation(
        serviceOrderId: String,
        eye: Eye,
        baseRightRotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBaseTop(
        serviceOrderId: String,
        eye: Eye,
        baseTop: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBaseBottom(
        serviceOrderId: String,
        eye: Eye,
        baseBottom: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateTopPointLength(
        serviceOrderId: String,
        eye: Eye,
        topPointLength: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateTopPointRotation(
        serviceOrderId: String,
        eye: Eye,
        topPointRotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBottomPointLength(
        serviceOrderId: String,
        eye: Eye,
        bottomPointLength: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBottomPointRotation(
        serviceOrderId: String,
        eye: Eye,
        bottomPointRotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateBridgePivot(
        serviceOrderId: String,
        eye: Eye,
        bridgePivot: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckBottom(
        serviceOrderId: String,
        eye: Eye,
        checkBottom: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckTop(
        serviceOrderId: String,
        eye: Eye,
        checkTop: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckLeft(
        serviceOrderId: String,
        eye: Eye,
        checkLeft: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckLeftRotation(
        serviceOrderId: String,
        eye: Eye,
        checkLeftRotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckMiddle(
        serviceOrderId: String,
        eye: Eye,
        checkMiddle: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckRight(
        serviceOrderId: String,
        eye: Eye,
        checkRight: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateCheckRightRotation(
        serviceOrderId: String,
        eye: Eye,
        checkRightRotation: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateFramesBottom(
        serviceOrderId: String,
        eye: Eye,
        framesBottom: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateFramesLeft(
        serviceOrderId: String,
        eye: Eye,
        framesLeft: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateFramesRight(
        serviceOrderId: String,
        eye: Eye,
        framesRight: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateFramesTop(
        serviceOrderId: String,
        eye: Eye,
        framesTop: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateOpticCenterRadius(
        serviceOrderId: String,
        eye: Eye,
        centerRadius: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateOpticCenterX(
        serviceOrderId: String,
        eye: Eye,
        centerX: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateOpticCenterY(
        serviceOrderId: String,
        eye: Eye,
        centerY: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateHeight(
        serviceOrderId: String,
        eye: Eye,
        height: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateWidth(
        serviceOrderId: String,
        eye: Eye,
        width: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateProportionToPictureHorizontal(
        serviceOrderId: String,
        eye: Eye,
        proportionToPictureHorizontal: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateProportionToPictureVertical(
        serviceOrderId: String,
        eye: Eye,
        proportionToPictureVertical: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateEulerAngleX(
        serviceOrderId: String,
        eye: Eye,
        eulerAngleX: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateEulerAngleY(
        serviceOrderId: String,
        eye: Eye,
        eulerAngleY: Double,
    ): EditPositioningUpdateResponse
    suspend fun updateEulerAngleZ(
        serviceOrderId: String,
        eye: Eye,
        eulerAngleZ: Double,
    ): EditPositioningUpdateResponse
}