package com.peyess.salesapp.screen.create_client.basic_info.utils

import com.peyess.salesapp.typing.client.Sex

fun readableSexName(sex: Sex): String {
    return when (sex) {
        is Sex.Female -> "Feminino"
        is Sex.Male -> "Masculino"
        is Sex.Unknown -> "Prefiro não dizer"
    }
}

fun fromReadableSexName(sexName: String): Sex {
    return when (sexName) {
        "Feminino" -> Sex.Female
        "Masculino" -> Sex.Male
        else -> Sex.Unknown
    }
}