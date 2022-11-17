package com.peyess.salesapp.dao.client.room

sealed class ClientRole {
    object User: ClientRole()
    object Responsible: ClientRole()
    object Witness: ClientRole()

    companion object {
        val allOptions by lazy {
            listOf(User, Responsible, Witness)
        }

        fun fromName(name: String?): ClientRole? {

            return when(name?.lowercase() ?: "") {
                "user" -> User
                "responsible" -> Responsible
                "witness" -> Witness
                else -> null
            }
        }

        fun fromType(type: ClientRole?): String? {

            return when(type) {
                is User -> "user"
                is Responsible -> "responsible"
                is Witness -> "witness"
                else -> null
            }
        }
    }
}
