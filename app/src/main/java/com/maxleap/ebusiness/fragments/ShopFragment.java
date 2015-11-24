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
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.activities.MainActivity;
import com.maxleap.ebusiness.activities.ProductDetailActivity;
import com.maxleap.ebusiness.adapters.OrderProductAdapter;
import com.maxleap.ebusiness.models.CartList;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.models.Product;
import com.maxleap.ebusiness.models.ProductData;
import com.maxleap.ebusiness.utils.CartPreferenceUtil;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Handler mHandler;
    private Context mContext;
    private ArrayList<OrderProduct> mOrderProducts;
    private OrderProductAdapter mAdapter;
    private View mEmptyView;
    private TextView mTotalPayView;
    private ListView mListView;
    private MainActivity mainActivity;
    private float mTotalPay;

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_shop, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mContext = getActivity();
        mainActivity = (MainActivity) getActivity();
        mHandler = new Handler();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderProducts.size() == 0) return;
                TextView editView = (TextView) v;
                if (editView.getText().toString().equals(getString(R.string.frag_shop_toolbar_edit))) {
                    editView.setText(R.string.frag_shop_toolbar_edit_done);
                    mAdapter.setInEditMode(true);
                } else {
                    editView.setText(R.string.frag_shop_toolbar_edit);
                    mAdapter.setInEditMode(false);
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView = (ListView) view.findViewById(R.id.shop_list);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.view_shop_list_foot, null);
        mTotalPayView = (TextView) footView.findViewById(R.id.total_pay);
        mTotalPayView.setText(String.format(getString(R.string.product_price), mTotalPay));
        footView.findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:
            }
        });
        mEmptyView = view.findViewById(R.id.empty);
        mEmptyView.setVisibility(View.GONE);
        mEmptyView.findViewById(R.id.to_main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.selectTab(0);
            }
        });
        mListView.addFooterView(footView);

        if (mOrderProducts == null) {
            mOrderProducts = new ArrayList<>();
            mHandler.postDelayed(mProgressRunnable, 100);
        }
        if (mOrderProducts.isEmpty()) {
            fetchShopData();
        }
        if (mAdapter == null) {
            mAdapter = new OrderProductAdapter(mContext, mOrderProducts, new OrderProductAdapter.CountListener() {
                @Override
                public void onCountChanged() {
                    if (mOrderProducts.size() == 0) {
                        mListView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        return;
                    }
                    int totalPay = 0;
                    for (OrderProduct orderProduct : mOrderProducts) {
                        totalPay += orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
                    }
                    mTotalPay = totalPay / 100f;
                    mTotalPayView.setText(String.format(getString(R.string.product_price), mTotalPay));
                }
            });
        }
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void fetchShopData() {
        CartPreferenceUtil sp = CartPreferenceUtil.getComplexPreferences(mContext, CartPreferenceUtil.DATA, 0);
        CartList cartList = sp.getObject(CartPreferenceUtil.KEY, CartList.class);
        if (cartList == null || cartList.getList() == null || cartList.getList().isEmpty()) {
            FFLog.d("fetchShopData cartList : " + cartList);
            mHandler.removeCallbacks(mProgressRunnable);
            mSwipeRefreshLayout.setRefreshing(false);
            mListView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }
        FFLog.d("start fetchShopData");
        final List<String> productIds = new ArrayList<>();
        final List<Integer> counts = new ArrayList<>();
        for (ProductData data : cartList.getList()) {
            productIds.add(data.getId());
            counts.add(data.getCount());
        }
        MLQuery<MLObject> query = new MLQuery<MLObject>("Product");
        query.whereContainedIn("objectId", productIds);

        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                mHandler.removeCallbacks(mProgressRunnable);
                mSwipeRefreshLayout.setRefreshing(false);
                FFLog.d("fetchShopData list: " + list);
                FFLog.d("fetchShopData e: " + e);
                if (e == null) {
                    mEmptyView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrderProducts.clear();
                    int totalPay = 0;
                    for (int i = 0; i < list.size(); i++) {
                        OrderProduct orderProduct = new OrderProduct();
                        orderProduct.setQuantity(counts.get(i));
                        orderProduct.setProduct(new Product(list.get(i)));
                        totalPay += orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
                        mOrderProducts.add(orderProduct);
                    }
                    mTotalPay = totalPay / 100f;
                    mTotalPayView.setText(String.format(getString(R.string.product_price), mTotalPay));
                    mAdapter.notifyDataSetChanged();
                } else {
                    mListView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        fetchShopData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODID, mOrderProducts.get(position).getProduct().getId());
        startActivity(intent);
    }
}
