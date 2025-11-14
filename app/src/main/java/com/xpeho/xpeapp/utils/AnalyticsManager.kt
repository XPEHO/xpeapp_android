package com.xpeho.xpeapp.utils

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsManager {
    fun logEvent(name: String, params: Bundle? = null)
    fun logScreen(screenName: String)
    fun setUserId(userId: String?)
}

class FirebaseAnalyticsManager(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsManager {
    override fun logEvent(name: String, params: Bundle?) {
        Log.d("Analytics", "logEvent: $name params=${params?.keySet()?.associateWith { params.get(it) }}")
        firebaseAnalytics.logEvent(name, params)
    }

    override fun logScreen(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, screenName)
        }
        Log.d("Analytics", "logScreen: $screenName")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
    }

    override fun setUserId(userId: String?) {
        Log.d("Analytics", "setUserId: $userId")
        firebaseAnalytics.setUserId(userId)
    }
}

object AnalyticsEventName {
    const val OPEN_NEWSLETTER = "open_newsletter"
    const val AGENCY_PAGE = "agency_page"
    const val QVST_PAGE = "qvst_page"
    const val NEWSLETTER_PAGE = "newsletter_page"
    const val OPEN_HOME = "open_home"
    const val ABOUT_OPEN = "about_open"
    const val IDEA_SUBMITTED = "idea_submitted"
    const val IDEABOX_PAGE = "ideabox_page"
    const val CAMPAIGN_OPEN = "campaign_open"
    const val CAMPAIGN_VIEW_RESULTS = "campaign_view_results"
    const val CAMPAIGN_COMPLETED = "campaign_completed"
    const val NEWSLETTER_FILTER_SELECTED = "newsletter_filter_selected"
    const val CAMPAIGN_FILTER_SELECTED = "campaign_filter_selected"
    const val HOME_PAGE = "home_page"
    const val LOGIN_PAGE = "login_page"
    const val AGENDA_PAGE = "agenda_page"
    const val QVST_CAMPAIGN_DETAIL_PAGE = "qvst_campaign_detail_page"
}

object AnalyticsParamKey {
    const val ITEM_ID = FirebaseAnalytics.Param.ITEM_ID
    const val ITEM_NAME = FirebaseAnalytics.Param.ITEM_NAME
}
