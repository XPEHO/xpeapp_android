package com.xpeho.xpeapp

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic

fun mockAllLogMethods() {
    mockkStatic(Log::class)
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
}
