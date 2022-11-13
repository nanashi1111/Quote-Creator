package com.burhanrashid52.imageeditor.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.models.Category;

import java.util.List;

/**
 * Created by admin on 8/29/18.
 */

public class PreferenceUtils {

    private static final String PREF_NAME = "Quoter_PREF";

    //so thoi gian toi thieu de tang 1 lan view category
    //public static final long INTERVAL_TIME = 180 * 1000L;
    public static final long INTERVAL_TIME = 180 * 1000L;
    //so quote lay tu duoi chen len dau
    public static final int INTERVAL_QUOTES = 4;

    private static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    //thoi gian gan nhat xem category
    public static long getLastTimeViewCategory(Context context, String category) {
        return getSharePreference(context).getLong("last_time_view_category_" + category, 0);
    }

    public static void saveLastTimeViewCategory(Context context, String category) {
        LogUtils.d("saveLastTimeViewCategory " + category + ": " + System.currentTimeMillis());
        getSharePreference(context).edit().putLong("last_time_view_category_" + category, System.currentTimeMillis()).commit();
    }

    public static boolean needUpdateCategory(Context context, String category) {
        LogUtils.d("needUpdateCategory " + category + " current: " + System.currentTimeMillis() + " ; lastTime = " + getLastTimeViewCategory(context, category) + " ; result = " + (System.currentTimeMillis() - getLastTimeViewCategory(context, category) - INTERVAL_TIME));
        return (System.currentTimeMillis() > getLastTimeViewCategory(context, category) + INTERVAL_TIME);
    }

    public static int getCachedNumberQuoteBackground(Context context) {
        return getSharePreference(context).getInt("number_bg", 25);
    }

    public static void saveNumberQuoteBackground(Context context, long number) {
        getSharePreference(context).edit().putInt("number_bg", (int) number).commit();
    }

    public static void saveNumberViewCategory(Context context, int number) {
        getSharePreference(context).edit().putInt("view_cate_count", number).commit();
    }

    public static int getNumberViewCategory(Context context) {
        return getSharePreference(context).getInt("view_cate_count", 0);
    }

    public static void saveNumberViewQuote(Context context, int number) {
        getSharePreference(context).edit().putInt("view_quote_count", number).commit();
    }

    public static int getNumberViewQuote(Context context) {
        return getSharePreference(context).getInt("view_quote_count", 0);
    }

    public static void saveNumberShareQuote(Context context, int number) {
        getSharePreference(context).edit().putInt("share_quote_count", number).commit();
    }

    public static int getNumberShareQuote(Context context) {
        return getSharePreference(context).getInt("share_quote_count", 0);
    }

    public static void saveNumberSaveQuote(Context context, int number) {
        getSharePreference(context).edit().putInt("save_quote_count", number).commit();
    }

    public static int getNumberSaveQuote(Context context) {
        return getSharePreference(context).getInt("save_quote_count", 0);
    }

    public static void saveNumberCreateQuote(Context context, int number) {
        getSharePreference(context).edit().putInt("create_quote_count", number).commit();
    }

    public static int getNumberCreateQuote(Context context) {
        return getSharePreference(context).getInt("create_quote_count", 0);
    }

    public static void saveUserReviewed(Context context, boolean reviewed) {
        getSharePreference(context).edit().putBoolean("save_reviewed", reviewed).commit();
    }

    public static boolean isUserReviewed(Context context) {
        return getSharePreference(context).getBoolean("save_reviewed", false);
    }

    public static void saveLaunchAppCountToShowRate(Context context, long count) {
        getSharePreference(context).edit().putLong("launch_count_to_show_rate", count).commit();
    }

    public static long getLaunchAppCountToShowRate(Context context) {
        return getSharePreference(context).getLong("launch_count_to_show_rate", 4L);
    }

    public static long getLaunchAppCount(Context context) {
        return getSharePreference(context).getLong("launch_app_count", 0L);
    }

    public static void saveLaunchAppCount(Context context) {
        getSharePreference(context).edit().putLong("launch_app_count", 1 + getLaunchAppCount(context)).commit();
    }

    public static void saveCanShowAds(Context context
            , boolean enable
            , List<String> keys
            , List<Long> values
            , long itemPerNativeAds
            , long indexStartNativeAds
            , String adsTypePriority
            , boolean nativeAdsInList
            , boolean nativeBannerInEditScreen) {
        SharedPreferences.Editor edit = getSharePreference(context).edit();
        edit.putBoolean("show_ads", enable);
        for (int i = 0; i < keys.size(); i++) {
            edit.putLong(keys.get(i), values.get(i));
        }
        edit.putLong("item_per_native_ads", itemPerNativeAds);
        edit.putLong("index_start_native_ads", indexStartNativeAds);
        //use banner or native at editing screen
        edit.putString("ads_priority", adsTypePriority);
        edit.putBoolean("native_in_list", nativeAdsInList);
        edit.putBoolean("ads_in_edit_screen", nativeBannerInEditScreen);

        edit.commit();
    }


    public static boolean canShowNativeAdsInListQuote(Context context) {
        return getSharePreference(context).getBoolean("native_in_list", true);
    }

    public static boolean canShowBannerNativeAdsInEditingScreen(Context context) {
        return getSharePreference(context).getBoolean("ads_in_edit_screen", true);
        //return true;
    }


    public static boolean isCanShowAds(Context context) {
        //return getSharePreference(context).getBoolean("show_ads", true);
        return true;
    }


    public static long getAdsIntervalValue(Context context, String key) {
        long defaultValue = 2;
        if (key.equals("ACTION_QUOTE_CREATE")) {
            defaultValue = 2;
        } else if (key.equals("ACTION_QUOTE_VIEW_CATEGORY")) {
            defaultValue = 4;
        } else if (key.equals("ACTION_QUOTE_SHARE")) {
            defaultValue = 2;
        } else if (key.equals("ACTION_QUOTE_SELECT")) {
            defaultValue = 2;
        }
        return getSharePreference(context).getLong(key, defaultValue);
    }


    public static long getItemPerNativeAds(Context context) {
        return getSharePreference(context).getLong("item_per_native_ads", 4);
    }


    public static long getItemStartNativeAds(Context context) {
        return getSharePreference(context).getLong("index_start_native_ads", 2);
    }

    public static void saveEnableShowBannerAds(Context context, boolean b) {
        getSharePreference(context).edit().putBoolean("show_banner", b).commit();
    }


    public static boolean isEnableShowBannerAds(Context context) {
        return getSharePreference(context).getBoolean("show_banner", true);
        //return true;
    }

    public static String getFirstAdsTypePriority(Context context) {
        return getSharePreference(context).getString("ads_priority", "native");
    }


}
