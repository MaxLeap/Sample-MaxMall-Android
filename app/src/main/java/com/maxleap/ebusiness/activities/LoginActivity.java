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
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.maxleap.MLAnalytics;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.manage.OperationCallback;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.User;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.ebusiness.utils.NoUtilCheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends BaseActivity {

    private EditText loginTel;
    private EditText loginVerifyCode;
    private Button getCodeBtn;
    private Button loginBtn;
    private RelativeLayout progressRL;

    private String verifyCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_login_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {

        initToolBar();
        progressRL = (RelativeLayout) findViewById(R.id.login_progress_rl);

        loginTel = (EditText) findViewById(R.id.login_tel);
        loginTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtnState();
            }
        });

        loginVerifyCode = (EditText) findViewById(R.id.login_verify_code);
        loginVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtnState();
            }
        });

        getCodeBtn = (Button) findViewById(R.id.login_verify_code_get);
        getCodeBtn.setEnabled(false);
        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        getCodeBtn.setEnabled(false);
                        getCodeBtn.setText(millisUntilFinished / 1000 + "s");
                    }

                    @Override
                    public void onFinish() {
                        getCodeBtn.setText(R.string.activity_login_get_verify_code);
                        checkBtnState();
                    }
                };
                countDownTimer.start();
                loginVerifyCode.setText(createVerifyCode());
            }
        });

        loginBtn = (Button) findViewById(R.id.login_confirm);
        loginBtn.setEnabled(false);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                progressRL.setVisibility(View.VISIBLE);
                loginBtn.setText("");
                loginBtn.setEnabled(false);
                User user = new User();
                user.setUsername(loginTel.getText().toString());
                UserManager.getInstance().login(user, new OperationCallback() {
                    @Override
                    public void success() {
                        Map<String, String> dimensions = new HashMap<>();
                        dimensions.put("username", loginTel.getText().toString());
                        MLAnalytics.logEvent("RegisterOrLoginEvent", 1, dimensions);
                        finish();
                    }

                    @Override
                    public void failed(String error) {
                        FFLog.toast(LoginActivity.this, error);
                        loginBtn.setText(R.string.activity_login_verify);
                        progressRL.setVisibility(View.GONE);
                        loginBtn.setEnabled(false);
                    }
                });
            }
        });
    }

    private void checkBtnState() {
        int telNoLength = loginTel.getText().length();
        int verifyNoLength = loginVerifyCode.getText().length();
        if (telNoLength == 11 && NoUtilCheck.isMobileNo(loginTel.getText().toString())) {
            if (getCodeBtn.getText().equals(getString(R.string.activity_login_get_verify_code))) {
                getCodeBtn.setEnabled(true);
            } else {
                getCodeBtn.setEnabled(false);
            }
            if (verifyNoLength > 0) {
                loginBtn.setEnabled(true);
            } else {
                loginBtn.setEnabled(false);
            }
        } else {
            getCodeBtn.setEnabled(false);
            loginBtn.setEnabled(false);
        }

    }

    private String createVerifyCode() {
        String code = "";
        Random object = new Random();
        for (int i = 0; i < 4; i++) {
            int x = object.nextInt(10);
            code = code + x;
        }
        verifyCode = code;
        return code;
    }

}
