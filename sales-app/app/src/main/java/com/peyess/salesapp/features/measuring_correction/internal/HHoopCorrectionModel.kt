package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

internal class HHoopCorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val horizontalHoop = rawMeasuring.horizontalHoop * -0.11680091
        val verticalHoop = rawMeasuring.verticalHoop * -0.1343153
        val ve = rawMeasuring.ve * -0.34691312
        val horizontalBridgeHoop = rawMeasuring.horizontalBridgeHoop * -0.19174032
        val baseSize = rawMeasuring.baseSize * 0.09632585
        val intercept = 0.07213599721328494

        val diff = horizontalHoop + verticalHoop + ve + horizontalBridgeHoop + baseSize + intercept
        val prediction = rawMeasuring.horizontalHoop + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}