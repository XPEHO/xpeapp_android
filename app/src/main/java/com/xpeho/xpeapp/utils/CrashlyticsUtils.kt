package com.xpeho.xpeapp.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    /**
     * Enregistre un événement personnalisé dans Crashlytics.
     */
    fun logEvent(message: String) {
        crashlytics.log(message)
    }

    /**
     * Enregistre une exception non fatale dans Crashlytics.
     */
    fun recordException(exception: Throwable) {
        crashlytics.recordException(exception)
    }

    /**
     * Définit un identifiant utilisateur pour les crash reports.
     */
    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    /**
     * Ajoute une clé-valeur personnalisée aux crash reports.
     */
    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    /**
     * Définit le screen/écran actuel pour contextualiser les erreurs.
     */
    fun setCurrentScreen(screenName: String) {
        crashlytics.setCustomKey("screen", screenName)
    }

    /**
     * Définit la feature/fonctionnalité actuelle.
     */
    fun setCurrentFeature(featureName: String) {
        crashlytics.setCustomKey("feature", featureName)
    }

    /**
     * Définit le contexte utilisateur (connecté/déconnecté).
     */
    fun setUserContext(isLoggedIn: Boolean, userRole: String = "") {
        crashlytics.setCustomKey("user_logged_in", isLoggedIn.toString())

        if (userRole.isNotEmpty()) {
            crashlytics.setCustomKey("user_role", userRole)
        }
    }
}
