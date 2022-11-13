package com.burhanrashid52.imageeditor.listquote;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.burhanrashid52.imageeditor.BuildConfig;
import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.EditImageActivity;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.ShareQuoteActivity;
import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.models.Quote;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.burhanrashid52.imageeditor.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 8/19/18.
 */

public class ListQuoteActivity extends BaseActivity {

    ListQuoteAdapter adapter;

    String category;
    ProgressBar pbLoading;
    RelativeLayout rlAds;
    TextView tvTitle;

    RecyclerView rvListQuote;
    InterstitialAd popup;
    Quote selectedQuote;
    List<Integer> listRes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTablet()) {
            makeFullScreen();
        }
        setContentView(R.layout.activity_list_quote_2);
        category = getIntent().getExtras().getString(Constant.KEY_CATEGORY, "Love");
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(category);
        rvListQuote = findViewById(R.id.rv_list_quote);
        rvListQuote.setLayoutManager(new LinearLayoutManager(this));
        pbLoading = findViewById(R.id.pb_loading);
        rlAds = findViewById(R.id.rl_ads);
        prepareQuoteData();
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeStatusBarColor(getResources().getColor(R.color.color_status_bar_list_quote));


        initAds();
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

    private void prepareQuoteData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Quote> listQuote = dbHelper.getListQuote(category);
                if (listQuote.isEmpty()) {
                    if (category.equalsIgnoreCase("Love")) {
                        for (String s : Constant.listLoveQuote) {
                            dbHelper.saveQuote(s, "Love", "");
                        }
                    } else if (category.equalsIgnoreCase("Motivation")) {
                        for (String s : Constant.listMotivationQuote) {
                            dbHelper.saveQuote(s, "Motivation", "");
                        }
                    } else if (category.equalsIgnoreCase("Family")) {
                        for (String s : Constant.listFamilyQuote) {
                            dbHelper.saveQuote(s, "Family", "");
                        }
                    } else if (category.equalsIgnoreCase("Life")) {
                        for (String s : Constant.listLifeQuote) {
                            dbHelper.saveQuote(s, "Life", "");
                        }
                    } else if (category.equalsIgnoreCase("Happiness")) {
                        for (String s : Constant.listHappinessQuote) {
                            dbHelper.saveQuote(s, "Happiness", "");
                        }
                    } else if (category.equalsIgnoreCase("Women")) {
                        for (String s : Constant.listWomenQuote) {
                            dbHelper.saveQuote(s, "Women", "");
                        }
                    } else if (category.equalsIgnoreCase("Mother")) {
                        for (String s : Constant.listMotherQuote) {
                            dbHelper.saveQuote(s, "Mother", "");
                        }
                    } else if (category.equalsIgnoreCase("Sad")) {
                        for (String s : Constant.listSadQuote) {
                            dbHelper.saveQuote(s, "Sad", "");
                        }
                    } else if (category.equalsIgnoreCase("Friendship")) {
                        for (String s : Constant.listFriendQuote) {
                            dbHelper.saveQuote(s, "Friendship", "");
                        }
                    } else if (category.equalsIgnoreCase("Father")) {
                        for (String s : Constant.listFatherQuote) {
                            dbHelper.saveQuote(s, "Father", "");
                        }
                    } else if (category.equalsIgnoreCase("Positive")) {
                        for (String s : Constant.listFatherQuote) {
                            dbHelper.saveQuote(s, "Positive", "");
                        }
                    } else if (category.equalsIgnoreCase("Alone")) {
                        for (String s : Constant.listAloneQuote) {
                            dbHelper.saveQuote(s, "Alone", "");
                        }
                    } else if (category.equalsIgnoreCase("Trust")) {
                        for (String s : Constant.listTrustQuote) {
                            dbHelper.saveQuote(s, "Trust", "");
                        }
                    } else if (category.equalsIgnoreCase("Funny")) {
                        for (String s : Constant.listFunnyQuote) {
                            dbHelper.saveQuote(s, "Funny", "");
                        }
                    }
                    listQuote.addAll(dbHelper.getListQuote(category));
                } else {
                    if (PreferenceUtils.needUpdateCategory(ListQuoteActivity.this, category)) {
                        //get sorted list quote
                        ArrayList<Quote> listChangeQuote = new ArrayList<>();
                        for (int i = listQuote.size() - 1; i >= listQuote.size() - 1 - PreferenceUtils.INTERVAL_QUOTES; i--) {
                            listChangeQuote.add(0, listQuote.get(i));
                        }
                        listQuote.removeAll(listChangeQuote);
                        listQuote.addAll(0, listChangeQuote);
                        //update in DB
                        dbHelper.clearCategory(category);
                        for (Quote quote : listQuote) {
                            dbHelper.saveQuote(quote.getContent(), category, "");
                        }
                    }
                }

                if (category.equalsIgnoreCase("Love")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listLoveQuote.size()) {
                        for (int i = Constant.listLoveQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listLoveQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listLoveQuote.get(i), category, "");
                            }
                        }
                    }
                } else if (category.equalsIgnoreCase("Motivation")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listMotivationQuote.size()) {
                        for (int i = Constant.listMotivationQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listMotivationQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listMotivationQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Family")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listFamilyQuote.size()) {
                        for (int i = Constant.listFamilyQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listFamilyQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listFamilyQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Life")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listLifeQuote.size()) {
                        for (int i = Constant.listLifeQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listLifeQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listLifeQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Happiness")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listHappinessQuote.size()) {
                        for (int i = Constant.listHappinessQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listHappinessQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listHappinessQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Women")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listWomenQuote.size()) {
                        for (int i = Constant.listWomenQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listWomenQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listWomenQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Mother")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listMotherQuote.size()) {
                        for (int i = Constant.listMotherQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listMotherQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listMotherQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Sad")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listSadQuote.size()) {
                        for (int i = Constant.listSadQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listSadQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listSadQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Friendship")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listFriendQuote.size()) {
                        for (int i = Constant.listFriendQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listFriendQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listFriendQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Father")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listFatherQuote.size()) {
                        for (int i = Constant.listFatherQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listFatherQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listFatherQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Positive")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listPositiveQuote.size()) {
                        for (int i = Constant.listPositiveQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listPositiveQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listPositiveQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Alone")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listAloneQuote.size()) {
                        for (int i = Constant.listAloneQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listAloneQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listAloneQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Trust")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listTrustQuote.size()) {
                        for (int i = Constant.listTrustQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listTrustQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listTrustQuote.get(i), category, "");
                            }

                        }
                    }
                } else if (category.equalsIgnoreCase("Funny")) {
                    if (dbHelper.getNumberQuoteCount(category) != Constant.listFunnyQuote.size()) {
                        for (int i = Constant.listFunnyQuote.size() - 1; i >= 0; i--) {
                            boolean exist = dbHelper.checkQuoteExist(new Quote(Constant.listFunnyQuote.get(i), category, ""), category);
                            if (exist) {
                                break;
                            } else {
                                dbHelper.saveQuote(Constant.listFunnyQuote.get(i), category, "");
                            }

                        }
                    }
                }

                listRes = new ArrayList<>();
                listRes.add(R.drawable.bg_01);
                listRes.add(R.drawable.bg_02);
                listRes.add(R.drawable.bg_03);
                listRes.add(R.drawable.bg_04);
                listRes.add(R.drawable.bg_05);
                listRes.add(R.drawable.bg_06);
                listRes.add(R.drawable.bg_07);
                listRes.add(R.drawable.bg_08);
                listRes.add(R.drawable.bg_09);
                listRes.add(R.drawable.bg_10);
                listRes.add(R.drawable.bg_11);
                listRes.add(R.drawable.bg_12);
                listRes.add(R.drawable.bg_13);
                listRes.add(R.drawable.bg_14);
                listRes.add(R.drawable.bg_15);
                listRes.add(R.drawable.bg_16);
                listRes.add(R.drawable.bg_17);
                listRes.add(R.drawable.bg_18);
                listRes.add(R.drawable.bg_19);
                listRes.add(R.drawable.bg_20);
                listRes.add(R.drawable.bg_21);
                listRes.add(R.drawable.bg_22);
                listRes.add(R.drawable.bg_23);
                listRes.add(R.drawable.bg_24);
                listRes.add(R.drawable.bg_25);
                listRes.add(R.drawable.bg_26);
                listRes.add(R.drawable.bg_27);
                listRes.add(R.drawable.bg_28);
                listRes.add(R.drawable.bg_29);
                listRes.add(R.drawable.bg_30);
                listRes.add(R.drawable.bg_31);
                listRes.add(R.drawable.bg_32);
                listRes.add(R.drawable.bg_33);
                listRes.add(R.drawable.bg_34);
                listRes.add(R.drawable.bg_35);
                listRes.add(R.drawable.bg_36);
                listRes.add(R.drawable.bg_37);
                listRes.add(R.drawable.bg_38);
                listRes.add(R.drawable.bg_39);
                listRes.add(R.drawable.bg_40);
                listRes.add(R.drawable.bg_41);
                listRes.add(R.drawable.bg_42);
                listRes.add(R.drawable.bg_43);
                listRes.add(R.drawable.bg_44);
                listRes.add(R.drawable.bg_45);
                listRes.add(R.drawable.bg_46);
                listRes.add(R.drawable.bg_47);
                listRes.add(R.drawable.bg_48);
                listRes.add(R.drawable.bg_49);
                listRes.add(R.drawable.bg_50);
                listRes.add(R.drawable.bg_51);
                listRes.add(R.drawable.bg_52);
                listRes.add(R.drawable.bg_53);
                listRes.add(R.drawable.bg_54);
                listRes.add(R.drawable.bg_55);
                listRes.add(R.drawable.bg_56);
                listRes.add(R.drawable.bg_57);
                listRes.add(R.drawable.bg_58);
                listRes.add(R.drawable.bg_59);
                listRes.add(R.drawable.bg_60);
                listRes.add(R.drawable.bg_61);
                listRes.add(R.drawable.bg_62);
                listRes.add(R.drawable.bg_63);
                listRes.add(R.drawable.bg_64);
                listRes.add(R.drawable.bg_65);
                listRes.add(R.drawable.bg_66);
                listRes.add(R.drawable.bg_67);
                listRes.add(R.drawable.bg_68);
                listRes.add(R.drawable.bg_69);
                listRes.add(R.drawable.bg_70);
                listRes.add(R.drawable.bg_71);
                listRes.add(R.drawable.bg_72);
                listRes.add(R.drawable.bg_73);
                listRes.add(R.drawable.bg_74);
                listRes.add(R.drawable.bg_75);
                listRes.add(R.drawable.bg_76);
                listRes.add(R.drawable.bg_77);
                listRes.add(R.drawable.bg_78);
                listRes.add(R.drawable.bg_79);
                listRes.add(R.drawable.bg_80);
                listRes.add(R.drawable.bg_81);
                listRes.add(R.drawable.bg_82);
                listRes.add(R.drawable.bg_83);
                listRes.add(R.drawable.bg_84);
                listRes.add(R.drawable.bg_85);
                listRes.add(R.drawable.bg_86);

                Collections.shuffle(listRes);
                for (int i = 0; i < listQuote.size(); i++) {
                    listQuote.get(i).setRef(listRes.get(i % listRes.size()));
                }
                ArrayList<Object> listObject = new ArrayList<>();
                listObject.addAll(listQuote);

                if (Constant.listNativeAds.size() > 0
                        && PreferenceUtils.isCanShowAds(ListQuoteActivity.this)
                        && PreferenceUtils.canShowNativeAdsInListQuote(ListQuoteActivity.this)) {
                    long itemPerNativeAds = PreferenceUtils.getItemPerNativeAds(ListQuoteActivity.this);
                    long indexStartNativeAds = PreferenceUtils.getItemStartNativeAds(ListQuoteActivity.this);
                    int numberItemAdded = 0;

                    for (int i = (int) indexStartNativeAds; i <= listObject.size(); i += itemPerNativeAds) {
                        listObject.add(i, Constant.listNativeAds.get(numberItemAdded % Constant.listNativeAds.size()));
                        numberItemAdded++;
                    }
                }


                adapter = new ListQuoteAdapter(listObject);
                adapter.setQuoteSelectListener(new ListQuoteAdapter.QuoteSelectListener() {
                    @Override
                    public void onQuoteSelect(Quote quote) {

                        //log event
                        Bundle bundleLog = new Bundle();
                        bundleLog.putString("category_name", category);
                        logEvent("Action_Edit_Quote", bundleLog);

                        int viewQuoteCount = PreferenceUtils.getNumberViewQuote(ListQuoteActivity.this);
                        if (Constant.showAds(viewQuoteCount, PreferenceUtils.getAdsIntervalValue(ListQuoteActivity.this, "ACTION_QUOTE_SELECT")) && popup!=null && PreferenceUtils.isCanShowAds(ListQuoteActivity.this)) {
                            selectedQuote = quote;

                            popup.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    initAds();
                                    PreferenceUtils.saveNumberViewQuote(ListQuoteActivity.this, PreferenceUtils.getNumberViewQuote(ListQuoteActivity.this) + 1);
                                    Intent intent = new Intent(ListQuoteActivity.this, EditImageActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constant.KEY_PRE_TEXT, selectedQuote.getContent());
                                    bundle.putInt(Constant.KEY_PRE_SOURCE, selectedQuote.getRef());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            popup.show(ListQuoteActivity.this);
                        } else {
                            PreferenceUtils.saveNumberViewQuote(ListQuoteActivity.this, viewQuoteCount + 1);
                            Intent intent = new Intent(ListQuoteActivity.this, EditImageActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.KEY_PRE_TEXT, quote.getContent());
                            bundle.putInt(Constant.KEY_PRE_SOURCE, quote.getRef());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onQuoteCopy(Quote quote) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(quote.getContent());
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Text", quote.getContent());
                            clipboard.setPrimaryClip(clip);
                        }
                        Toast.makeText(ListQuoteActivity.this, "Quote is copied to your clipboard", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onQuoutShare(Quote quote) {
                        Intent intent = new Intent(ListQuoteActivity.this, ShareQuoteActivity.class);
                        intent.putExtra(Constant.KEY_RES_ID, quote.getRef());
                        intent.putExtra(Constant.KEY_QUOTE, quote.getContent());
                        startActivity(intent);
                    }
                });


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferenceUtils.saveLastTimeViewCategory(ListQuoteActivity.this, category);
                        if (rvListQuote == null) {
                            rvListQuote = findViewById(R.id.rv_list_quote);
                            rvListQuote.setLayoutManager(new LinearLayoutManager(ListQuoteActivity.this));
                        }
                        rvListQuote.setAdapter(adapter);
                        if (pbLoading == null) {
                            pbLoading = findViewById(R.id.pb_loading);
                        }
                        pbLoading.setVisibility(View.GONE);


                    }
                });

            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rvListQuote = null;
    }
}
