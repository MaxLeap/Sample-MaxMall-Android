package com.maxleap.ebusiness.models;

import com.maxleap.FindCallback;
import com.maxleap.MLObject;
import com.maxleap.MLQueryManager;
import com.maxleap.ebusiness.utils.FFLog;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String id;
    private int total;
    private Address address;
    private User user;
    private String delivery;
    private String receiptTitle;
    private String receiptContent;
    private String receiptInfo;
    private String remarks;
    private String payMethod;
    private String orderStatus;
    private List<OrderProduct> orderProducts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    public String getReceiptInfo() {
        return receiptInfo;
    }

    public void setReceiptInfo(String receiptInfo) {
        this.receiptInfo = receiptInfo;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public interface OrderListener {
        void onFetch(Order order);
    }

    public static void from(MLObject object, final OrderListener listener) {
        final Order order = new Order();
        order.setId(object.getObjectId());
        order.setTotal(object.getInt("total"));
        User user = User.from(object.getMLObject("user"));
        order.setUser(user);
        Address address = Address.from(object.getMLObject("address"));
        order.setAddress(address);
        order.setDelivery(object.getString("delivery"));
        order.setReceiptTitle(object.getString("receipt_title"));
        order.setReceiptContent(object.getString("receipt_content"));
        order.setReceiptInfo(object.getString("receipt_info"));
        order.setRemarks(object.getString("remarks"));
        order.setPayMethod(object.getString("pay_method"));
        order.setOrderStatus(object.getString("order_status"));

        MLQueryManager.findAllInBackground(object.getRelation("order_products").getQuery(), new FindCallback<MLObject>() {
            @Override
            public void done(final List<MLObject> list, MLException e) {
                FFLog.d("order_products list: " + list);
                FFLog.d("order_products e: " + e);
                if (e == null) {
                    ArrayList<OrderProduct> orderProducts = new ArrayList<>();
                    for (MLObject object : list) {
                        orderProducts.add(OrderProduct.from(object));
                    }
                    order.setOrderProducts(orderProducts);
                    if (listener != null) {
                        listener.onFetch(order);
                    }
                }
            }

        });
    }
}
