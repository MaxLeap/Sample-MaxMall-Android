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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.CommentAdapter;
import com.maxleap.ebusiness.manage.OperationCallback;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.Comment;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.utils.FFLog;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity {

    private ListView listView;
    private FrameLayout productArea;
    private Button confirmBtn;
    private ProgressBar progressBar;
    private ArrayList<OrderProduct> mProducts;
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
                confirmBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                List<Comment> comments = mProductAdapter.getContents();
                if (comments == null) return;
                UserManager.getInstance().addComment(comments, new OperationCallback() {
                    @Override
                    public void success() {
                        FFLog.toast(CommentActivity.this, R.string.activity_comment_success);
                        finish();
                    }

                    @Override
                    public void failed(String error) {
                        FFLog.toast(CommentActivity.this, error);
                        confirmBtn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        mProducts = getIntent().getParcelableExtra(MyOrderActivity.INTENT_ORDERPRODUCTS_KEY);
        mProductAdapter = new CommentAdapter(this, mProducts);
        listView.setAdapter(mProductAdapter);
    }

}
