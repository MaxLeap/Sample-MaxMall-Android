/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleap.ebusiness.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.activities.CommentActivity;
import com.maxleap.ebusiness.activities.MyOrderActivity;
import com.maxleap.ebusiness.activities.OrderDetailActivity;
import com.maxleap.ebusiness.manage.OperationCallback;
import com.maxleap.ebusiness.manage.UserManager;
import com.maxleap.ebusiness.models.Order;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.models.ProductData;
import com.maxleap.ebusiness.utils.DialogUtil;
import com.maxleap.ebusiness.widget.CustomClickableSpan;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<Order> mOrders;
    private Map<String, OrderProduct> orderProductMap;
    private Dialog dialog;

    public OrderAdapter(Context context, List<Order> orders) {
        mContext = context;
        mOrders = orders;
        orderProductMap = new HashMap<>();
        dialog = DialogUtil.createProgressDialog(context);
    }

    public void putOrderProduct(OrderProduct orderProduct) {
        orderProductMap.put(orderProduct.getId(), orderProduct);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        final Order order = mOrders.get(mOrders.size() - 1 - position);

        CustomClickableSpan customClickableSpan = new CustomClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.INTENT_ORDER_ID_KEY, order.getId());
                ((MyOrderActivity) mContext).toDetail(intent, position);
            }
        };
        SpannableString ss = new SpannableString(String.format(mContext.getString(R.string.activity_my_order_no), order.getId()));
        ss.setSpan(customClickableSpan, mContext.getString(R.string.activity_my_order_no).length() - 4,
                ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.orderNo.setText(ss);
        holder.orderNo.setMovementMethod(LinkMovementMethod.getInstance());

        holder.remainTime.setVisibility(View.GONE);
        holder.orderState.setOnClickListener(null);
        switch (order.getOrderStatus()) {
            case 1:
                holder.orderState.setText(R.string.activity_my_order_state_cancel);
                holder.orderState.setTextColor(mContext.getResources().getColor(R.color.orange));
                holder.orderState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateState(order, 2);
                    }
                });
                holder.confirmBtn.setText(R.string.activity_my_order_to_pay);
                holder.confirmBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.orderState.setText(R.string.activity_my_order_state_deliver);
                holder.orderState.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                holder.confirmBtn.setVisibility(View.GONE);
                break;
            case 3:
                holder.orderState.setText(R.string.activity_my_order_state_delivered);
                holder.orderState.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                holder.confirmBtn.setText(R.string.activity_my_order_to_confirm);
                holder.confirmBtn.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.orderState.setText(R.string.activity_my_order_state_done);
                holder.orderState.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                holder.confirmBtn.setText(R.string.activity_my_order_to_comment);
                holder.confirmBtn.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.orderState.setText(R.string.activity_my_order_state_commented);
                holder.orderState.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                holder.confirmBtn.setVisibility(View.GONE);
                break;
            case 6:
            case 7:
                holder.orderState.setText(R.string.activity_my_order_state_canceled);
                holder.orderState.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                holder.confirmBtn.setVisibility(View.GONE);
                break;
        }

        int size = order.getOrderProducts().size();
        int productsNo = holder.products.getChildCount();
        int total = 0;
        for (int i = 0; i < size; i++) {
            OrderProduct orderProduct = orderProductMap.get(order.getOrderProducts().get(i).getId());
            View productView;
            if (i < productsNo) {
                productView = holder.products.getChildAt(i);
                productView.setVisibility(View.VISIBLE);
            } else {
                productView = LayoutInflater.from(mContext).inflate(R.layout.item_order_product, null);
                holder.products.addView(productView);
            }
            ImageView imageView = (ImageView) productView.findViewById(R.id.item_order_product_icon);
            Picasso.with(mContext).load(orderProduct.getProduct().getIcons().get(0)).into(imageView);
            String customInfo = TextUtils.isEmpty(orderProduct.getCustomInfo()) ? "" : orderProduct.getCustomInfo();
            ((TextView) productView.findViewById(R.id.item_order_product_title))
                    .setText(orderProduct.getProduct().getTitle() + " " + customInfo);
            ((TextView) productView.findViewById(R.id.item_order_product_no))
                    .setText(String.format(mContext.getString(R.string.activity_my_order_product_no)
                            , orderProduct.getQuantity()));
            if (i + 1 == size) {
                for (int j = i + 1; j < productsNo; j++) {
                    holder.products.getChildAt(j).setVisibility(View.GONE);
                }
            }
            total = total + orderProduct.getPrice() * orderProduct.getQuantity();
        }
        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmText = ((Button) v).getText().toString();
                if (confirmText.equals(mContext.getString(R.string.activity_my_order_to_pay))) {
                    updateState(order, 2);
                } else if (confirmText.equals(mContext.getString(R.string.activity_my_order_to_confirm))) {
                    updateState(order, 4);
                } else if (confirmText.equals(mContext.getString(R.string.activity_my_order_to_comment))) {
                    ArrayList<ProductData> products = new ArrayList<>();
                    for (int i = 0; i < order.getOrderProducts().size(); i++) {
                        OrderProduct orderProduct = orderProductMap.get(order.getOrderProducts().get(i).getId());
                        ProductData productData = new ProductData();
                        productData.setId(orderProduct.getProduct().getId());
                        productData.setPrice(orderProduct.getPrice());
                        productData.setCount(orderProduct.getQuantity());
                        productData.setTitle(orderProduct.getProduct().getTitle());
                        productData.setCustomInfo(orderProduct.getCustomInfo());
                        productData.setImageUrl(orderProduct.getProduct().getIcons().get(0));
                        products.add(productData);
                    }
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra(CommentActivity.INTENT_ORDER_ID_KEY, order.getId());
                    intent.putExtra(CommentActivity.INTENT_PRODUCT_DATA_KEY, products);
                    ((MyOrderActivity) mContext).toComment(intent, position);
                }
            }
        });

        holder.orderTotal.setText(String.format(mContext.getString(R.string.activity_my_order_total), total / 100f));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.createTime.setText(String.format(mContext.getString(R.string.activity_my_order_order_time)
                , simpleDateFormat.format(order.getCreateTime())));
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

    private void updateState(final Order order, int state) {
        final int originalState = order.getOrderStatus();
        order.setOrderStatus(state);
        dialog.show();
        UserManager.getInstance().updateOrder(order, new OperationCallback() {
            @Override
            public void success() {
                dialog.dismiss();
                notifyDataSetChanged();
            }

            @Override
            public void failed(String error) {
                dialog.dismiss();
                order.setOrderStatus(originalState);
            }
        });
    }
}
