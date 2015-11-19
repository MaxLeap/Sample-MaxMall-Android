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

public class Comment {
    private int score;
    private String content;
    private User user;
    private Product product;

    public Comment(MLObject object) {
        this.score = object.getInt("score");
        this.content = object.getString("content");
        this.user = new User(object.getMLObject("user"));
        this.product = new Product(object.getMLObject("product"));
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "score=" + score +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", product=" + product +
                '}';
    }
}
