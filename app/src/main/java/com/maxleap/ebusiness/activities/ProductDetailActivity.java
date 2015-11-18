/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.ProductGalleryAdapter;
import com.maxleap.ebusiness.databinding.ActivityProductDetailBinding;
import com.maxleap.ebusiness.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends BaseActivity {

    public final static String PROD = "product";

    private ActivityProductDetailBinding mBinding;
    private ProductGalleryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        initViews();
        Product product = (Product) getIntent().getSerializableExtra(PROD);
        mBinding.setProduct(product);
    }

    private void initViews() {
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle(R.string.activity_product_detail_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tv = mBinding.originPrice;
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        initViewPager();
    }

    private void initViewPager() {
        List<String> urls = new ArrayList<>();
        urls.add("http://7xls0x.com5.z0.glb.clouddn.com/9e9f05d0424b9541bc93daf1dc89b482ec10a314");
        urls.add("http://7xls0x.com5.z0.glb.clouddn.com/120c11140ef657ea4e7220d133dd2b06c4d0bf18");
        urls.add("http://7xls0x.com5.z0.glb.clouddn.com/8de3539da0a2a38d413decfd8c4fabc6b95056f3");
        urls.add("http://7xls0x.com5.z0.glb.clouddn.com/f671e61ce8b39c373a472824312a885e12d47a7b");
        mAdapter = new ProductGalleryAdapter(getApplicationContext(), urls);
        mBinding.viewPager.setAdapter(mAdapter);

        mBinding.indicator.setItemCount(urls.size());
        mBinding.indicator.setIndex(1);

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.indicator.setIndex(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}