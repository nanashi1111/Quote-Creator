package com.burhanrashid52.imageeditor.listcate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.burhanrashid52.imageeditor.BuildConfig;
import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.listquote.ListQuoteActivity;
import com.burhanrashid52.imageeditor.models.Category;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.burhanrashid52.imageeditor.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;

/**
 * Created by admin on 8/18/18.
 */

public class ListCateActivity extends BaseActivity {

    RecyclerView rvListCategory;
    ProgressBar pbLoading;
    RelativeLayout rlAds;

    InterstitialAd popup;
    Category selectedCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTablet()) {
            makeFullScreen();
        }
        setContentView(R.layout.activity_list_cate);
        rvListCategory = findViewById(R.id.rv_list_cate);
        pbLoading = findViewById(R.id.pb_loading);
        rlAds = findViewById(R.id.rl_ads);
        //rvListCategory.setLayoutManager(new GridLayoutManager(this, 2));
        rvListCategory.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        prepareListCate();
        changeStatusBarColor(getResources().getColor(R.color.color_status_bar_list_quote));
        initAds();
    }

    private void initAds() {
        //createAdView(rlAds);
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

    private void prepareListCate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Category> listCategory = new ArrayList<>();
                listCategory.add(new Category(R.drawable.bg_category_love, "Love"));
                listCategory.add(new Category(R.drawable.bg_category_friendship, "Friendship"));
                listCategory.add(new Category(R.drawable.bg_category_motivation, "Motivation"));
                listCategory.add(new Category(R.drawable.bg_category_family, "Family"));
                listCategory.add(new Category(R.drawable.bg_category_life, "Life"));
                listCategory.add(new Category(R.drawable.bg_category_positive, "Positive"));
                listCategory.add(new Category(R.drawable.bg_category_happiness, "Happiness"));
                listCategory.add(new Category(R.drawable.bg_category_father, "Father"));
                listCategory.add(new Category(R.drawable.bg_category_mother, "Mother"));
                listCategory.add(new Category(R.drawable.bg_category_trust, "Trust"));
                listCategory.add(new Category(R.drawable.bg_category_alone, "Alone"));
                listCategory.add(new Category(R.drawable.bg_category_sad, "Sad"));
                listCategory.add(new Category(R.drawable.bg_category_funny, "Funny"));
                listCategory.add(new Category(R.drawable.bg_category_woman, "Women"));

                ArrayList<String> listQuoteOfCategory = new ArrayList<>();
                for (int i = 0; i < listCategory.size(); i++) {
                    //listQuoteOfCategory.add(dbHelper.getQuoteOfCategory(listCategory.get(i).getName()));
                    listQuoteOfCategory.add(Constant.getQuoteOfCategory(listCategory.get(i).getName()));
                }

                final ListCateStyleListAdapter listCateAdapter = new ListCateStyleListAdapter(listCategory, listQuoteOfCategory);
                listCateAdapter.setOnCateSelectListener(new ListCateAdapter.OnCateSelectListener() {
                    @Override
                    public void onCateSelected(Category category) {
                        Bundle bundleLog = new Bundle();
                        bundleLog.putString("category_name", category.getName());
                        logEvent("Action_View_Category_Detail", bundleLog);

                        int viewCategoryCount = PreferenceUtils.getNumberViewCategory(ListCateActivity.this);
                        if (Constant.showAds(viewCategoryCount, PreferenceUtils.getAdsIntervalValue(ListCateActivity.this, "ACTION_QUOTE_VIEW_CATEGORY"))
                                && popup!=null
                                && PreferenceUtils.isCanShowAds(ListCateActivity.this)) {
                            selectedCategory = category;

                            popup.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    initAds();
                                    PreferenceUtils.saveNumberViewCategory(ListCateActivity.this, PreferenceUtils.getNumberViewCategory(ListCateActivity.this) + 1);
                                    Intent intent = new Intent(ListCateActivity.this, ListQuoteActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constant.KEY_CATEGORY, selectedCategory.getName());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            popup.show(ListCateActivity.this);
                        } else {
                            PreferenceUtils.saveNumberViewCategory(ListCateActivity.this, viewCategoryCount + 1);
                            Intent intent = new Intent(ListCateActivity.this, ListQuoteActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.KEY_CATEGORY, category.getName());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        /*Intent intent = new Intent(ListCateActivity.this, ListQuoteActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_CATEGORY, category.getName());
                        intent.putExtras(bundle);
                        startActivity(intent);*/
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvListCategory.setAdapter(listCateAdapter);
                        pbLoading.setVisibility(View.GONE);
                    }
                });

            }
        }).start();


    }
}
