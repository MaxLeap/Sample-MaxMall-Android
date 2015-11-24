/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.models;

import java.util.ArrayList;
import java.util.List;

public class CartList {

    private List<ProductData> list;

    public CartList() {
        list = new ArrayList<>();
    }

    public List<ProductData> getList() {
        return list;
    }

    public void setList(List<ProductData> list) {
        this.list = list;
    }
}