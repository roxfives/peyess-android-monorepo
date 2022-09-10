package com.peyess.salesapp.feature.demonstration.state

import com.airbnb.mvrx.MavericksState

sealed class DemonstrationShown {
    object Multi: DemonstrationShown()
    object Treatments: DemonstrationShown()
    object Photo: DemonstrationShown()

    fun name(): String {
        return nameOf(this)
    }

    companion object {
        fun listOfDemonstrations(): List<DemonstrationShown> {
            return listOf(
                Multi,
                Treatments,
                Photo,
            )
        }

        fun nameOf(demo: DemonstrationShown): String {
            // TODO: use string resource
            return when (demo) {
                Multi -> "Multifocais"
                Treatments -> "Tratamentos"
                Photo -> "Fotossensíveis"
            }
        }

        fun fromName(name: String): DemonstrationShown {
            return when (name) {
                "Multifocais" -> Multi
                "Tratamentos" -> Treatments
                else -> Photo
            }
        }
    }
}

data class DemonstrationState(
    val demonstrationShown: DemonstrationShown = DemonstrationShown.Multi
): MavericksState