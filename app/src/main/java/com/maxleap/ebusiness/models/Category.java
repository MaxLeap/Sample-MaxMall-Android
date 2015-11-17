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

import java.io.Serializable;

public class Category implements Serializable {

    private String id;
    private String title;
    private String icon;
    private MLRelation products;
    private boolean recommend;
    private boolean onSales;

    public Category(MLObject object) {
        this.title = object.getString("title");
        this.recommend = object.getBoolean("recommend");
        this.onSales = object.getBoolean("on_sales");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isOnSales() {
        return onSales;
    }

    public void setOnSales(boolean onSales) {
        this.onSales = onSales;
    }

    public MLRelation getProduct() {
        return product;
    }

    public void setProduct(MLRelation product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", recommend=" + recommend +
                ", onSales=" + onSales +
                '}';
    }
}