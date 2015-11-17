/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.maxleap.MaxLeap;

public class EBusinessApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MaxLeap.initialize(this, ApiKey.MAXLEAP_APP_ID, ApiKey.MAXLEAP_CLIENT_KEY);
        FlurryAgent.init(this, ApiKey.FLURRY_KEY);
    }
}
