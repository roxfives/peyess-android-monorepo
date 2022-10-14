package com.peyess.salesapp.features.measuring_correction

import com.peyess.salesapp.features.measuring_correction.internal.BridgeCorrectionModel
import com.peyess.salesapp.features.measuring_correction.internal.DiameterCorrectionModel
import com.peyess.salesapp.features.measuring_correction.internal.HECorrectionModel
import com.peyess.salesapp.features.measuring_correction.internal.HHoopCorrectionModel
import com.peyess.salesapp.features.measuring_correction.internal.IPDCorrectionModel
import com.peyess.salesapp.features.measuring_correction.internal.VHoopCorrectionModel

sealed class CorrectionField {
    object Bridge: CorrectionField()
    object IPD: CorrectionField()
    object HHoop: CorrectionField()
    object HE: CorrectionField()
    object VHoop: CorrectionField()
    object Diameter: CorrectionField()
}

fun correctionModelFactory(field: CorrectionField): CorrectionModel {
    return when (field) {
        CorrectionField.Bridge -> BridgeCorrectionModel()
        CorrectionField.HHoop -> HHoopCorrectionModel()
        CorrectionField.HE -> HECorrectionModel()
        CorrectionField.IPD -> IPDCorrectionModel()
        CorrectionField.VHoop -> VHoopCorrectionModel()
        CorrectionField.Diameter -> DiameterCorrectionModel()
    }
}