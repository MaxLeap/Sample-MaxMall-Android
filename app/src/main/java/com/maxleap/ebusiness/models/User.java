/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.models;


import com.maxleap.MLObject;
import com.maxleap.MLRelation;

public class User {
    private String id;
    private String username;
    private String icon;
    private String nickname;
    private MLRelation favorites;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public MLRelation getFavorites() {
        return favorites;
    }

    public void setFavorites(MLRelation favorites) {
        this.favorites = favorites;
    }

    public static User from(MLObject object) {
        User user = new User();
        user.setId(object.getObjectId());
        user.setUsername(object.getString("username"));
        user.setIcon(object.getString("icon"));
        user.setNickname(object.getString("nickname"));
        user.setFavorites(object.getRelation("favorites"));
        return user;
    }
}
