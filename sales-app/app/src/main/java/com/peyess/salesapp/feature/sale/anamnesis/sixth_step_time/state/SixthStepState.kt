package com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import kotlin.math.abs
import kotlin.math.max

data class SixthStepState(
    @PersistState val readingSlider: Float = 0f,
    @PersistState val phoneSlider: Float = 0f,
    @PersistState val computerSlider: Float = 0f,
    @PersistState val televisionSlider: Float = 0f,
    @PersistState val drivingSlider: Float = 0f,
    @PersistState val sportsSlider: Float = 0f,
    @PersistState val externalAreaSlider: Float = 0f,
    @PersistState val internalAreaSlider: Float = 0f,

    @PersistState val timeWakeUp: Float = 360f,
    @PersistState val timeGoToBed: Float = 1320f,
): MavericksState {
    val reading = readingSlider.toInt() * 30f
    val phone = phoneSlider.toInt() * 30f
    val computer = computerSlider.toInt() * 30f
    val television = televisionSlider.toInt() * 30f
    val driving = drivingSlider.toInt() * 30f
    val sports = sportsSlider.toInt() * 30f
    val externalArea = externalAreaSlider.toInt() * 30f
    val internalArea = internalAreaSlider.toInt() * 30f

    val totalUsed = reading + phone + computer + television +
            driving + sports + externalArea + internalArea

    val totalAvailable = max(
        abs(timeGoToBed - timeWakeUp),
        1440 - abs(timeGoToBed - timeWakeUp),
    )

    val totalLeft = totalAvailable - totalUsed

    val farUsage = (sports + driving) / totalUsed
    val nearUsage = (reading + phone) / totalUsed
    val intermediateUsage = (computer + television) / totalUsed

    val sunLightExposure = (driving + sports + externalArea) / totalUsed

    val artificialLightExposure = (phone + computer + television + externalArea + internalArea) / totalUsed

    val blueLightExposure = (phone + computer + television + externalArea + internalArea) / totalUsed
}
