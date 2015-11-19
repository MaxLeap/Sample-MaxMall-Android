/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.models.Order;
import com.maxleap.ebusiness.models.OrderProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<Order> mOrders;

    public OrderAdapter(Context context, List<Order> orders) {
        mContext = context;
        mOrders = orders;
    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
            holder = new ViewHolder();
            holder.orderNo = (TextView) convertView.findViewById(R.id.item_order_no);
            holder.orderState = (TextView) convertView.findViewById(R.id.item_order_state);
            holder.products = (LinearLayout) convertView.findViewById(R.id.item_order_products);
            holder.orderTotal = (TextView) convertView.findViewById(R.id.item_order_total);
            holder.createTime = (TextView) convertView.findViewById(R.id.item_order_create_time);
            holder.remainTime = (TextView) convertView.findViewById(R.id.item_order_remaining_time);
            holder.confirmBtn = (Button) convertView.findViewById(R.id.item_order_confirm);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = mOrders.get(position);

        holder.orderNo.setText(String.format(mContext.getString(R.string.activity_my_order_no), order.getId()));

        int size = order.getOrderProducts().size();
        int productsNo = holder.products.getChildCount();
        for (int i = 0; i < size; i++) {
            OrderProduct orderProduct = order.getOrderProducts().get(i);
            View productView;
            if (i < productsNo) {
                productView = holder.products.getChildAt(i);
                productView.setVisibility(View.VISIBLE);
            } else {
                productView = LayoutInflater.from(mContext).inflate(R.layout.item_order_product, holder.products, false);
            }
            ImageView imageView = (ImageView) productView.findViewById(R.id.item_order_product_icon);
            Picasso.with(mContext).load(orderProduct.getProduct().getIcons().get(0)).into(imageView);
            ((TextView) productView.findViewById(R.id.item_order_product_info))
                    .setText(orderProduct.getProduct().getTitle());
            ((TextView) productView.findViewById(R.id.item_order_product_no))
                    .setText(String.format(mContext.getString(R.string.activity_my_order_product_no)
                            , orderProduct.getQuantity()));
            if (i + 1 == size) {
                for (int j = i + 1; j < productsNo; j++) {
                    holder.products.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView orderNo;
        TextView orderState;
        LinearLayout products;
        TextView orderTotal;
        TextView createTime;
        TextView remainTime;
        Button confirmBtn;
    }
}
