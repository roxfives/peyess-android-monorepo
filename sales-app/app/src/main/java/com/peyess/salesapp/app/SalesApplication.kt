package com.peyess.salesapp.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.airbnb.mvrx.Mavericks
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.firebase.FirebaseManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

@HiltAndroidApp
class SalesApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var firebaseManager: FirebaseManager

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        firebaseManager.initializeFirebase()
        Mavericks.initialize(this)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()



    fun stringResource(id: Int): String {
        return this.getString(id)
    }
}