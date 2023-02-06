package com.peyess.salesapp.screen.sale.lens_pick.constant

sealed class ListFilter {
    object LensType: ListFilter()
    object LensSupplier: ListFilter()
    object LensFamily: ListFilter()
    object LensDescription: ListFilter()
    object LensMaterial: ListFilter()
    object LensSpecialty: ListFilter()
    object LensGroup: ListFilter()
}
