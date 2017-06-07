package com.countmein.countmein;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.EApplication;

/**
 * Created by Home on 5/2/2017.
 */

@EApplication
public class CountMeInApp extends Application {


    @Override
    public void onCreate() {


       super.onCreate();
        Fresco.initialize(this);
    }
}
