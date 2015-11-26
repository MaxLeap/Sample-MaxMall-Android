/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.activities.LoginActivity;
import com.maxleap.ebusiness.activities.MainActivity;
import com.maxleap.ebusiness.activities.OrderConfirmActivity;
import com.maxleap.ebusiness.activities.ProductDetailActivity;
import com.maxleap.ebusiness.adapters.ShopProductAdapter;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.ProductData;
import com.maxleap.ebusiness.utils.CartPreferenceUtil;

import java.util.ArrayList;

public class ShopFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Context mContext;
    private ArrayList<ProductData> mProductDatas;
    private ShopProductAdapter mAdapter;
    private TextView mTotalPayView;
    private MainActivity mainActivity;
    private float mTotalPay;
    private Button mPayButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_shop, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchShopData();
    }

    private void initUI(View view) {
        mContext = getActivity();
        mainActivity = (MainActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final TextView editView = (TextView) toolbar.findViewById(R.id.edit);
        if (mAdapter != null && mAdapter.isInEditMode()) {
            editView.setText(R.string.frag_shop_toolbar_edit_done);
        } else {
            editView.setText(R.string.frag_shop_toolbar_edit);
        }
        toolbar.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.isInEditMode()) {
                    editView.setText(R.string.frag_shop_toolbar_edit);
                    mAdapter.setInEditMode(false);
                } else {
                    editView.setText(R.string.frag_shop_toolbar_edit_done);
                    mAdapter.setInEditMode(true);
                }
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.shop_list);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.view_shop_list_foot, null);
        mTotalPayView = (TextView) footView.findViewById(R.id.total_pay);
        mTotalPayView.setText(String.format(getString(R.string.product_price), mTotalPay));
        mPayButton = (Button) footView.findViewById(R.id.pay_button);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.getInstance().getCurrentUser() == null) {
                    Intent toAccountIntent = new Intent(mContext, LoginActivity.class);
                    startActivity(toAccountIntent);
                } else {
                    Intent intent = new Intent(mContext, OrderConfirmActivity.class);
                    startActivity(intent);
                }
            }
        });
        view.findViewById(R.id.to_main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.selectTab(0);
            }
        });
        listView.addFooterView(footView);
        listView.setEmptyView(view.findViewById(R.id.empty));

        if (mProductDatas == null) {
            mProductDatas = new ArrayList<>();
        }

        if (mAdapter == null) {
            mAdapter = new ShopProductAdapter(mContext, mProductDatas, new ShopProductAdapter.CountListener() {
                @Override
                public void onCountChanged() {
                    if (mProductDatas.size() == 0) {
                        return;
                    }
                    int totalPay = 0;
                    for (ProductData productData : mProductDatas) {
                        totalPay += productData.getPrice() * productData.getCount();
                    }
                    mTotalPay = totalPay / 100f;
                    mTotalPayView.setText(String.format(getString(R.string.product_price), mTotalPay));
                }
            });
        }

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    private void fetchShopData() {
        CartPreferenceUtil sp = CartPreferenceUtil.getComplexPreferences(mContext);
        ArrayList<ProductData> productDatas = (ArrayList<ProductData>) sp.getProductData();
        if (productDatas == null) {
            productDatas = new ArrayList<>();
        }
        mProductDatas.clear();
        mProductDatas.addAll(productDatas);
        int totalPay = 0;
        for (ProductData data : mProductDatas) {
            totalPay += data.getPrice() * data.getCount();
        }
        mTotalPay = totalPay / 100f;
        mTotalPayView.setText(String.format(getString(R.string.product_price), mTotalPay));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODID, mProductDatas.get(position).getId());
        startActivity(intent);
    }
}
