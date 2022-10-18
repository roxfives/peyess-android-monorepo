package com.peyess.salesapp.features.measuring_correction.internal

import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.features.measuring_correction.CorrectionModel
import kotlin.math.round

//['he', 'bottom_point', 'vu', 'vertical_hoop', 'euler_angle_x']
//[-0.41125911  0.07294129  0.34769308 -0.02399553 -0.01028523]
//3.6376696749963684

internal class HECorrectionModel: CorrectionModel() {
    override fun fixField(rawMeasuring: RawMeasuring): Double {
        val he = rawMeasuring.he * -0.41125911 //-0.50958316
        val bottomPoint = rawMeasuring.bottomPoint * 0.07294129 // 0.07772573
        val vu = rawMeasuring.vu * 0.34769308 //0.50141493
        val verticalHoop = rawMeasuring.verticalHoop * -0.02399553 //-0.07498336
        val eulerX = rawMeasuring.eulerAngleX * -0.01028523 //-0.01581821
        val intercept = 3.6376696749963684

        val diff = he + bottomPoint + vu + verticalHoop + eulerX + intercept
        val prediction = rawMeasuring.he + diff
        val rounded = round(prediction * 2.0) / 2.0

        return rounded
    }
}