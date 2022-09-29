package com.peyess.salesapp.data.model.client_legal

sealed class ClientLegalMethod {
    object SalesAppCreateAccount: ClientLegalMethod()

    fun toName() = toName(this)

    companion object {
        fun fromName(name: String): ClientLegalMethod? {
            return if (name == "sales-app:create-account") {
                SalesAppCreateAccount
            } else {
                null
            }
        }

        fun toName(method: ClientLegalMethod): String {
            return "sales-app:create-account"
        }
    }
}
