/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.CategoryAdapter;
import com.maxleap.ebusiness.databinding.FragmentCategoriesBinding;
import com.maxleap.ebusiness.models.ProductType;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.ebusiness.widget.HorizontalDividerItemDecoration;
import com.maxleap.exception.MLException;

import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentCategoriesBinding mBinding;
    private ObservableArrayList<ProductType> mCategories;
    private CategoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_categories, container, false);
        initViews();
        return mBinding.getRoot();
    }

    private void initViews() {
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle(R.string.activity_categories_title);

        mBinding.recyclerview.setHasFixedSize(true);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(R.color.bg_main)
                        .size(1)
                        .marginResId(R.dimen.item_margin,
                                R.dimen.item_margin).build()
        );
        if(mCategories == null){
            mCategories = new ObservableArrayList<>();
            fetchData();
        }
        if(mAdapter == null){
            mAdapter = new CategoryAdapter(mCategories);
        }
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
                        ProductType category = new ProductType(object);
                        FFLog.i(category.toString());
                        mCategories.add(category);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
