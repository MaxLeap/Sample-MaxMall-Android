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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maxleap.ebusiness.R;
import com.maxleap.ebusiness.models.Comment;
import com.maxleap.ebusiness.models.OrderProduct;
import com.maxleap.ebusiness.utils.FFLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private static final int MAX_COMMENT_LENGTH = 140;

    private Context mContext;
    private List<OrderProduct> mProducts;
    private int[] ratings;
    private String[] contents;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<OrderProduct> products) {
        mContext = context;
        mProducts = products;
        ratings = new int[products.size()];
        contents = new String[products.size()];
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            holder = new ViewHolder();
            holder.productIcon = (ImageView) convertView.findViewById(R.id.item_comment_product)
                    .findViewById(R.id.item_order_product_icon);
            holder.productTitle = (TextView) convertView.findViewById(R.id.item_comment_product)
                    .findViewById(R.id.item_order_product_title);
            holder.productNo = (TextView) convertView.findViewById(R.id.item_comment_product)
                    .findViewById(R.id.item_order_product_no);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_comment_rate);
            holder.comment = (EditText) convertView.findViewById(R.id.item_comment_edit);
            holder.commentCount = (TextView) convertView.findViewById(R.id.item_comment_edit_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderProduct orderProduct = mProducts.get(position);

        Picasso.with(mContext).load(orderProduct.getProduct().getIcons().get(0)).into(holder.productIcon);
        holder.productTitle.setText(orderProduct.getProduct().getTitle());
        holder.productNo.setText(String.format(mContext.getString(R.string.activity_my_order_product_no)
                , orderProduct.getQuantity()));
        holder.ratingBar.setRating(ratings[position]);
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                FFLog.d("rating : " + rating);
                ratings[position] = (int) rating;
            }
        });
        holder.comment.setText(contents[position]);
        holder.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contents[position] = s.toString();
                holder.commentCount.setText(s.toString().length() + "/" + MAX_COMMENT_LENGTH);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView productIcon;
        TextView productTitle;
        TextView productNo;
        RatingBar ratingBar;
        EditText comment;
        TextView commentCount;
    }

    public List<Comment> getContents() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.clear();
        for (int i = 0; i < mProducts.size(); i++) {
            if (ratings[i] == 0 || contents[i] == null) {
                return null;
            }
            Comment comment = new Comment();
            comment.setScore(ratings[i]);
            comment.setProduct(mProducts.get(i).getProduct());
            comment.setContent(contents[i]);
            comments.add(comment);
        }
        return comments;
    }

}
