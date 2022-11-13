package com.burhanrashid52.imageeditor.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.burhanrashid52.imageeditor.PhotoApp;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.db.DBHelper;
import com.burhanrashid52.imageeditor.utils.PreferenceUtils;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Burhanuddin Rashid on 1/17/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    protected DBHelper dbHelper;

    protected void createAdView(final RelativeLayout parentView) {

    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/


    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void showSnackbar(@NonNull String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    protected int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    protected int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = PhotoApp.getDbHelper();
    }


    protected void logEvent(String action) {
        PhotoApp.getPhotoApp().getFirebaseAnalytics().logEvent(action, new Bundle());
    }

    protected void logEvent(String action, Bundle bundle) {
        PhotoApp.getPhotoApp().getFirebaseAnalytics().logEvent(action, bundle);
    }



    protected void showRateDialog() {
        if (!PreferenceUtils.isUserReviewed(this)) {
            new AlertDialog.Builder(this).setTitle("Rate app")
                    .setMessage("If you like or hate this app, let me know. Your feedback is the motivation for me to improve the app. Thank you.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PreferenceUtils.saveUserReviewed(BaseActivity.this, true);
                            String packageName = getPackageName();

                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                            }

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNeutralButton("Never", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceUtils.saveUserReviewed(BaseActivity.this, true);
                }
            }).show();
        }
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

}
