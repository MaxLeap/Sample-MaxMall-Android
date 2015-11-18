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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.ProductAdapter;
import com.maxleap.ebusiness.adapters.SimpleAdapter;
import com.maxleap.ebusiness.models.Product;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private static final String SP_SEARCH_HISTORY_FILE = "search_history";
    private static final String SP_SEARCH_HISTORY_KEY = "search";
    private String mSeparator;
    private LinearLayout mSearchHistoryLayout;
    private LinearLayout mSearchResultLayout;
    private ProductAdapter mProductAdapter;
    private ArrayList<Product> mProducts;
    private EditText mSearchView;
    private TextView mSortView;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
    }

    private void init() {
        mSeparator = ";";
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchView = (EditText) findViewById(R.id.search);
        mSortView = (TextView) findViewById(R.id.sort);
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchContent = mSearchView.getText().toString();
                    if (!searchContent.isEmpty()) {
                        searchContent.replaceAll(mSeparator, "");
                        performSearch(searchContent);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initUI() {
        mSearchHistoryLayout = (LinearLayout) findViewById(R.id.search_history_layout);
        mSearchResultLayout = (LinearLayout) findViewById(R.id.search_result_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.search_progress);
        mSearchHistoryLayout.setVisibility(View.GONE);
        mSearchResultLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mProducts = new ArrayList<>();

        final ArrayList<String> searchHistory = getSearchHistory();
        if (!searchHistory.isEmpty()) {
            mSortView.setVisibility(View.GONE);
            mSearchHistoryLayout.setVisibility(View.VISIBLE);
            ListView listView = (ListView) findViewById(R.id.search_history_list);
            listView.setAdapter(new SimpleAdapter(this, searchHistory));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    performSearch(searchHistory.get(position));
                }
            });
        }
    }

    private void performSearch(String searchContent) {
        hideSoftKeyBoard(mSearchView);
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchHistoryLayout.setVisibility(View.GONE);
        saveSearchHistory(searchContent);
        fetchProductData(searchContent);
    }

    private void fetchProductData(String searchContent) {
        FFLog.d("start fetchProductData");
        MLQuery query = new MLQuery("Product");
        query.whereContains("title", searchContent);
        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(final List<MLObject> list, MLException e) {
                FFLog.d("fetchProductData list: " + list);
                FFLog.d("fetchProductData e: " + e);
                if (e == null) {
                    final ArrayList<Product> products = new ArrayList<Product>();
                    for (MLObject object : list) {
                        products.add(new Product(object));
                    }
                    if (products.size() > 0) {
                        mSortView.setVisibility(View.VISIBLE);
                        mProducts.clear();
                        mProducts.addAll(products);
                        showSearchResult();
                    }
                } else {
                    mSortView.setVisibility(View.GONE);
                    mProducts.clear();
                    showSearchResult();
                }
            }

        });
    }

    private void showSearchResult() {
        mProgressBar.setVisibility(View.GONE);
        mSearchHistoryLayout.setVisibility(View.GONE);
        mSearchResultLayout.setVisibility(View.VISIBLE);
        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(this, mProducts);
            ListView listView = (ListView) findViewById(R.id.search_result_list);
            listView.setAdapter(mProductAdapter);
            listView.setEmptyView(findViewById(R.id.search_empty));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } else {
            mProductAdapter.notifyDataSetChanged();
        }
    }

    private void saveSearchHistory(String searchContent) {
        SharedPreferences sp = getSharedPreferences(SP_SEARCH_HISTORY_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        String savedContent = sp.getString(SP_SEARCH_HISTORY_KEY, "");
        if (savedContent.isEmpty()) {
            editor.putString(SP_SEARCH_HISTORY_KEY, searchContent + mSeparator);
        } else {
            if (savedContent.contains(searchContent)) {
                savedContent = savedContent.replaceAll(searchContent + mSeparator, "");
            }
            savedContent += searchContent + mSeparator;
            editor.putString(SP_SEARCH_HISTORY_KEY, savedContent);
        }
        editor.apply();
    }

    private ArrayList<String> getSearchHistory() {
        ArrayList<String> searchHistory = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(SP_SEARCH_HISTORY_FILE, 0);
        String savedContent = sp.getString(SP_SEARCH_HISTORY_KEY, "");
        if (!savedContent.isEmpty()) {
            String[] savedArray = savedContent.split(mSeparator);
            for (int i = savedArray.length - 1; i > -1; i--) {
                if (!savedArray[i].isEmpty() && searchHistory.size() < 10) {
                    searchHistory.add(savedArray[i]);
                }
            }
        }
        return searchHistory;
    }

    public void clearSearchHistory(View v) {
        FFLog.d("clearSearchHistory ");
        SharedPreferences sp = getSharedPreferences(SP_SEARCH_HISTORY_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        mSearchHistoryLayout.setVisibility(View.GONE);
        mSearchResultLayout.setVisibility(View.GONE);
    }


    private void hideSoftKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
