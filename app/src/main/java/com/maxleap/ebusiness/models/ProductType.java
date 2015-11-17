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

public class ProductType {
    private String title;
    private String icon;
    private MLRelation products;
    private boolean recommend;
    private boolean on_sales;

    public ProductType(MLObject object) {
        this.setTitle(object.getString("title"));
        this.setIcon(object.getString("icon"));
        this.setProducts(object.getRelation("products"));
        this.setRecommend(object.getBoolean("recommend"));
        this.setOn_sales(object.getBoolean("on_sales"));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isOn_sales() {
        return on_sales;
    }

    public void setOn_sales(boolean on_sales) {
        this.on_sales = on_sales;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public MLRelation getProducts() {
        return products;
    }

    public void setProducts(MLRelation products) {
        this.products = products;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", products=" + products +
                ", recommend=" + recommend +
                ", on_sales=" + on_sales +
                '}';
    }
}
