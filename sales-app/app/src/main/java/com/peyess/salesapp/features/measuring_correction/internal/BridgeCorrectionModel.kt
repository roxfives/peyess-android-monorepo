package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

//['bridge', 'horizontal_check', 'vertical_check', 've', 'bottom_point', 'euler_angle_x']
//[-0.29235279 -0.17628389 -0.1462711   0.12873821 -0.05510489  0.03842293]
//8.55350834500817

internal class BridgeCorrectionModel : CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val bridge = rawMeasuring.bridge * -0.29235279 //-0.63286615 //-0.69311305
        val horizontalCheck = rawMeasuring.horizontalCheck * -0.17628389 //-0.25144899 //-0.2746577
        val verticalCheck = rawMeasuring.verticalCheck * -0.1462711 //-0.07611089 //-0.0840033
        val ve = rawMeasuring.ve * 0.12873821 //0.19329001 //0.2447102
        val bottomPoint = rawMeasuring.bottomPoint * -0.05510489 //-0.11161496 //-0.12043775
        val eulerX = rawMeasuring.eulerAngleX * 0.03842293 //0.13882194 //0.11192217
        val intercept = 8.55350834500817 //0.08683901736094168 //0.1367223274938127

        val diff = bridge + horizontalCheck + verticalCheck + ve + bottomPoint + eulerX + intercept
        val prediction = rawMeasuring.bridge + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}