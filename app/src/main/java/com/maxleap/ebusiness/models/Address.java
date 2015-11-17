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

import java.io.Serializable;


public class Address implements Serializable {
    private String id;
    private User user;
    private String name;
    private String street;
    private String tel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public static Address from(MLObject object) {
        Address address = new Address();
        address.setId(object.getObjectId());
        User user = User.from(object.getMLObject("user"));
        address.setUser(user);
        address.setName(object.getString("name"));
        address.setTel(object.getString("tel"));
        address.setStreet(object.getString("street"));
        return address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
