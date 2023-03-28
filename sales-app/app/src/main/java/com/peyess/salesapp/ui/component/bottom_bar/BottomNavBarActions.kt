package com.peyess.salesapp.ui.component.bottom_bar

interface BottomNavBarActions {
    val onHomeSelected: () -> Unit
    val onSaleSelected: () -> Unit
    val onPeopleSelected: () -> Unit
}