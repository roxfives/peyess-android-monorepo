package com.peyess.salesapp.typing.general

sealed class Eye {
    object Left: Eye()
    object Right: Eye()
    object None: Eye()

    fun toName() = toName(this)

    companion object {
        val allOptions by lazy {
            listOf(Left, Right, None)
        }

        fun toName(eye: Eye?): String {
            return when(eye) {
                is Right -> "right"
                is Left -> "left"
                else -> "none"
            }
        }

        fun toEye(name: String?): Eye {
            return when(name?.lowercase()) {
                "left" -> Left
                "right" -> Right
                else -> None
            }
        }
    }
}