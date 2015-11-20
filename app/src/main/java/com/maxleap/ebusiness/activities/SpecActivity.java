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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.SpecAdapter;
import com.maxleap.ebusiness.models.Specs;

import org.json.JSONException;

public class SpecActivity extends BaseActivity {

    public static final String SPEC = "spec";

    private RecyclerView mRecyclerView;
    private SpecAdapter mSpecAdapter;

    private String mSpecString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec);
        mSpecString = "[\n" +
                "\t{\"key\":\"基本参数\", \"value\" : \"\"},\n" +
                "\n" +
                "\t{\"key\":\"品牌\", \"value\" : \"魅族Meizu\"},\n" +
                "\n" +
                "\t{\"key\":\"型号\", \"value\" : \"MX4 PRO\"},\n" +
                "\n" +
                "\t{\"key\":\"基本参数\", \"value\" : \"\"},\n" +
                "\n" +
                "\t{\"key\":\"品牌\", \"value\" : \"魅族Meizu\"},\n" +
                "\n" +
                "\t{\"key\":\"型号\", \"value\" : \"MX4 PRO\"}\n" +
                "\n" +
                "]";//getIntent().getStringExtra(SPEC);

        initViews();

        try {
            Specs specs = new Specs(mSpecString);
            mSpecAdapter = new SpecAdapter(specs);
            mRecyclerView.setAdapter(mSpecAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initViews() {
        initToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_spec_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}