package com.xpeho.xpeapp.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {
    
    /**
     * Enregistre un événement personnalisé dans Crashlytics
     */
    fun logEvent(message: String) {
        try {
            FirebaseCrashlytics.getInstance().log(message)
        } catch (_: Throwable) {
        }
    }
    
    /**
     * Enregistre une exception non fatale dans Crashlytics
     */
    fun recordException(exception: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(exception)
        } catch (_: Throwable) {
        }
    }
    
    /**
     * Définit un identifiant utilisateur pour les crash reports
     */
    fun setUserId(userId: String) {
        try {
            FirebaseCrashlytics.getInstance().setUserId(userId)
        } catch (_: Throwable) {
        }
    }
    
    /**
     * Ajoute une clé-valeur personnalisée aux crash reports
     */
    fun setCustomKey(key: String, value: String) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey(key, value)
        } catch (_: Throwable) {
        }
    }
    
    /**
     * Définit le screen/écran actuel pour contextualiser les erreurs
     */
    fun setCurrentScreen(screenName: String) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey("screen", screenName)
        } catch (_: Throwable) {
        }
        logEvent("Navigation vers: $screenName")
    }
    
    /**
     * Définit la feature/fonctionnalité actuelle
     */
    fun setCurrentFeature(featureName: String) {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey("feature", featureName)
        } catch (_: Throwable) {
        }
    }
    
    /**
     * Définit le contexte utilisateur (connecté/déconnecté)
     */
    fun setUserContext(isLoggedIn: Boolean, userRole: String = "") {
        try {
            FirebaseCrashlytics.getInstance().setCustomKey("user_logged_in", isLoggedIn.toString())
            if (userRole.isNotEmpty()) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_role", userRole)
            }
        } catch (_: Throwable) {
        }
    }
}