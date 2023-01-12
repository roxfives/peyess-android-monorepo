package com.peyess.salesapp.feature.demonstration.state

import com.airbnb.mvrx.MavericksState

sealed class DemonstrationShown {
    object Multi: DemonstrationShown()
    object Treatments: DemonstrationShown()
    object Photo: DemonstrationShown()
    object Polarized: DemonstrationShown()
    object Misc: DemonstrationShown()

    fun name(): String {
        return nameOf(this)
    }

    companion object {
        fun listOfDemonstrations(): List<DemonstrationShown> {
            return listOf(
                Multi,
                Treatments,
                Photo,
                Polarized,
                Misc
            )
        }

        fun nameOf(demo: DemonstrationShown): String {
            // TODO: use string resource
            return when (demo) {
                Multi -> "Multifocais"
                Treatments -> "Antirreflexo"
                Photo -> "Fotossensíveis"
                Polarized -> "Polarizada"
                Misc -> "Outros"
            }
        }

        fun fromName(name: String): DemonstrationShown {
            return when (name) {
                "Multifocais" -> Multi
                "Antirreflexo" -> Treatments
                "Polarizada" -> Polarized
                "Outros" -> Misc
                else -> Photo
            }
        }
    }
}

data class DemonstrationState(
    val demonstrationShown: DemonstrationShown = DemonstrationShown.Multi
): MavericksState
