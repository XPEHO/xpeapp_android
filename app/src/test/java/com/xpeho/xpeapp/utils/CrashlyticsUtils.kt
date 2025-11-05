package com.xpeho.xpeapp.utils

/**
 * Version de test de CrashlyticsUtils qui ne fait rien
 * pour éviter les problèmes avec Firebase dans les tests unitaires
 */
object CrashlyticsUtils {
    
    /**
     * Enregistre un événement personnalisé dans Crashlytics
     */
    fun logEvent(message: String) {
        // Do nothing in tests
    }
    
    /**
     * Enregistre une exception non fatale dans Crashlytics
     */
    fun recordException(exception: Throwable) {
        // Do nothing in tests
    }
    
    /**
     * Définit un identifiant utilisateur pour les crash reports
     */
    fun setUserId(userId: String) {
        // Do nothing in tests
    }
    
    /**
     * Ajoute une clé-valeur personnalisée aux crash reports
     */
    fun setCustomKey(key: String, value: String) {
        // Do nothing in tests
    }
    
    /**
     * Définit le screen/écran actuel pour contextualiser les erreurs
     */
    fun setCurrentScreen(screenName: String) {
        // Do nothing in tests
    }
    
    /**
     * Définit la feature/fonctionnalité actuelle
     */
    fun setCurrentFeature(featureName: String) {
        // Do nothing in tests
    }
    
    /**
     * Définit le contexte utilisateur (connecté/déconnecté)
     */
    fun setUserContext(isLoggedIn: Boolean, userRole: String = "") {
        // Do nothing in tests
    }
}