package com.burhanrashid52.imageeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.listcate.ListCateActivity;
import com.burhanrashid52.imageeditor.mygallery.MyGalleryActivity;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.burhanrashid52.imageeditor.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.Arrays;


public class MenuActivity extends BaseActivity implements View.OnClickListener {

    InterstitialAd popup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        /*initAds();
        loadNativeAds();*/
        getConfig();
        setContentView(R.layout.activity_menu);
        PreferenceUtils.saveLaunchAppCount(this);
        findViewById(R.id.bt_best_quotes).setOnClickListener(this);
        findViewById(R.id.bt_create_quotes).setOnClickListener(this);
        findViewById(R.id.bt_get_idea).setOnClickListener(this);

        if (Constant.screenWidth == 0) {
            Constant.screenWidth = getScreenWidth();
        }
        if (Constant.screenHeight == 0) {
            Constant.screenHeight = getScreenHeight();
        }

        long launchAppCount = PreferenceUtils.getLaunchAppCount(this);
        long launchAppCountToShowRate = PreferenceUtils.getLaunchAppCountToShowRate(this);
        if(launchAppCountToShowRate == 0) {
            launchAppCountToShowRate = 3;
        }
        LogUtils.d("launchAppCount = " + launchAppCount + " ; launchAppCountToShowRate = " + launchAppCountToShowRate);

        if (launchAppCount > 0 && launchAppCount % launchAppCountToShowRate == 0) {
            showRateDialog();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkFirebaseDynamicLinks();
    }

    private void checkFirebaseDynamicLinks() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        StringBuilder dataBuilder = new StringBuilder();
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {

                            LogUtils.d("deepLink "+pendingDynamicLinkData.toString());

                            deepLink = pendingDynamicLinkData.getLink();
                            dataBuilder.append("Link: "+deepLink.toString());
                            if (pendingDynamicLinkData.getUtmParameters() != null) {
                                dataBuilder.append("\n\n");
                                Bundle bundle = pendingDynamicLinkData.getUtmParameters();
                                dataBuilder.append("Campaign Information\n");
                                String utmCampaign = bundle.getString("utm_campaign");
                                dataBuilder.append("UTM Campaign: "+utmCampaign +"\n");
                                dataBuilder.append("UTM Medium: "+bundle.getString(
                                        "utm_medium") +"\n");
                                dataBuilder.append("UTM Source: "+bundle.getString(
                                        "utm_source") +"\n");

                            }

                            if (pendingDynamicLinkData.getExtensions()!=null) {
                                Bundle extension = pendingDynamicLinkData.getExtensions();
                                Bundle scionData = extension.getBundle("scionData");
                                if (scionData != null) {
                                    Bundle dynamicLinkAppOpen = scionData.getBundle(
                                            "dynamic_link_app_open");
                                    if (dynamicLinkAppOpen != null) {
                                        String medium = dynamicLinkAppOpen.getString("medium");
                                        String source = dynamicLinkAppOpen.getString("source");
                                        String campaign = dynamicLinkAppOpen.getString("campaign");
                                        dataBuilder.append("\n\n");
                                        dataBuilder.append("medium = "+medium +" ; source = "+ source +" ; campaign = "+ campaign);
                                    }
                                }
                            }

                            Toast.makeText(MenuActivity.this, dataBuilder.toString(),
                                    Toast.LENGTH_LONG).show();

                        } else {
                            LogUtils.d("deepLink null");
                            Toast.makeText(MenuActivity.this, "No Deeplink",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void initAds() {
        //inter
        if (!PreferenceUtils.isCanShowAds(this)) {
            return;
        }
        popup = null;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, BuildConfig.popup_ads_id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        popup = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        popup = null;
                    }
                });


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_best_quotes:
                intent = new Intent(this, ListCateActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_create_quotes:
                int createQuoteCount = PreferenceUtils.getNumberCreateQuote(MenuActivity.this);
                if (Constant.showAds(createQuoteCount, PreferenceUtils.getAdsIntervalValue(this, "ACTION_QUOTE_CREATE")) && popup!=null && PreferenceUtils.isCanShowAds(MenuActivity.this)) {
                    popup.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            initAds();
                            PreferenceUtils.saveNumberCreateQuote(MenuActivity.this, PreferenceUtils.getNumberCreateQuote(MenuActivity.this) + 1);
                            Intent intent = new Intent(MenuActivity.this, EditImageActivity.class);
                            startActivity(intent);
                        }
                    });
                    popup.show(MenuActivity.this);


                } else {
                    PreferenceUtils.saveNumberCreateQuote(MenuActivity.this, createQuoteCount + 1);
                    intent = new Intent(this, EditImageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.bt_get_idea:
                intent = new Intent(this, MyGalleryActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getConfig() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetch(1).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRemoteConfig.activate();
                    boolean enableAds = firebaseRemoteConfig.getBoolean("enable_ads");
                    long viewCateAdsInterval = firebaseRemoteConfig.getLong("ACTION_QUOTE_VIEW_CATEGORY");
                    long createQuoteAdsInterval = firebaseRemoteConfig.getLong("ACTION_QUOTE_CREATE");
                    long shareQuoteAdsInterval = firebaseRemoteConfig.getLong("ACTION_QUOTE_SHARE");
                    long selectQuoteAdsInterval = firebaseRemoteConfig.getLong("ACTION_QUOTE_SELECT");
                    long itemPerNativeAds = firebaseRemoteConfig.getLong("item_per_native_ads");
                    long indexStartNativeAds = firebaseRemoteConfig.getLong("index_start_native_ads");
                    String firstAdsPriority = firebaseRemoteConfig.getString("ads_type_priority");
                    boolean canShowNativeAdsInList = firebaseRemoteConfig.getBoolean("can_show_native_ads_list_quote");
                    boolean canShowNativeBannerInEditScreen = firebaseRemoteConfig.getBoolean("can_show_banner_native_edit_screen");
                    long launchAppCountToShowRate = firebaseRemoteConfig.getLong("launch_app_count_to_show_rate");

                    ArrayList<String> keys = new ArrayList<>(Arrays.asList("ACTION_QUOTE_VIEW_CATEGORY", "ACTION_QUOTE_CREATE", "ACTION_QUOTE_SHARE", "ACTION_QUOTE_SELECT"));
                    ArrayList<Long> values = new ArrayList<>(Arrays.asList(viewCateAdsInterval, createQuoteAdsInterval, shareQuoteAdsInterval, selectQuoteAdsInterval));
                    PreferenceUtils.saveCanShowAds(MenuActivity.this, enableAds, keys, values, itemPerNativeAds, indexStartNativeAds, firstAdsPriority, canShowNativeAdsInList, canShowNativeBannerInEditScreen);
                    PreferenceUtils.saveLaunchAppCountToShowRate(MenuActivity.this, launchAppCountToShowRate);
                    LogUtils.d("EnableAds = " + enableAds + " ; viewCateAdsInterval = " + viewCateAdsInterval + " ; createQuoteAdsInterval = " + createQuoteAdsInterval + " ; shareQuoteAdsInterval = " + shareQuoteAdsInterval + " ; selectQuoteAdsInterval = " + selectQuoteAdsInterval + " ; itemPerNativeAds = " + itemPerNativeAds + " ; indexStartNativeAds = " + indexStartNativeAds + " ; firstAdsPriority = " + firstAdsPriority + " ; canShowNativeAdsInList = " + canShowNativeAdsInList + " ; canShowNativeBannerInEditScreen = " + canShowNativeBannerInEditScreen + " ; launchAppCountToShowRate = " + launchAppCountToShowRate);
                    logEvent("Fetch_Config_Success");
                } else {
                    logEvent("Fetch_Config_Failed");
                }
            }
        });

    }

    AdLoader adLoader;

    protected void loadNativeAds() {
        AdLoader adLoader = new AdLoader.Builder(this, BuildConfig.native_ads_id)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        Constant.listNativeAds.add(NativeAd);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAds(new AdRequest.Builder().build(), 2);
    }

}
