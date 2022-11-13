package com.burhanrashid52.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.burhanrashid52.imageeditor.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.io.File;
import java.util.Random;

public class ShareQuoteActivity extends BaseActivity implements View.OnClickListener {

    String quote;
    int resId;
    String imagePath;
    boolean shareNow;

    ImageView ivQuote;
    TextView tvQuote;
    RelativeLayout rlQuote;

    InterstitialAd popup;
    LinearLayoutCompat adViewContainer;
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_quote);
        changeStatusBarColor(getResources().getColor(R.color.color_status_bar_share));
        //changeStatusBarColor(getResources().getColor(R.color.black));
        quote = getIntent().getStringExtra(Constant.KEY_QUOTE);
        if (quote == null) {
            quote = "";
        }
        resId = getIntent().getIntExtra(Constant.KEY_RES_ID, R.drawable.bg_01);
        imagePath = getIntent().getStringExtra(Constant.KEY_IMAGE_PATH);
        if (imagePath == null) {
            imagePath = "";
        }
        shareNow = getIntent().getBooleanExtra(Constant.KEY_SHARE_NOW, false);
        tvQuote = findViewById(R.id.tvQuote);
        ivQuote = findViewById(R.id.ivQuote);
        rlQuote = findViewById(R.id.rlQuote);

        int id = Constant.getResourceFontId("13");
        tvQuote.setTypeface(ResourcesCompat.getFont(this, id));

        if (imagePath.isEmpty()) {
            Glide.with(this).load(resId).apply(Constant.glideRequestOption).into(ivQuote);
            tvQuote.setText(quote);
            //share();
            //countDownTimerShare.start();
            Toast.makeText(this, getString(R.string.share_loading), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    share();
                }
            }, 1500);
        } else {
            tvQuote.setVisibility(View.GONE);
            findViewById(R.id.v_layer).setVisibility(View.GONE);
            Glide.with(this).load(new File(imagePath)).apply(Constant.glideRequestOption).into(ivQuote);
            if (shareNow) {
                Toast.makeText(this, getString(R.string.share_loading), Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        share();
                    }
                }, 1500);
            }
        }

        findViewById(R.id.btBack).setOnClickListener(this);
        findViewById(R.id.btShare).setOnClickListener(this);
        initAds();
        initBannerAds();
        long launchAppCount = PreferenceUtils.getLaunchAppCount(this);
        long launchAppCountToShowRate = PreferenceUtils.getLaunchAppCountToShowRate(this);
        if (launchAppCountToShowRate == 0) {
            launchAppCountToShowRate = 3;
        }
        LogUtils.d("launchAppCount = " + launchAppCount + " ; launchAppCountToShowRate = " + launchAppCountToShowRate);
        if (launchAppCount > 0 && launchAppCount % launchAppCountToShowRate == 0) {
            showRateDialog();
        }
    }

    private void initBannerAds() {
        if (!PreferenceUtils.isCanShowAds(this)) {
            return;
        }
        adViewContainer = findViewById(R.id.adViewContainer);
        adView = new AdView(this);
        adView.setAdSize(AdSize.LARGE_BANNER);
        adView.setAdUnitId(BuildConfig.banner_ads_id);
        adView.loadAd(new AdRequest.Builder().build());
        adViewContainer.addView(adView);
    }

    private void initAds() {
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

    private void share() {

        rlQuote.post(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<Integer, Void, Bitmap>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
                        if (bitmapPath == null) {
                            Toast.makeText(ShareQuoteActivity.this, "Sorry, please use share button at the top of screen :(", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Uri bitmapUri = Uri.parse(bitmapPath);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        startActivity(Intent.createChooser(intent, "Share"));
                    }

                    @Override
                    protected Bitmap doInBackground(Integer... size) {
                        Bitmap returnedBitmap = Bitmap.createBitmap(size[0], size[1], Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(returnedBitmap);
                        Drawable bgDrawable = rlQuote.getBackground();
                        if (bgDrawable != null)
                            bgDrawable.draw(canvas);
                        else
                            canvas.drawColor(Color.WHITE);
                        rlQuote.draw(canvas);
                        return returnedBitmap;

                    }
                }.execute(rlQuote.getWidth(), rlQuote.getHeight());

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btBack) {
            onBackPressed();
        } else if (v.getId() == R.id.btShare) {

            int numberShareCount = PreferenceUtils.getNumberShareQuote(this);
            if (Constant.showAds(numberShareCount, PreferenceUtils.getAdsIntervalValue(this, "ACTION_QUOTE_SHARE")) && popup!=null && PreferenceUtils.isCanShowAds(ShareQuoteActivity.this)) {

                popup.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        initAds();
                        PreferenceUtils.saveNumberShareQuote(ShareQuoteActivity.this, PreferenceUtils.getNumberShareQuote(ShareQuoteActivity.this) + 1);
                        share();
                    }
                });
                popup.show(ShareQuoteActivity.this);
            } else {
                PreferenceUtils.saveNumberShareQuote(ShareQuoteActivity.this, numberShareCount + 1);
                share();
            }
        }
    }


}
