package com.peyess.salesapp.typing.frames

import timber.log.Timber

sealed class FramesType {
    object MetalNylon: FramesType()
    object MetalEnclosed: FramesType()
    object MetalScrewed: FramesType()
    object AcetateEnclosed: FramesType()
    object AcetateNylon: FramesType()
    object AcetateScrewed: FramesType()
    object None: FramesType()

    fun toName() = toName(this)

    companion object {
        val listOfPositions by lazy {
            listOf(
                MetalNylon,
                MetalEnclosed,
                MetalScrewed,
                AcetateEnclosed,
                AcetateNylon,
                AcetateScrewed,
            )
        }

        fun toName(position: FramesType): String {
            Timber.i("Translating position $position")

            return when(position) {
                AcetateEnclosed -> "Acetato Fechado"
                AcetateNylon -> "Acetato Nylon"
                AcetateScrewed -> "Acetato Parafusado"
                MetalEnclosed -> "Metal Fechado"
                MetalNylon -> "Metal Nylon"
                MetalScrewed -> "Metal Parafusado"
                None-> "None"
            }
        }

        fun toFramesType(name: String): FramesType {
            Timber.i("Translating name $name")

            return when(name) {
                "Acetato Fechado" -> AcetateEnclosed
                "Acetato Nylon" -> AcetateNylon
                "Acetato Parafusado" -> AcetateScrewed
                "Metal Fechado" -> MetalEnclosed
                "Metal Nylon" -> MetalNylon
                "Metal Parafusado" -> MetalScrewed
                else -> None
            }
        }
    }
}
