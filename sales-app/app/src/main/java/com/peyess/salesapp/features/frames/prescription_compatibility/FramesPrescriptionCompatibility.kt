package com.peyess.salesapp.features.frames.prescription_compatibility

import com.peyess.salesapp.features.frames.prescription_compatibility.model.FramesCompatibility
import com.peyess.salesapp.features.frames.prescription_compatibility.model.PrescriptionCompatibility
import com.peyess.salesapp.typing.frames.FramesType

fun isFramesInCompatible(
    frames: FramesCompatibility,
    prescription: PrescriptionCompatibility,
): Boolean {
    val areFramesProblematic = frames.type is FramesType.AcetateScrewed
            || frames.type is FramesType.MetalScrewed
            || frames.type is FramesType.AcetateNylon
            || frames.type is FramesType.MetalNylon

    val hasProblemsOnLeft = prescription.sphericalLeft <= -4.0
            || prescription.sphericalLeft >= 4.0
            || prescription.sphericalLeft + prescription.cylindricalLeft <= -4.0

    val hasProblemsOnRight = prescription.sphericalRight <= -4.0
            || prescription.sphericalRight >= 4.0
            || prescription.sphericalRight + prescription.cylindricalRight <= -4.0

    return areFramesProblematic && (hasProblemsOnLeft || hasProblemsOnRight)
}