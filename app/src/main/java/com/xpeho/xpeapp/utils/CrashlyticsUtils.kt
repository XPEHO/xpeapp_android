package com.xpeho.xpeapp.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {

    private const val TAG = "CrashlyticsUtils"

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    /**
     * Enregistre un événement personnalisé dans Crashlytics.
     */
    fun logEvent(message: String) {
        try {
            crashlytics.log(message)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to log event to Crashlytics", e)
        }
    }

    /**
     * Enregistre une exception non fatale dans Crashlytics.
     */
    fun recordException(exception: Throwable) {
        try {
            crashlytics.recordException(exception)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to record exception to Crashlytics", e)
        }
    }

    /**
     * Définit un identifiant utilisateur pour les crash reports.
     */
    fun setUserId(userId: String) {
        try {
            crashlytics.setUserId(userId)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to set user ID", e)
        }
    }

    /**
     * Ajoute une clé-valeur personnalisée aux crash reports.
     */
    fun setCustomKey(key: String, value: String) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to set custom key ($key)", e)
        }
    }

    /**
     * Définit le screen/écran actuel pour contextualiser les erreurs.
     */
    fun setCurrentScreen(screenName: String) {
        try {
            crashlytics.setCustomKey("screen", screenName)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to set current screen", e)
        }
    }

    /**
     * Définit la feature/fonctionnalité actuelle.
     */
    fun setCurrentFeature(featureName: String) {
        try {
            crashlytics.setCustomKey("feature", featureName)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to set current feature", e)
        }
    }

    /**
     * Définit le contexte utilisateur (connecté/déconnecté).
     */
    fun setUserContext(isLoggedIn: Boolean, userRole: String = "") {
        try {
            crashlytics.setCustomKey("user_logged_in", isLoggedIn.toString())

            if (userRole.isNotEmpty()) {
                crashlytics.setCustomKey("user_role", userRole)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to set user context", e)
        }
    }
}
