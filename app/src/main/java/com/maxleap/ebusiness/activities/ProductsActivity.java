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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.MLRelation;
import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.adapters.ProductAdapter;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.Comment;
import com.maxleap.ebusiness.models.Product;
import com.maxleap.ebusiness.models.User;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductsActivity extends BaseActivity {
    public static final String INTENT_TITLE = "title";
    public static final String INTENT_TYPE_ID = "id";
    private ArrayList<Product> mProducts;
    private ArrayList<Comment> mComments;
    private ProductAdapter mProductAdapter;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        init();
    }

    private void init() {
        initToolBar();
        initUI();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(INTENT_TITLE));
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
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        ListView listView = (ListView) findViewById(R.id.products_list);
        listView.setEmptyView(findViewById(R.id.empty));
        if (mProducts == null) {
            mProducts = new ArrayList<>();
            mComments = new ArrayList<>();

        }
        if (mProducts.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
            fetchProductData();
        }
        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(this, mProducts, mComments);
        }
        listView.setAdapter(mProductAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = mProducts.get(position);
                Intent intent = new Intent(ProductsActivity.this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.PRODID, product.getId());
                startActivity(intent);
            }
        });
    }

    private void fetchProductData() {
        FFLog.d("start fetchProductData");
        MLQuery query = null;
        String productTypeId = getIntent().getStringExtra(INTENT_TYPE_ID);
        if (productTypeId == null || productTypeId.isEmpty()) {
            User user = UserManager.getInstance().getCurrentUser();
            if (user != null && user.getFavorites() != null) {
                query = user.getFavorites().getQuery();
            }
        } else {
            MLObject object = MLObject.createWithoutData("ProductType", productTypeId);
            MLRelation relation = object.getRelation("Product");
            relation.setTargetClass("Product");
            query = relation.getQuery();
        }
        if (query == null) {
            FFLog.d("query is null ");
            return;
        }

        MLQueryManager.findAllInBackground(query, new FindCallback<MLObject>() {
            @Override
            public void done(final List<MLObject> list, MLException e) {
                FFLog.d("fetchProductData list: " + list);
                FFLog.d("fetchProductData e: " + e);
                if (e == null) {
                    Set<MLObject> ids = new HashSet<MLObject>();
                    final ArrayList<Product> products = new ArrayList<Product>();
                    for (MLObject object : list) {
                        Product product = new Product(object);
                        products.add(product);
                        ids.add(MLObject.createWithoutData("Product", product.getId()));
                    }
                    if (products.size() > 0) {
                        mProducts.clear();
                        mProducts.addAll(products);

                        MLQuery<MLObject> commentQuery = MLQuery.getQuery("Comment");
                        commentQuery.whereContainedIn("product", ids);
                        commentQuery.include("product");
                        commentQuery.include("user");

                        MLQueryManager.findAllInBackground(commentQuery, new FindCallback<MLObject>() {
                            @Override
                            public void done(List<MLObject> list, MLException e) {
                                mProgressBar.setVisibility(View.GONE);
                                if (e == null) {
                                    mComments.clear();
                                    for (MLObject object : list) {
                                        mComments.add(new Comment(object));
                                    }
                                }
                                mProductAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

        });
    }

}
