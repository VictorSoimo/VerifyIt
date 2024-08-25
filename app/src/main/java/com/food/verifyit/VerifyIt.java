package com.food.verifyit;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class VerifyIt extends Application {

    public void onCreate() {
        super.onCreate();
        // Initialize Firebase here
        FirebaseApp.initializeApp(this);
    }
}
