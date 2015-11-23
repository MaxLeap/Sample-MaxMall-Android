/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.ReviewPagerAdapter;
import com.maxleap.ebusiness.fragments.ReviewFragment;
import com.maxleap.ebusiness.models.Comment;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends BaseActivity {

    private Context mContext;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ReviewPagerAdapter mReviewPagerAdapter;

    private ArrayList<Comment> mGreats;
    private ArrayList<Comment> mNormals;
    private ArrayList<Comment> mBads;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mContext = getApplicationContext();
        mGreats = new ArrayList<>();
        mNormals = new ArrayList<>();
        mBads = new ArrayList<>();
        fetchData();
        initViews();
    }

    private void fetchData() {
        MLQuery<MLObject> query = MLQuery.getQuery("Comment");

        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(List<MLObject> list, MLException e) {
                if (e == null) {
                    mBads.clear();
                    mNormals.clear();
                    mGreats.clear();
                    for (MLObject object : list) {
                        Comment comment = new Comment(object);
                        if (comment.getScore() <= 2) {
                            mBads.add(comment);
                        } else if (comment.getScore() == 3) {
                            mNormals.add(comment);
                        } else {
                            mGreats.add(comment);
                        }
                    }
                    initTabAndViewPager();
                }
            }
        });
    }

    private void initViews() {
        initTabAndViewPager();
        initToolbar();
    }

    private void initTabAndViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        mReviewPagerAdapter = new ReviewPagerAdapter(getSupportFragmentManager());
        mReviewPagerAdapter.addFragment(ReviewFragment.newInstance(mGreats), String.format(
                mContext.getString(R.string.activity_review_greate), mGreats.size()
        ));
        mReviewPagerAdapter.addFragment(ReviewFragment.newInstance(mNormals), String.format(
                mContext.getString(R.string.activity_review_normal), mNormals.size()
        ));
        mReviewPagerAdapter.addFragment(ReviewFragment.newInstance(mBads), String.format(
                mContext.getString(R.string.activity_review_bad), mBads.size()
        ));

        mViewPager.setAdapter(mReviewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.activity_review_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}