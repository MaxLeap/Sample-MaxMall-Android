/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.OrderConfirmAdapter;
import com.maxleap.ebusiness.manage.OperationCallback;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.Address;
import com.maxleap.ebusiness.models.Order;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.models.Product;
import com.maxleap.ebusiness.models.ProductData;
import com.maxleap.ebusiness.utils.CartPreferenceUtil;
import com.maxleap.ebusiness.utils.DialogUtil;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.ebusiness.widget.ReceiptDialog;

import java.util.ArrayList;

public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener {

    public static final int ADDRESS_REQUEST_CODE = 10;

    private ArrayList<ProductData> productData;
    private Address address;

    private TextView inputAdd;
    private RelativeLayout payTypeRL;
    private TextView payType;
    private TextView remark;
    private TextView receiptInfo;
    private ListView productLV;
    private TextView mTotalPrice;
    private Button mConfirmBtn;
    private ReceiptDialog receiptDialog;

    private String selectedType;
    private String selectedContent;
    private String headingText;
    private int total;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        init();
    }

    private void init() {
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_order_confirm_title);
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

        CartPreferenceUtil sp = CartPreferenceUtil.getComplexPreferences(this);
        productData = (ArrayList<ProductData>) sp.getProductData();
        if (productData == null || productData.isEmpty()) {
            return;
        }

        productLV = (ListView) findViewById(R.id.order_confirm_product);
        View headView = LayoutInflater.from(this).inflate(R.layout.item_order_confirm_head, null, false);
        productLV.addHeaderView(headView);
        OrderConfirmAdapter adapter = new OrderConfirmAdapter(this, productData);
        productLV.setAdapter(adapter);

        inputAdd = (TextView) headView.findViewById(R.id.order_confirm_input_address);
        inputAdd.setOnClickListener(this);
        payTypeRL = (RelativeLayout) headView.findViewById(R.id.order_confirm_pay_type_area);
        payTypeRL.setOnClickListener(this);
        payType = (TextView) headView.findViewById(R.id.order_confirm_pay_type);
        remark = (TextView) headView.findViewById(R.id.order_confirm_remarks);
        findViewById(R.id.order_confirm_remarks_rl).setOnClickListener(this);
        receiptInfo = (TextView) headView.findViewById(R.id.order_confirm_receipt);
        findViewById(R.id.order_confirm_receipt_rl).setOnClickListener(this);

        mTotalPrice = (TextView) findViewById(R.id.order_confirm_price_total);
        mConfirmBtn = (Button) findViewById(R.id.order_confirm_btn);
        mConfirmBtn.setOnClickListener(this);

        initFoodPrice();
    }

    private void initFoodPrice() {
        total = 0;
        for (int i = 0; i < productData.size(); i++) {
            total = total + productData.get(i).getPrice() * productData.get(i).getCount();
        }
        mTotalPrice.setText(String.format(getString(R.string.activity_order_confirm_total_price)
                , total / 100f));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                address = (Address) data.getExtras().getSerializable(AddressActivity.INTENT_ADDRESS_KEY);
                inputAdd.setText(address.getStreet());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_confirm_input_address:
                Intent intent = new Intent(this, AddressActivity.class);
                intent.putExtra(AddressActivity.INTENT_CHOOSE_KEY, true);
                startActivityForResult(intent, ADDRESS_REQUEST_CODE);
                break;
            case R.id.order_confirm_pay_type_area:
                FFLog.toast(OrderConfirmActivity.this,
                        R.string.activity_order_confirm_pay_no_choice);
                break;
            case R.id.order_confirm_remarks_rl:
                String remarkText = remark.getText().toString().equals(getString(R.string.activity_order_confirm_remarks_hint)) ? "" : remark.getText().toString();
                DialogUtil.showInputInfoDialog(this, getString(R.string.remarks_dialog_title),
                        getString(R.string.remarks_dialog_hint), remarkText,
                        new DialogUtil.Listener() {
                            @Override
                            public void onOk(String content) {
                                remark.setText(content);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
            case R.id.order_confirm_receipt_rl:
                if (receiptDialog == null) {
                    receiptDialog = new ReceiptDialog(this, new ReceiptDialog.ChooseListener() {
                        @Override
                        public void onChooseType(String type) {
                            selectedType = type;
                        }

                        @Override
                        public void onChooseContent(String content) {
                            selectedContent = content;
                        }

                        @Override
                        public void onInputHeading(String heading) {
                            headingText = heading;
                            receiptInfo.setText(headingText);
                        }
                    });
                }
                receiptDialog.showReceipt();
                break;
            case R.id.order_confirm_btn:
                v.setEnabled(false);
                toOrderStateAc();
                break;
            default:
                break;
        }
    }

    private void toOrderStateAc() {
        if (address == null) {
            FFLog.toast(this, R.string.activity_order_confirm_no_address);
            mConfirmBtn.setEnabled(true);
            return;
        }
        final Order order = new Order();
        String remarkText = remark.getText().toString().equals(getString(R.string.activity_order_confirm_remarks_hint)) ? "" : remark.getText().toString();
        order.setRemarks(remarkText);
        if (!TextUtils.isEmpty(headingText)) {
            order.setReceiptType(selectedType);
            order.setReceiptContent(selectedContent);
            order.setReceiptHeading(headingText);
        } else {
            order.setReceiptType("");
            order.setReceiptContent("");
            order.setReceiptHeading("");
        }
        order.setPayMethod(payType.getText().toString());
        order.setOrderStatus(2);
        order.setAddress(address);
        order.setTotal(total);
        order.setDelivery("商家自配");
        ArrayList<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < productData.size(); i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setPrice(productData.get(i).getPrice());
            orderProduct.setProduct(new Product(productData.get(i).getId()));
            orderProduct.setQuantity(productData.get(i).getCount());
            orderProduct.setCustomInfo(productData.get(i).getCustomInfo());
            orderProducts.add(orderProduct);
        }

        mConfirmBtn.setEnabled(false);
        mConfirmBtn.setText(R.string.activity_order_confirm_committing);
        UserManager.getInstance().addOrder(order, new OperationCallback() {
            @Override
            public void success() {
                Intent intent = new Intent(OrderConfirmActivity.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.INTENT_ORDER_ID_KEY, order.getId());
                startActivity(intent);
                CartPreferenceUtil.getComplexPreferences(OrderConfirmActivity.this).drop();
                finish();
            }

            @Override
            public void failed(String error) {
                FFLog.toast(OrderConfirmActivity.this, error);
                mConfirmBtn.setEnabled(true);
                mConfirmBtn.setText(R.string.activity_order_confirm_commit);
            }
        });
    }
}
