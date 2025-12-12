package com.xpeho.xpeapp

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.xpeho.xpeapp.di.AppModule
import com.xpeho.xpeapp.di.MainAppModule

class XpeApp : Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase Crashlytics
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        appModule = MainAppModule(appContext = this)
    }
}