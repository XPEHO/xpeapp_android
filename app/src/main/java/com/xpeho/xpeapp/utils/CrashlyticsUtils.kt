package com.xpeho.xpeapp.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {
    
    /**
     * Enregistre un événement personnalisé dans Crashlytics
     */
    fun logEvent(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }
    
    /**
     * Enregistre une exception non fatale dans Crashlytics
     */
    fun recordException(exception: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(exception)
    }
    
    /**
     * Définit un identifiant utilisateur pour les crash reports
     */
    fun setUserId(userId: String) {
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }
    
    /**
     * Ajoute une clé-valeur personnalisée aux crash reports
     */
    fun setCustomKey(key: String, value: String) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }
    
    /**
     * Définit le screen/écran actuel pour contextualiser les erreurs
     */
    fun setCurrentScreen(screenName: String) {
        FirebaseCrashlytics.getInstance().setCustomKey("screen", screenName)
        logEvent("Navigation vers: $screenName")
    }
    
    /**
     * Définit la feature/fonctionnalité actuelle
     */
    fun setCurrentFeature(featureName: String) {
        FirebaseCrashlytics.getInstance().setCustomKey("feature", featureName)
    }
    
    /**
     * Définit le contexte utilisateur (connecté/déconnecté)
     */
    fun setUserContext(isLoggedIn: Boolean, userRole: String = "") {
        FirebaseCrashlytics.getInstance().setCustomKey("user_logged_in", isLoggedIn.toString())
        if (userRole.isNotEmpty()) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_role", userRole)
        }
    }
    
    /**
     * Force un crash pour tester Crashlytics (à utiliser uniquement en test !)
     */
    fun testCrash() {
        throw RuntimeException("Test crash pour Crashlytics")
    }
}