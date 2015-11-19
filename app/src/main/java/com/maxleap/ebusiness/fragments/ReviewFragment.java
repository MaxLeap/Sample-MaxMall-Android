/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.ReviewAdapter;
import com.maxleap.ebusiness.models.Comment;

import java.util.List;

public class ReviewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private List<Comment> mCommentList;

    public static ReviewFragment newInstance(List<Comment> list) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_review, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.review_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(mCommentList);
        mRecyclerView.setAdapter(mReviewAdapter);
    }
}