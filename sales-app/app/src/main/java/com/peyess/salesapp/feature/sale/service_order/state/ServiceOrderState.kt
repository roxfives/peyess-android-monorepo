package com.peyess.salesapp.feature.sale.service_order.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity

data class ServiceOrderState(
    val userClientAsync: Async<ClientEntity?> = Uninitialized,
    val responsibleClientAsync: Async<ClientEntity?> = Uninitialized,
    val witnessClientAsync: Async<ClientEntity?> = Uninitialized,

    val prescriptionPictureAsync: Async<PrescriptionPictureEntity> = Uninitialized,
    val prescriptionDataAsync: Async<PrescriptionDataEntity> = Uninitialized,

    val lensEntityAsync: Async<LocalLensEntity> = Uninitialized,
    val coloringEntityAsync: Async<LocalColoringEntity> = Uninitialized,
    val treatmentEntityAsync: Async<LocalTreatmentEntity> = Uninitialized,
    val framesEntityAsync: Async<FramesEntity> = Uninitialized,
): MavericksState {
    val isUserLoading = userClientAsync is Loading
    val userClient = if (userClientAsync is Success) {
        userClientAsync.invoke() ?: ClientEntity()
    } else {
        ClientEntity()
    }

    val isResponsibleLoading = responsibleClientAsync is Loading
    val responsibleClient = if (responsibleClientAsync is Success) {
        responsibleClientAsync.invoke() ?: ClientEntity()
    } else {
        ClientEntity()
    }

    val isWitnessLoading = witnessClientAsync is Loading
    val witnessClient = witnessClientAsync.invoke()

    val isPrescriptionPictureLoading = prescriptionPictureAsync is Loading
    val prescriptionPicture = if (prescriptionPictureAsync is Success) {
        prescriptionPictureAsync.invoke()
    } else {
        PrescriptionPictureEntity()
    }

    val isPrescriptionDataLoading = prescriptionDataAsync is Loading
    val prescriptionData = if (prescriptionDataAsync is Success) {
        prescriptionDataAsync.invoke()
    } else {
        PrescriptionDataEntity()
    }

    val isLensLoading = lensEntityAsync is Loading
    val lensEntity = if (lensEntityAsync is Success) {
        lensEntityAsync.invoke()
    } else {
        LocalLensEntity()
    }

    val isColoringLoading = lensEntityAsync is Loading
    val coloringEntity = if (coloringEntityAsync is Success) {
        coloringEntityAsync.invoke()
    } else {
        LocalColoringEntity()
    }

    val isTreatmentLoading = treatmentEntityAsync is Loading
    val treatmentEntity = if (treatmentEntityAsync is Success) {
        treatmentEntityAsync.invoke()
    } else {
        LocalTreatmentEntity()
    }

    val isFramesLoading = framesEntityAsync is Loading
    val framesEntity = if (framesEntityAsync is Success) {
        framesEntityAsync.invoke()
    } else {
        FramesEntity()
    }
}
