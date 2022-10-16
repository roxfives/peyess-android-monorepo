package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionField
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import com.peyess.salesapp.features.measuring_correction.correctionModelFactory
import kotlin.math.ceil
import kotlin.math.round

internal class HorizontalBridgeHoopCorrectionModel : CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val fixerBridge = correctionModelFactory(CorrectionField.Bridge)
        val fixerHHoop = correctionModelFactory(CorrectionField.HHoop)

        val fixedValue = fixerBridge.fixField(rawMeasuring) + fixerHHoop.fixField(rawMeasuring)
        val rounded = ceil(2.0 * fixedValue) / 2.0

        return rounded
    }
}