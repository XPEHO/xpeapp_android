package com.xpeho.xpeapp.ui.notifications

import android.content.Context

class Notification(private val context: Context) {

    companion object {
        const val NEWSLETTER_CHANNEL_ID = "XpeApp_Newsletters"
        const val NEWSLETTER_CHANNEL_NAME = "Nouvelles newsletters"
        const val NEWSLETTER_NOTIFICATION_TITLE = "Nouvelle newsletter !"
        const val NEWSLETTER_NOTIFICATION_MESSAGE = "Restez informé avec notre nouvelle newsletter !"

        const val QVST_NEW_CHANNEL_ID = "XpeApp_Qvst_New"
        const val QVST_NEW_CHANNEL_NAME = "Nouvelles campagnes QVST"
        const val QVST_NEW_NOTIFICATION_TITLE = "Nouvelle campagne QVST !"
        const val QVST_NEW_NOTIFICATION_MESSAGE = "Donnez votre avis dans la nouvelle campagne QVST !"

        const val QVST_REMINDER_CHANNEL_ID = "XpeApp_Qvst_Reminder"
        const val QVST_REMINDER_CHANNEL_NAME = "Rappels campagnes QVST"
        const val QVST_REMINDER_NOTIFICATION_TITLE = "Rappel campagne QVST !"
        const val QVST_REMINDER_NOTIFICATION_MESSAGE =
            "Dernier jour, n'oubliez pas de donner votre avis dans la campagne QVST !"

        const val QVST_RESULTS_CHANNEL_ID = "XpeApp_Qvst_Results"
        const val QVST_RESULTS_CHANNEL_NAME = "Résultats des campagnes QVST"
        const val QVST_RESULTS_NOTIFICATION_TITLE = "Résultats campagne QVST !"
        const val QVST_RESULTS_NOTIFICATION_MESSAGE = "Les résultats de la dernière campagne QVST sont disponibles !"
    }

}