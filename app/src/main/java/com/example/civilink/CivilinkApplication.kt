package com.example.civilink

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class CivilinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable Firebase Realtime Database persistence for offline support
        // This ensures the app remains responsive even with poor connectivity
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
