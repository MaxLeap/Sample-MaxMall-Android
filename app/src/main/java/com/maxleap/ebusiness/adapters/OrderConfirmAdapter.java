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
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.models.ProductData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderConfirmAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ProductData> productDatas;

    public OrderConfirmAdapter(Context context, ArrayList<ProductData> productDatas) {
        this.mContext = context;
        this.productDatas = productDatas;
    }

    @Override
    public int getCount() {
        return productDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return productDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address, parent, false);
            holder = new ViewHolder();
            holder.productIcon = (ImageView) convertView.findViewById(R.id.item_order_product_icon);
            holder.productTitle = (TextView) convertView.findViewById(R.id.item_order_product_title);
            holder.productNo = (TextView) convertView.findViewById(R.id.item_order_product_no);
            holder.productPrice = (TextView) convertView.findViewById(R.id.item_order_product_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductData productData = productDatas.get(position);

        Picasso.with(mContext).load(productData.getImageUrl()).into(holder.productIcon);
        holder.productTitle.setText(productData.getTitle());
        holder.productNo.setText(String.format(mContext.getString(R.string.activity_my_order_product_no)
                , productData.getCount()));
        holder.productPrice.setText(String.format(mContext.getString(R.string.activity_my_order_product_price)
                , productData.getPrice() * productData.getCount()));

        return convertView;

    }


    static class ViewHolder {
        ImageView productIcon;
        TextView productTitle;
        TextView productNo;
        TextView productPrice;
    }
}
