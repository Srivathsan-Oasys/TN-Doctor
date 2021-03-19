package com.oasys.digihealth.doctor.fire_base_analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FireBaseManger(context: Context) {

    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun trackFireBaseEvent(pEventName: String, pProperties: Bundle) {
        firebaseAnalytics.logEvent(pEventName, pProperties)
    }
}