package com.burhanrashid52.imageeditor;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.burhanrashid52.imageeditor.db.DBHelper;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Burhanuddin Rashid on 1/23/2018.
 */

public class PhotoApp extends MultiDexApplication {
    private static PhotoApp sPhotoApp;
    private static final String TAG = PhotoApp.class.getSimpleName();
    private static DBHelper dbHelper;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        sPhotoApp = this;
        dbHelper = new DBHelper(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initAdmobs();
        //Fabric.with(this, new Crashlytics());
        //config.setDefaults(R.xml.remote_config_defaults);
    }

    private void initAdmobs() {
        MobileAds.initialize(this, initializationStatus -> {
            LogUtils.d("onInitializationComplete");
            List<String> testDeviceIds = Arrays.asList("B91277E7C65EB6333A9FF9C09BC08FA7","230DA6DF19C0142A477D8C8BA7D91AB4",
                    "2441075FBFB9BE9CD6B30E665BE9BC02", "AA0505B4BF536BB1458971FBB57A6314", "230DA6DF19C0142A477D8C8BA7D91AB4", "2441075FBFB9BE9CD6B30E665BE9BC02", "04718CC2D5CE7D541CF98439A5FA7163", "885D25B111BF2ABCCB9939F5B225A579");
            RequestConfiguration configuration =
                    new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);
            loadAds();


        });
    }

    public void loadAds() {
        StringBuilder sb = new StringBuilder();
        sb.append("Popup: " + BuildConfig.popup_ads_id + "\n");
        sb.append("Banner: " + BuildConfig.banner_ads_id + "\n");
        sb.append("Native: " + BuildConfig.native_ads_id + "\n");
        LogUtils.d("Admob Info: " + sb);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, BuildConfig.popup_ads_id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    }
                });

    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }

    public static PhotoApp getPhotoApp() {
        return sPhotoApp;
    }

    public Context getContext() {
        return sPhotoApp.getContext();
    }

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

}
