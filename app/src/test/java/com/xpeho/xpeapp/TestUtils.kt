package com.xpeho.xpeapp

import android.os.Process
import android.os.StrictMode
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic

/**
 * Mocke toutes les méthodes statiques Android/Firebase nécessaires pour les tests JVM.
 */
fun mockAllAndroidFirebaseStatics() {
    // Mock statiques Android/Firebase
    mockkStatic(Log::class)
    mockkStatic(StrictMode::class)
    mockkStatic(Process::class)
    mockkStatic(FirebaseApp::class)
    mockkStatic(FirebaseCrashlytics::class)

    // Log
    every { Log.e(any<String>(), any<String>()) } returns 0
    every { Log.d(any<String>(), any<String>()) } returns 0
    every { Log.i(any<String>(), any<String>()) } returns 0
    every { Log.w(any<String>(), any<String>()) } returns 0
    every { Log.v(any<String>(), any<String>()) } returns 0
    every { Log.e(any<String>(), any<String>(), any<Throwable>()) } returns 0
    every { Log.d(any<String>(), any<String>(), any<Throwable>()) } returns 0
    every { Log.i(any<String>(), any<String>(), any<Throwable>()) } returns 0
    every { Log.w(any<String>(), any<String>(), any<Throwable>()) } returns 0
    every { Log.v(any<String>(), any<String>(), any<Throwable>()) } returns 0

    // StrictMode & Process
    every { StrictMode.allowThreadDiskReads() } returns null
    every { StrictMode.setThreadPolicy(any()) } returns Unit
    every { Process.myPid() } returns 1234

    // Firebase
    val fakeCrash = mockk<FirebaseCrashlytics>()
    every { FirebaseCrashlytics.getInstance() } returns fakeCrash
    every { fakeCrash.setCustomKey(any<String>(), any<Boolean>()) } returns Unit
    every { fakeCrash.setCustomKey(any<String>(), any<Double>()) } returns Unit
    every { fakeCrash.setCustomKey(any<String>(), any<Float>()) } returns Unit
    every { fakeCrash.setCustomKey(any<String>(), any<Int>()) } returns Unit
    every { fakeCrash.setCustomKey(any<String>(), any<Long>()) } returns Unit
    every { fakeCrash.setCustomKey(any<String>(), any<String>()) } returns Unit
    every { fakeCrash.log(any<String>()) } returns Unit
    every { fakeCrash.recordException(any()) } returns Unit
    every { fakeCrash.setUserId(any<String>()) } returns Unit
    val fakeApp = mockk<FirebaseApp>()
    every { FirebaseApp.getInstance() } returns fakeApp
}