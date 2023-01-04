package com.peyess.salesapp.typing.prescription

sealed class PrismPosition {
    object None: PrismPosition()
    object Nasal: PrismPosition()
    object Temporal: PrismPosition()
    object Superior: PrismPosition()
    object Inferior: PrismPosition()
    object Axis: PrismPosition()

    fun toName() = toName(this)

    companion object {
        val listOfPositions: List<PrismPosition> by lazy {
            listOf(
                None,
                Nasal,
                Temporal,
                Superior,
                Inferior,
                Axis,
            )
        }

        fun toName(position: PrismPosition?): String {
            return when(position) {
                is Nasal -> "Nasal"
                is Temporal -> "Temporal"
                is Superior -> "Superior"
                is Inferior -> "Inferior"
                is Axis -> "Eixo"
                else -> "Nenhuma"
            }
        }

        fun toPrism(name: String?): PrismPosition {
            return when(name?.lowercase()) {
                "nasal" ->  Nasal
                "temporal" ->  Temporal
                "superior" ->  Superior
                "inferior" -> Inferior
                "eixo" -> Axis
                else -> None
            }
        }
    }
}
