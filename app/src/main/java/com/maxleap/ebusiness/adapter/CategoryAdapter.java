/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.databinding.ItemCategoryBinding;
import com.maxleap.ebusiness.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;

        public CategoryHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemCategoryBinding binding) {
            this.binding = binding;
        }

        public void bind(Category category) {
            binding.setCategory(category);
        }
    }

    private ObservableArrayList<Category> mList;

    public CategoryAdapter(ObservableArrayList<Category> list) {
        this.mList = list;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_category, parent, false
        );
        CategoryHolder holder = new CategoryHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = mList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}