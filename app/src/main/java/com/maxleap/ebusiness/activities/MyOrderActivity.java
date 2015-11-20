/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.OrderAdapter;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.Order;
import com.maxleap.ebusiness.models.User;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends BaseActivity {

    private ListView listView;
    private TextView emptyView;
    private ProgressBar progressBar;
    private List<Order> mOrders;
    private OrderAdapter mOrderAdapter;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        init();
    }

    private void init() {
        initToolBar();
        initUI();
        fetchOrderData();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_my_order_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUI() {
        mUser = UserManager.getInstance().getCurrentUser();
        progressBar = (ProgressBar) findViewById(R.id.my_order_progress_bar);
        listView = (ListView) findViewById(R.id.my_order_list);
        emptyView = (TextView) findViewById(R.id.my_order_empty_view);
        mOrders = new ArrayList<>();
        mOrderAdapter = new OrderAdapter(this, mOrders);
        listView.setAdapter(mOrderAdapter);
    }

    private void fetchOrderData() {
        FFLog.d("start fetchOrderData");
        MLQuery query = new MLQuery("Order");
        MLObject user = MLObject.createWithoutData("_User", mUser.getId());
        query.whereEqualTo("user", user);
        query.include("address");
        query.include("order_products");
        query.include("product");
        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                FFLog.d("fetchOrderData list: " + list);
                FFLog.d("fetchOrderData e: " + e);
                if (e == null) {
                    for (MLObject object : list) {
                        mOrders.add(Order.from(object));
                    }
                    listView.setEmptyView(emptyView);
                    mOrderAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (e.getCode() == MLException.OBJECT_NOT_FOUND) {
                        listView.setEmptyView(emptyView);
                    } else {
                        FFLog.toast(MyOrderActivity.this, e.getMessage());
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

        });
    }

}
