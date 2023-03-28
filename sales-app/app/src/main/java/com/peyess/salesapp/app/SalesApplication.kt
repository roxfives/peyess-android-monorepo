package com.peyess.salesapp.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.airbnb.mvrx.Mavericks
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
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

    private val workerConfig by lazy {
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private val productsTableChannelId = "products_table_channel_id"
    private val clientsSyncChannelId = "client_sync_channel_id"

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        createNotificationChannels()

        firebaseManager.initializeFirebase()
        Mavericks.initialize(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfig
    }

    private fun createNotificationChannels() {
        createProductsNotificationChannels()
        createClientsNotificationChannels()
    }

    private fun createProductsNotificationChannels() {
        val name = getString(R.string.channel_name_products_table)
        val descriptionText = getString(R.string.channel_description_products_table)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(productsTableChannelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createClientsNotificationChannels() {
        val name = getString(R.string.channel_name_clients_sync)
        val descriptionText = getString(R.string.channel_description_clients_sync)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(clientsSyncChannelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun stringResource(id: Int): String {
        return this.getString(id)
    }
}