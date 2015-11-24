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
import android.widget.ListView;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.OrderConfirmAdapter;
import com.maxleap.ebusiness.models.Order;

import java.text.SimpleDateFormat;

public class OrderDetailActivity extends BaseActivity {

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
        order = (Order) getIntent().getSerializableExtra("");

        confirmBtn = (Button) findViewById(R.id.order_detail_confirm);
        confirmBtn.setOnClickListener(onClickListener);
        productLV = (ListView) findViewById(R.id.order_detail_products);
        View headView = LayoutInflater.from(this).inflate(R.layout.order_detail_head, null, false);
        orderNo = (TextView) headView.findViewById(R.id.order_detail_no);
        orderNo.setText(String.format(getString(R.string.activity_my_order_no), order.getId()));

        orderState = (TextView) headView.findViewById(R.id.order_detail_state);
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
        productLV.addHeaderView(headView);
        View footView = LayoutInflater.from(this).inflate(R.layout.order_detail_foot, null, false);
        orderTotal = (TextView) footView.findViewById(R.id.order_detail_total);
        int total = 0;
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            total = total + order.getOrderProducts().get(i).getQuantity() * order.getOrderProducts().get(i).getPrice();
        }
        orderTotal.setText(String.format(getString(R.string.activity_my_order_total), total));

        receiveUser = (TextView) footView.findViewById(R.id.order_detail_receive_user);
        receiveUser.setText(String.format(
                getString(R.string.activity_order_detail_receive_user),
                order.getAddress().getName()));

        receiveAddress = (TextView) footView.findViewById(R.id.order_detail_receive_address);
        receiveAddress.setText(String.format(
                getString(R.string.activity_order_detail_receive_address),
                order.getAddress().getStreet()));

        createTime = (TextView) footView.findViewById(R.id.order_detail_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createTime.setText(String.format(
                getString(R.string.activity_order_detail_order_time),
                simpleDateFormat.format(order.getCreateTime())));

        deliverType = (TextView) footView.findViewById(R.id.order_detail_deliver_type);
        deliverType.setText(String.format(
                getString(R.string.activity_order_detail_deliver_type),
                order.getDelivery()));

        receiptHeading = (TextView) footView.findViewById(R.id.order_detail_receipt_heading);
        receiptHeading.setText(String.format(
                getString(R.string.activity_order_detail_receipt_heading),
                order.getReceiptHeading()));

        receiptType = (TextView) footView.findViewById(R.id.order_detail_receipt_type);
        receiptType.setText(String.format(
                getString(R.string.activity_order_detail_receipt_type),
                order.getReceiptType()));

        receiptContent = (TextView) footView.findViewById(R.id.order_detail_receipt_content);
        receiptContent.setText(String.format(
                getString(R.string.activity_order_detail_receipt_content),
                order.getReceiptContent()));

        productLV.addFooterView(footView);

        OrderConfirmAdapter adapter = new OrderConfirmAdapter(this, order.getOrderProducts());
        productLV.setAdapter(adapter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
