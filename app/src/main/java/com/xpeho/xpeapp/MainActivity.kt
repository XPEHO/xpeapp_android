package com.xpeho.xpeapp

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.Home
import com.xpeho.xpeapp.ui.notifications.AlarmScheduler
import com.xpeho.xpeapp.ui.theme.XpeAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Schedule the alarm if the permission is granted
            scheduleNotificationAlarm()
        } else {
            Log.d("Permission", "Permission denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        // Check if the app has the necessary notifications permissions.
        checkPermissions()

        // Check for app update
        checkForUpdate()

        // This is done to skip to the Home screen faster,
        // thus not forcing the user to wait for authentication.
        val connectedLastTime = runBlocking {
            XpeApp.appModule.datastorePref.getWasConnectedLastTime()
        }
        val startScreenFlow: MutableStateFlow<Screens> =
            MutableStateFlow(if (connectedLastTime) Screens.Home else Screens.Login)

        // Vérification périodique de l'expiration du token (toutes les 30 secondes)
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                kotlinx.coroutines.delay(30_000) // 30 secondes
                
                // Vérifier si on est connecté et si le token a expiré
                val authState = XpeApp.appModule.authenticationManager.authState.value
                if (authState is com.xpeho.xpeapp.domain.AuthState.Authenticated) {
                    if (!XpeApp.appModule.authenticationManager.isAuthValid()) {
                        // isAuthValid() fait automatiquement le logout si le token a expiré
                        // Le changement d'état sera détecté par Home.kt pour la redirection
                    }
                }
            }
        }

        // If the user was connected last time, try to restore the authentication state.
        if (connectedLastTime) {
            CoroutineScope(Dispatchers.IO).launch {
                XpeApp.appModule.authenticationManager.restoreAuthStateFromStorage()
                if (!XpeApp.appModule.authenticationManager.isAuthValid()) {
                    // isAuthValid() fait déjà le logout si nécessaire
                    // Home.kt gérera la redirection
                }
            }
        }

        setContent {
            XpeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = XpehoColors.BACKGROUND_COLOR
                ) {
                    val startScreen = startScreenFlow.collectAsStateWithLifecycle()
                    Home(startScreen.value)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("Permission", "Permission already granted")
                scheduleNotificationAlarm()
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun scheduleNotificationAlarm() {
        val alarmScheduler = AlarmScheduler()
        alarmScheduler.scheduleAlarm(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkForUpdate() {
        // Check for updates only in release mode
        if (!BuildConfig.DEBUG) {
            CoroutineScope(Dispatchers.IO).launch {
                val latestVersion = getLatestReleaseTag()
                val currentVersion = getCurrentAppVersion()

                // If the latest version is not null and is greater than the current version,
                if (latestVersion != null && isVersionLessThan(currentVersion, latestVersion)) {
                    withContext(Dispatchers.Main) {
                        showUpdateDialog(latestVersion)
                    }
                }
            }
        }
    }

    private fun showUpdateDialog(version: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.force_update_popup_title_label))
            .setMessage(
                getString(
                    R.string.force_update_popup_message_label,
                    version
                )
            )
            .setCancelable(false)
            .setPositiveButton(getString(R.string.force_update_popup_button_label)) { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=$packageName")
                    setPackage("com.android.vending")
                }
                startActivity(intent)
            }
            .show()
    }

    private fun getCurrentAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    private suspend fun getLatestReleaseTag(): String? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.github.com/repos/XPEHO/xpeapp_android/releases/latest")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val jsonResponse = JSONObject(response.body?.string() ?: "")
                jsonResponse.getString("tag_name")
            }
        }
    }

    private fun isVersionLessThan(currentVersion: String, latestVersion: String): Boolean {
        val currentParts = currentVersion.split(".")
        val latestParts = latestVersion.split(".")

        for (i in 0 until maxOf(currentParts.size, latestParts.size)) {
            val currentPart = currentParts.getOrNull(i)?.toIntOrNull() ?: 0
            val latestPart = latestParts.getOrNull(i)?.toIntOrNull() ?: 0

            if (currentPart < latestPart) return true
            if (currentPart > latestPart) return false
        }
        return false
    }
}
