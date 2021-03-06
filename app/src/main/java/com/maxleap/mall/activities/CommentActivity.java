/**
 * Copyright (c) 2015-present, MaxLeap.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.mall.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maxleap.mall.R;
import com.maxleap.mall.adapters.CommentAdapter;
import com.maxleap.mall.manage.OperationCallback;
import com.maxleap.mall.manage.UserManager;
import com.maxleap.mall.models.Comment;
import com.maxleap.mall.models.Order;
import com.maxleap.mall.models.ProductData;
import com.maxleap.mall.utils.FFLog;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity {

    public static final String INTENT_PRODUCT_DATA_KEY = "intent_product_data_key";
    public static final String INTENT_ORDER_ID_KEY = "intent_order_id_key";

    private ListView listView;
    private FrameLayout productArea;
    private Button confirmBtn;
    private ProgressBar progressBar;
    private ArrayList<ProductData> mProducts;
    private String orderId;
    private CommentAdapter mProductAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void init() {
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_comment_title);
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
        progressBar = (ProgressBar) findViewById(R.id.comment_progress_bar);
        listView = (ListView) findViewById(R.id.comment_list);
        productArea = (FrameLayout) findViewById(R.id.comment_confirm_area);
        confirmBtn = (Button) findViewById(R.id.comment_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Comment> comments = mProductAdapter.getContents();
                if (comments == null) {
                    FFLog.toast(CommentActivity.this, R.string.activity_comment_failed);
                    return;
                }
                confirmBtn.setEnabled(false);
                confirmBtn.setText("");
                progressBar.setVisibility(View.VISIBLE);
                UserManager.getInstance().addComment(comments, new OperationCallback() {
                    @Override
                    public void success() {
                        Order order = new Order();
                        order.setId(orderId);
                        order.setOrderStatus(5);
                        UserManager.getInstance().updateOrder(order, new OperationCallback() {
                            @Override
                            public void success() {
                                FFLog.toast(CommentActivity.this, R.string.activity_comment_success);
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void failed(String error) {
                                FFLog.toast(CommentActivity.this, error);
                                confirmBtn.setEnabled(true);
                                confirmBtn.setText(R.string.activity_comment_confirm);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void failed(String error) {
                        FFLog.toast(CommentActivity.this, error);
                        confirmBtn.setEnabled(true);
                        confirmBtn.setText(R.string.activity_comment_confirm);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        orderId = getIntent().getStringExtra(INTENT_ORDER_ID_KEY);
        mProducts = (ArrayList<ProductData>) getIntent().getSerializableExtra(INTENT_PRODUCT_DATA_KEY);
        mProductAdapter = new CommentAdapter(this, mProducts);
        listView.setAdapter(mProductAdapter);
    }

}
