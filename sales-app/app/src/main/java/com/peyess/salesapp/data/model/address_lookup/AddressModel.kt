package com.peyess.salesapp.data.model.address_lookup

data class AddressModel(
    val state: String = "",
    val city: String = "",

    val zipCode: String = "",

    val neighborhood: String = "",
    val street: String = "",
    val complement: String = "",
    val publicPlace: String = "",

    val ibge: String = "",
    val siafi: String = "",
    val gia: String = "",
)
