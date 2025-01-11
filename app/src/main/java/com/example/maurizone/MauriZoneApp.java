package com.example.maurizone;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class MauriZoneApp extends Application {
    @Override
    public void onCreate() {
        Log.i("maurizoneapp", "onCreate: clicked");
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}

