/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.models;

import org.json.JSONObject;

import java.util.List;

public class Product {
    private String id;
    private String title;
    private List<String> icons;
    private int price;
    private int original_price;
    private String intro;
    private List<String> services;
    private JSONObject info;
    private JSONObject custom_info1;
    private JSONObject custom_info2;
    private JSONObject custom_info3;
    private JSONObject detail;

    public Product() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIcons() {
        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public JSONObject getInfo() {
        return info;
    }

    public void setInfo(JSONObject info) {
        this.info = info;
    }

    public JSONObject getCustom_info1() {
        return custom_info1;
    }

    public void setCustom_info1(JSONObject custom_info1) {
        this.custom_info1 = custom_info1;
    }

    public JSONObject getCustom_info2() {
        return custom_info2;
    }

    public void setCustom_info2(JSONObject custom_info2) {
        this.custom_info2 = custom_info2;
    }

    public JSONObject getCustom_info3() {
        return custom_info3;
    }

    public void setCustom_info3(JSONObject custom_info3) {
        this.custom_info3 = custom_info3;
    }

    public JSONObject getDetail() {
        return detail;
    }

    public void setDetail(JSONObject detail) {
        this.detail = detail;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", icons=" + icons +
                ", price=" + price +
                ", original_price=" + original_price +
                ", intro='" + intro + '\'' +
                ", services=" + services +
                ", info=" + info +
                ", custom_info1=" + custom_info1 +
                ", custom_info2=" + custom_info2 +
                ", custom_info3=" + custom_info3 +
                ", detail=" + detail +
                '}';
    }
}
