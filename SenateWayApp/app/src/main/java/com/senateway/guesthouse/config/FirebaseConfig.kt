package com.senateway.guesthouse.config

import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.analytics.FirebaseAnalytics

//singleton object used to centralize all Firebase related instances
//Wijaya, Anthony. “Setting Singleton Property Value in Firebase Listener.” Stack Overflow, 18 Oct. 2015, stackoverflow.com/questions/33203379/setting-singleton-property-value-in-firebase-listener.
object FirebaseConfig {
    val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    
    fun getAnalytics(activity: android.app.Activity): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(activity)
    }
    
    init {
        //Enable persistence for offline support
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}

