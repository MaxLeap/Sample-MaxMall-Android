package com.maxleap.ebusiness.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by samzhao on 15/9/29.
 */
public class FFLog {
    private static final boolean DEBUG = true;

    public static void d(String log) {
        if (DEBUG) {
            Log.d("FFLog", "======== " + log);
        }
    }

    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
