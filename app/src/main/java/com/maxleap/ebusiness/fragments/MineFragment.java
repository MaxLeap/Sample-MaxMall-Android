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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.activities.AccountInfoActivity;
import com.maxleap.ebusiness.activities.LoginActivity;
import com.maxleap.ebusiness.manage.UserManager;

public class MineFragment extends Fragment implements View.OnClickListener {

    private final String MAXLEAPMOBILE_WEBSITE = "https://maxleap.cn";
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.fragment_mine_title);

        view.findViewById(R.id.mine_frag_account).setOnClickListener(this);
        view.findViewById(R.id.mine_frag_like).setOnClickListener(this);
        view.findViewById(R.id.mine_frag_order).setOnClickListener(this);
        view.findViewById(R.id.mine_frag_help).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_frag_account:
                if (UserManager.getInstance().getCurrentUser() != null) {
                    Intent toAccountIntent = new Intent(mContext, AccountInfoActivity.class);
                    startActivity(toAccountIntent);
                } else {
                    Intent toAccountIntent = new Intent(mContext, LoginActivity.class);
                    startActivity(toAccountIntent);
                }
                break;
            case R.id.mine_frag_like:
                break;
            case R.id.mine_frag_order:
                break;
            case R.id.mine_frag_help:
                Uri uri = Uri.parse(MAXLEAPMOBILE_WEBSITE);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
