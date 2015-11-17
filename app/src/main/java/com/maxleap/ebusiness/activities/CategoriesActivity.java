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
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapter.CategoryAdapter;
import com.maxleap.ebusiness.databinding.ActivityCategoriesBinding;
import com.maxleap.ebusiness.models.Category;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.List;

public class CategoriesActivity extends BaseActivity {

    private ActivityCategoriesBinding mBinding;
    private ObservableArrayList<Category> mCategories;
    private CategoryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_categories);
        mBinding.progressbar.setVisibility(View.VISIBLE);
        mCategories = new ObservableArrayList<>();
        initViews();
        fetchData();
    }

    private void initViews() {
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle(R.string.activity_categories_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBinding.recyclerview.setHasFixedSize(true);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CategoryAdapter(mCategories);
        mBinding.recyclerview.setAdapter(mAdapter);
    }

    private void fetchData() {
        MLQuery<MLObject> query = MLQuery.getQuery("ProductType");

        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                mCategories.clear();
                mBinding.progressbar.setVisibility(View.GONE);
                if (e == null) {
                    for (MLObject object : list) {
                        Category category = new Category(object);
                        FFLog.i(category.toString());
                        mCategories.add(category);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}