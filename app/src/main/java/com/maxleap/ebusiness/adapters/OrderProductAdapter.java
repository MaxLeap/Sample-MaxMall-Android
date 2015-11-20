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
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProductAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<OrderProduct> mOrderProducts;
    private Context mContext;
    private boolean inEditMode;

    public OrderProductAdapter(Context context, ArrayList<OrderProduct> orderProducts) {
        mContext = context;
        mOrderProducts = orderProducts;
        inEditMode = false;
    }

    @Override
    public int getCount() {
        return mOrderProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_product, parent, false);
            holder = new ViewHolder();
            holder.deleteBtn = (ImageButton) convertView.findViewById(R.id.delete_btn);
            holder.imageView = (ImageView) convertView.findViewById(R.id.product_image);
            holder.titleView = (TextView) convertView.findViewById(R.id.product_title);
            holder.priceView = (TextView) convertView.findViewById(R.id.product_price);
            holder.minusBtn = (ImageButton) convertView.findViewById(R.id.minus_btn);
            holder.countView = (TextView) convertView.findViewById(R.id.product_count);
            holder.addBtn = (ImageButton) convertView.findViewById(R.id.add_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isInEditMode()) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }

        OrderProduct orderProduct = mOrderProducts.get(position);
        Product item = orderProduct.getProduct();
        Picasso.with(mContext).load(item.getIcons().get(0)).placeholder(R.mipmap.def_item).into(holder.imageView);
        holder.titleView.setText(item.getTitle());
        holder.priceView.setText(String.format(mContext.getString(R.string.product_price), item.getPrice() / 100f));
        holder.countView.setText(String.valueOf(orderProduct.getQuantity()));

        holder.deleteBtn.setTag(position);
        holder.minusBtn.setTag(position);
        holder.addBtn.setTag(position);
        holder.deleteBtn.setOnClickListener(this);
        holder.minusBtn.setOnClickListener(this);
        holder.addBtn.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        final int position = (int) v.getTag();

        switch (v.getId()) {
            case R.id.delete_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                builder.setMessage(R.string.dialog_shop_delete_confirm_notice);
                builder.setPositiveButton(R.string.dialog_shop_delete_confirm_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOrderProducts.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.dialog_shop_delete_confirm_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.minus_btn:
                OrderProduct item = mOrderProducts.get(position);
                int count = item.getQuantity();
                count--;
                if (count < 0) count = 0;
                item.setQuantity(count);
                notifyDataSetChanged();
                break;
            case R.id.add_btn:
                OrderProduct item2 = mOrderProducts.get(position);
                int count2 = item2.getQuantity();
                count2++;
                item2.setQuantity(count2);
                notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    static class ViewHolder {
        ImageButton deleteBtn;
        ImageView imageView;
        TextView titleView;
        TextView priceView;
        ImageButton minusBtn;
        TextView countView;
        ImageButton addBtn;
    }

    public boolean isInEditMode() {
        return inEditMode;
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
        notifyDataSetChanged();
    }
}
