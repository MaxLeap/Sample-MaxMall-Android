/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.maxleap.ebusiness.BuildConfig;

public class FFLog {

    public static void i(String log) {
        if (BuildConfig.LOG_DEBUG) {
            Log.i("FFLog", "======== " + log);
        }
    }

    public static void d(String log) {
        if (BuildConfig.LOG_DEBUG) {
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
