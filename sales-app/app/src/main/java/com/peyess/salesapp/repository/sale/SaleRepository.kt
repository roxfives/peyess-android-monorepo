package com.peyess.salesapp.repository.sale

import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.model.products.LensTypeCategory
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun createSale(): Flow<Boolean>

    fun activeSale(): Flow<ActiveSalesEntity?>
    fun updateActiveSO(activeSOEntity: ActiveSOEntity)
    fun activeSO(): Flow<ActiveSOEntity?>

    fun updateSO(so: ActiveSOEntity)

    fun currentPrescriptionPicture(): Flow<PrescriptionPictureEntity>
    fun updatePrescriptionPicture(prescriptionPictureEntity: PrescriptionPictureEntity)

    fun currentPrescriptionData(): Flow<PrescriptionDataEntity>
    fun updatePrescriptionData(prescriptionDataEntity: PrescriptionDataEntity)

    fun currentPositioning(eye: Eye): Flow<PositioningEntity>
    fun updatePositioning(positioning: PositioningEntity)

    fun currentFramesData(): Flow<FramesEntity>
    fun updateFramesData(frames: FramesEntity)

    fun lensTypeCategories(): Flow<List<LensTypeCategory>>

    fun addLensForComparison(comparisonEntity: LensComparisonEntity)
    fun comparisons(): Flow<List<LensComparisonEntity>>
    fun removeComparison(id: Int)
}