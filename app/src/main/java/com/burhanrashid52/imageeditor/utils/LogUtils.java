package com.burhanrashid52.imageeditor.utils;


import android.util.Log;
import org.json.JSONObject;


public class LogUtils {

    private static final boolean LOG = true;
    private static final String TAG = "Quoter";

    /**
     * Log debug
     *
     * @param message message to log
     */
    public static void d(String message) {
        if (LOG) {
            Log.d(TAG, message);
        }
    }

    /**
     * Log info
     *
     * @param message message to log
     */
    public static void i(String message) {
        if (LOG) {
            Log.i(TAG, message);
        }
    }

    /**
     * Log error
     *
     * @param message message to log
     */
    public static void e(String message) {
        if (LOG) {
            Log.e(TAG, message);
        }
    }

    /**
     * Log warning
     *
     * @param message message to log
     */
    public static void w(String message) {
        if (LOG) {
            Log.w(TAG, message);
        }
    }

    /**
     * Log json object data
     *
     * @param description description for json data
     * @param jsonObject  object to log
     */
    public static void json(String description, JSONObject jsonObject) {
        if (LOG) {
            Log.d(TAG, description + ": " + jsonObject.toString());
        }
    }
}
