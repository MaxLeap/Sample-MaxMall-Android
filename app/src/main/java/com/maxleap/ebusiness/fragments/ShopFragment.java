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
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.activities.MainActivity;
import com.maxleap.ebusiness.adapters.OrderProductAdapter;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.models.Product;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Handler mHandler;
    private Context mContext;
    private ArrayList<OrderProduct> mOrderProducts;
    private OrderProductAdapter mAdapter;
    private View mEmptyView;
    private ListView mListView;
    private MainActivity mainActivity;

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
        toolbar.setTitle(R.string.frag_shop_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView = (ListView) view.findViewById(R.id.shop_list);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.view_shop_list_foot, null);
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
            mAdapter = new OrderProductAdapter(mContext, mOrderProducts);
        }
        mListView.setAdapter(mAdapter);
    }

    private void fetchShopData() {
        final List<String> productIds = getProductIds();
        final List<Integer> counts = getCounts();
        if (counts == null || productIds == null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mSwipeRefreshLayout.setRefreshing(false);
            mListView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }
        FFLog.d("start fetchShopData");
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
                    for (int i = 0; i < productIds.size(); i++) {
                        OrderProduct orderProduct = new OrderProduct();
                        orderProduct.setQuantity(counts.get(i));
                        orderProduct.setProduct(new Product(list.get(i)));
                        mOrderProducts.add(orderProduct);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    mListView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private List<Integer> getCounts() {
        //TODO : get data from sp.
        return null;
    }

    private List<String> getProductIds() {
        //TODO : get data from sp.
        return null;
    }

    @Override
    public void onRefresh() {
        fetchShopData();
    }
}
