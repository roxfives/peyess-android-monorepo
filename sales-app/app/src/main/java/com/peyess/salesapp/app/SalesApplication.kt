package com.peyess.salesapp.app

import android.app.Application
import android.content.Context
import com.airbnb.mvrx.Mavericks
import com.peyess.salesapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class SalesApplication: Application() {

    companion object {
        lateinit var context: Context
        lateinit var string: (id: Int) -> String
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        string = {
            context.resources.getString(it)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Mavericks.initialize(this)
    }
}