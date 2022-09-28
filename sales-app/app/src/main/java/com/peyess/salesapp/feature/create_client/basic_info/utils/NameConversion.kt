package com.peyess.salesapp.feature.create_client.basic_info.utils

import com.peyess.salesapp.data.model.client.Sex

fun readableSexName(sex: Sex): String {
    return when (sex) {
        is Sex.None -> "Nenhum"
        is Sex.Female -> "Feminino"
        is Sex.Male -> "Masculino"
        is Sex.Other -> "Outro"
    }
}

fun fromReadableSexName(sexName: String): Sex {
    return when (sexName) {
        "Feminino" -> Sex.Female
        "Masculino" -> Sex.Male
        "Outro" -> Sex.Other
        else -> Sex.None
    }
}