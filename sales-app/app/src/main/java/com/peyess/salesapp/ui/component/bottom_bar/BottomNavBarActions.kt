package com.peyess.salesapp.ui.component.bottom_bar

interface BottomNavBarActions {
    val onHomeSelected: () -> Unit
    val onPeopleSelected: () -> Unit
    val onFramesSelected: () -> Unit
    val onRealMeasureSelected: () -> Unit
}