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
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class CartPreferenceUtil {

    public static final String DATA = "cart";
    public static final String KEY = "items";
    private static CartPreferenceUtil cartPreferenceUtil;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static Gson GSON = new Gson();

    private CartPreferenceUtil(Context context, String namePreferences, int mode) {
        this.context = context;

        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
    }

    public static CartPreferenceUtil getComplexPreferences(Context context,
                                                           String namePreferences, int mode) {

        if (cartPreferenceUtil == null) {
            cartPreferenceUtil = new CartPreferenceUtil(context,
                    namePreferences, mode);
        }

        return cartPreferenceUtil;
    }

    public void putObject(String key, Object object) {
        if(object == null){
            throw new IllegalArgumentException("object is null");
        }

        if(key.equals("") || key == null){
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.putString(key, GSON.toJson(object));
        commit();
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try{
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }
}