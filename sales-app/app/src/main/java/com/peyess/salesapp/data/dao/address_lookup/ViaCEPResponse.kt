package com.peyess.salesapp.data.dao.address_lookup

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ViaCEPResponse")
@Keep
data class ViaCEPResponse(
    @SerialName("erro")
    val erro: Boolean = false,

    @SerialName("cep")
    val cep: String = "",
    @SerialName("logradouro")
    val logradouro: String = "",
    @SerialName("complemento")
    val complemento: String = "",
    @SerialName("bairro")
    val bairro: String = "",
    @SerialName("localidade")
    val localidade: String = "",
    @SerialName("uf")
    val uf: String = "",
    @SerialName("ibge")
    val ibge: String = "",
    @SerialName("gia")
    val gia: String = "",
    @SerialName("ddd")
    val ddd: String = "",
    @SerialName("siafi")
    val siafi: String = "",
)




