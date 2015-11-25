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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.GetCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.OrderConfirmAdapter;
import com.maxleap.ebusiness.models.Order;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    public static final String INTENT_ORDER_ID_KEY = "intent_order_id_key";

    private ListView productLV;
    private TextView orderNo;
    private TextView orderState;
    private TextView orderTotal;
    private TextView receiveUser;
    private TextView receiveAddress;
    private TextView createTime;
    private TextView deliverType;
    private TextView receiptHeading;
    private TextView receiptType;
    private TextView receiptContent;
    private ProgressBar progressBar;
    private FrameLayout confirmArea;
    private Button confirmBtn;

    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
    }

    private void init() {
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_order_detail_title);
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

        progressBar = (ProgressBar) findViewById(R.id.order_detail_progress);
        confirmArea = (FrameLayout) findViewById(R.id.order_detail_confirm_area);
        confirmBtn = (Button) findViewById(R.id.order_detail_confirm);
        confirmBtn.setOnClickListener(onClickListener);
        productLV = (ListView) findViewById(R.id.order_detail_products);
        View headView = LayoutInflater.from(this).inflate(R.layout.order_detail_head, null, false);
        orderNo = (TextView) headView.findViewById(R.id.order_detail_no);

        orderState = (TextView) headView.findViewById(R.id.order_detail_state);

        productLV.addHeaderView(headView);
        View footView = LayoutInflater.from(this).inflate(R.layout.order_detail_foot, null, false);
        orderTotal = (TextView) footView.findViewById(R.id.order_detail_total);

        receiveUser = (TextView) footView.findViewById(R.id.order_detail_receive_user);

        receiveAddress = (TextView) footView.findViewById(R.id.order_detail_receive_address);

        createTime = (TextView) footView.findViewById(R.id.order_detail_time);

        deliverType = (TextView) footView.findViewById(R.id.order_detail_deliver_type);

        receiptHeading = (TextView) footView.findViewById(R.id.order_detail_receipt_heading);

        receiptType = (TextView) footView.findViewById(R.id.order_detail_receipt_type);

        receiptContent = (TextView) footView.findViewById(R.id.order_detail_receipt_content);

        productLV.addFooterView(footView);
        fetchOrderData();
    }

    private void initData() {
        orderNo.setText(String.format(getString(R.string.activity_my_order_no), order.getId()));

        switch (order.getOrderStatus()) {
            case 1:
            case 2:
                orderState.setText(R.string.activity_my_order_state_cancel);
                orderState.setTextColor(getResources().getColor(R.color.text_color_black));
                break;
            case 3:
                orderState.setText(R.string.activity_my_order_state_delivered);
                orderState.setTextColor(getResources().getColor(R.color.text_color_black));
                break;
            case 4:
                orderState.setText(R.string.activity_my_order_state_done);
                orderState.setTextColor(getResources().getColor(R.color.text_color_black));
                break;
            case 5:
                orderState.setText(R.string.activity_my_order_state_commented);
                orderState.setTextColor(getResources().getColor(R.color.text_color_black));
                break;
            case 6:
            case 7:
                orderState.setText(R.string.activity_my_order_state_canceled);
                orderState.setTextColor(getResources().getColor(R.color.text_color_black));
                break;
        }

        int total = 0;
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            total = total + order.getOrderProducts().get(i).getQuantity() * order.getOrderProducts().get(i).getPrice();
        }
        orderTotal.setText(String.format(getString(R.string.activity_my_order_total), total / 100f));

        receiveUser.setText(String.format(
                getString(R.string.activity_order_detail_receive_user),
                order.getAddress().getName()));

        receiveAddress.setText(String.format(
                getString(R.string.activity_order_detail_receive_address),
                order.getAddress().getStreet()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createTime.setText(String.format(
                getString(R.string.activity_order_detail_order_time),
                simpleDateFormat.format(order.getCreateTime())));

        deliverType.setText(String.format(
                getString(R.string.activity_order_detail_deliver_type),
                order.getDelivery()));

        receiptHeading.setText(String.format(
                getString(R.string.activity_order_detail_receipt_heading),
                order.getReceiptHeading()));

        receiptType.setText(String.format(
                getString(R.string.activity_order_detail_receipt_type),
                order.getReceiptType()));

        receiptContent.setText(String.format(
                getString(R.string.activity_order_detail_receipt_content),
                order.getReceiptContent()));

        OrderConfirmAdapter adapter = new OrderConfirmAdapter(this, order.getOrderProducts());
        productLV.setAdapter(adapter);
    }

    private void fetchOrderData() {
        progressBar.setVisibility(View.VISIBLE);
        confirmArea.setVisibility(View.GONE);
        productLV.setVisibility(View.GONE);
        String id = getIntent().getStringExtra(INTENT_ORDER_ID_KEY);
        MLQuery query = new MLQuery("Order");
        query.include("address");

        MLQueryManager.getInBackground(query, id, new GetCallback<MLObject>() {
            @Override
            public void done(final MLObject object, MLException e) {
                if (e == null) {
                    List<MLObject> orderProducts = object.getList("order_products");
                    List<String> ids = new ArrayList<>();
                    for (MLObject orderProduct : orderProducts) {
                        ids.add(orderProduct.getObjectId());
                    }
                    MLQuery<MLObject> orderQuery = new MLQuery("OrderProduct");
                    orderQuery.include("product");
                    orderQuery.whereContainedIn("objectId", ids);
                    MLQueryManager.findAllInBackground(orderQuery, new FindCallback<MLObject>() {
                        @Override
                        public void done(List<MLObject> list, MLException e) {
                            if (e == null) {
                                object.put("order_products", list);
                                order = Order.from(object);
                                initData();
                                progressBar.setVisibility(View.GONE);
                                confirmArea.setVisibility(View.VISIBLE);
                                productLV.setVisibility(View.VISIBLE);
                            } else {
                                FFLog.toast(OrderDetailActivity.this, e.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    FFLog.toast(OrderDetailActivity.this, e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
