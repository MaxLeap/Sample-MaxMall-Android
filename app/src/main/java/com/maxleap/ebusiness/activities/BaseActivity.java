/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.maxleap.MLAnalytics;
import com.maxleap.ebusiness.ApiKey;

import net.hockeyapp.android.CrashManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLAnalytics.onResume(this);
        CrashManager.register(this, ApiKey.HOCKEY_APP_ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLAnalytics.onPause(this);
    }
}
