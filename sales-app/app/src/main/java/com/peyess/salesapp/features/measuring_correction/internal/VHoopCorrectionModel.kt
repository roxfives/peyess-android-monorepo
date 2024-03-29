package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

//['ipd', 'horizontal_bridge_hoop', 'base_size']
//[-0.28458225 -0.00170404  0.00614364]
//8.164947602377614

internal class VHoopCorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val ipd = rawMeasuring.ipd * -0.28458225 //-0.28278602
        val horizontalBridgeHoop = rawMeasuring.horizontalBridgeHoop * -0.00170404 //-0.00422118
        val baseSize = rawMeasuring.baseSize * 0.00614364 //0.0062254
        val intercept = 8.164947602377614 //8.266623473616168

        val diff = ipd + horizontalBridgeHoop + baseSize + intercept
        val prediction = rawMeasuring.verticalHoop + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}