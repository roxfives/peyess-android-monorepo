package com.peyess.salesapp.app

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.airbnb.mvrx.Mavericks
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

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

        Firebase.initialize(this)
        Mavericks.initialize(this)
    }
}