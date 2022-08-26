package com.peyess.salesapp.dao.products.firestore.disponibility

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSDisponibility(

    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("diam")
    val diam: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("max_cyl")
    val maxCyl: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("min_cyl")
    val minCyl: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("max_sph")
    val maxSph: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("min_sph")
    val minSph: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("max_add")
    val maxAdd: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("min_add")
    val minAdd: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("has_prism")
    val hasPrism: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("prism")
    val prism: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("prism_price")
    val prismPrice: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("prism_cost")
    val prismCost: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("separate_prism")
    val separatePrism: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("needs_check")
    val needsCheck: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("sum_rule")
    val sumRule: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("manufacturers")
    val manufacturers: Map<String, FSDispManufacturer> = emptyMap(),

)