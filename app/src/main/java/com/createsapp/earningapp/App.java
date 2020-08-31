package com.createsapp.earningapp;

import android.app.Application;
import android.os.StrictMode;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //initialize fb sdk
        AudienceNetworkAds.initialize(this);

        //initialize admob sdk
        MobileAds.initialize(this);
    }
}
