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

import com.maxleap.GetCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.ProductGalleryAdapter;
import com.maxleap.ebusiness.databinding.ActivityProductDetailBinding;
import com.maxleap.ebusiness.models.Product;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    public final static String PRODID = "id";

    private ActivityProductDetailBinding mBinding;
    private ProductGalleryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        initViews();
        String id = getIntent().getStringExtra(PRODID);
        fetchProductData(id);
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

        mBinding.review.setOnClickListener(this);
        mBinding.spec.setOnClickListener(this);
        mBinding.detail.setOnClickListener(this);
        mBinding.increaseQuantity.setOnClickListener(this);
        mBinding.decreaseQuantity.setOnClickListener(this);
        mBinding.colorLayout.setOnClickListener(this);
        mBinding.versionLayout.setOnClickListener(this);
        mBinding.fav.setOnClickListener(this);
        mBinding.cart.setOnClickListener(this);
        mBinding.addToCart.setOnClickListener(this);


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

    private void fetchProductData(String id) {
        MLQuery<MLObject> query = MLQuery.getQuery("Product");
        query.whereEqualTo("objectId", id);
        MLQueryManager.getFirstInBackground(query, new GetCallback<MLObject>() {
            @Override
            public void done(MLObject object, MLException e) {
                Product product = new Product(object);
                mBinding.setProduct(product);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color_layout:
                break;
            case R.id.version_layout:
                break;
            case R.id.increase_quantity:
                increase();
                break;
            case R.id.decrease_quantity:
                decrease();
                break;
            case R.id.review:
                break;
            case R.id.spec:
                break;
            case R.id.detail:
                break;
            case R.id.fav:
                fav();
                break;
            case R.id.cart:
                break;
            case R.id.add_to_cart:
                break;
            default:
                break;
        }
    }

    private void decrease() {
        int quantity = Integer.parseInt(mBinding.quantity.getText().toString());
        if (quantity == 0) {
            mBinding.decreaseQuantity.setEnabled(false);
        } else {
            mBinding.quantity.setText(--quantity + "");
        }
    }

    private void increase() {
        int quantity = Integer.parseInt(mBinding.quantity.getText().toString());
        // check store quantity
        mBinding.quantity.setText(++quantity + "");
    }

    private void fav() {
        boolean isFav = mBinding.fav.isSelected();
        mBinding.fav.setSelected(!isFav);
    }




}