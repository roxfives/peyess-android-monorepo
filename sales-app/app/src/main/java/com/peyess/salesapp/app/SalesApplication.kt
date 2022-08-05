package com.peyess.salesapp.app

import android.app.Application
import com.airbnb.mvrx.Mavericks
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.firebase.FirebaseManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SalesApplication: Application() {

    @Inject
    lateinit var firebaseManager: FirebaseManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        firebaseManager.initializeFirebase()
        Mavericks.initialize(this)
    }

    fun stringResource(id: Int): String {
        return this.getString(id)
    }
}