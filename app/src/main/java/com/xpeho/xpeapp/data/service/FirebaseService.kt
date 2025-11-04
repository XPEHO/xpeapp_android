package com.xpeho.xpeapp.data.service

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.FEATURE_FLIPPING_COLLECTION
import com.xpeho.xpeapp.data.NEWSLETTERS_COLLECTION
import com.xpeho.xpeapp.data.model.FeatureFlipping
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.data.model.toFeatureFlipping
import com.xpeho.xpeapp.utils.CrashlyticsUtils
import kotlinx.coroutines.tasks.await
import java.time.ZoneId

class FirebaseService {
    suspend fun authenticate() {
        try {
            CrashlyticsUtils.logEvent("Firebase: Tentative d'authentification anonyme")
            FirebaseAuth.getInstance().signInAnonymously().await()
            CrashlyticsUtils.logEvent("Firebase: Authentification anonyme réussie")
        } catch (e: Exception) {
            CrashlyticsUtils.logEvent("Firebase: Erreur d'authentification anonyme")
            CrashlyticsUtils.recordException(e)
            throw e
        }
    }

    fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null

    fun signOut() {
        try {
            CrashlyticsUtils.logEvent("Firebase: Déconnexion")
            FirebaseAuth.getInstance().signOut()
        } catch (e: Exception) {
            CrashlyticsUtils.recordException(e)
            throw e
        }
    }

    suspend fun fetchFeatureFlipping(): List<FeatureFlipping> {
        try {
            CrashlyticsUtils.logEvent("Firebase: Récupération des feature flags")
            val db = FirebaseFirestore.getInstance()
            val document = db.collection(FEATURE_FLIPPING_COLLECTION)
                .get()
                .await()

            val featureFlippingList = mutableListOf<FeatureFlipping>()
            for (doc in document.documents) {
                val featureFlipping = doc.toFeatureFlipping()
                if (!featureFlippingList.contains(featureFlipping)) {
                    featureFlippingList.add(featureFlipping)
                } else {
                    featureFlippingList[featureFlippingList.indexOf(featureFlipping)] = featureFlipping
                }
            }
            CrashlyticsUtils.logEvent("Firebase: Feature flags récupérés avec succès (${featureFlippingList.size} éléments)")
            return featureFlippingList
        } catch (firebaseException: FirebaseException) {
            Log.e("fetchFeatureFlipping", "Error getting documents: $firebaseException")
            CrashlyticsUtils.logEvent("Firebase: Erreur lors de la récupération des feature flags")
            CrashlyticsUtils.recordException(firebaseException)
            return emptyList()
        }
    }

    suspend fun fetchNewsletters(): List<Newsletter> {
        val newslettersList = mutableListOf<Newsletter>()
        val db = FirebaseFirestore.getInstance()
        val defaultSystemOfZone = ZoneId.systemDefault()

        try {
            CrashlyticsUtils.logEvent("Firebase: Récupération des newsletters")
            db.collection(NEWSLETTERS_COLLECTION)
                .get()
                .await()
                .map {
                    val dateTimestamp = (it.data["date"] as com.google.firebase.Timestamp)
                        .toDate()
                        .toInstant()
                    val publicationDateTime =
                        (it.data["publicationDate"] as com.google.firebase.Timestamp)
                            .toDate()
                            .toInstant()
                    val newsletter = Newsletter(
                        id = it.id,
                        summary = it.data["summary"].toString(),
                        date = dateTimestamp.atZone(defaultSystemOfZone)
                            .toLocalDate(),
                        publicationDate = publicationDateTime.atZone(defaultSystemOfZone)
                            .toLocalDate(),
                        pdfUrl = it.data["pdfUrl"].toString(),
                        picture = it.data["previewPath"].toString()
                    )
                    newslettersList.add(newsletter)
                }
            CrashlyticsUtils.logEvent("Firebase: Newsletters récupérées avec succès (${newslettersList.size} éléments)")
        } catch (firebaseException: FirebaseException) {
            Log.d("fetchNewsletters", "Error getting documents: ", firebaseException)
            CrashlyticsUtils.logEvent("Firebase: Erreur lors de la récupération des newsletters")
            CrashlyticsUtils.recordException(firebaseException)
        }

        return newslettersList.sortedByDescending { it.date }
    }

}