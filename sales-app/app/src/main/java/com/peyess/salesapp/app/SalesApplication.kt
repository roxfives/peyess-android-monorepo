package com.peyess.salesapp.app

import android.app.Application
import com.airbnb.mvrx.Mavericks
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SalesApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Mavericks.initialize(this)
    }
}