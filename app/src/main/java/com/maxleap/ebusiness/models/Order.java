package com.maxleap.ebusiness.models;

import com.maxleap.MLObject;

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

    public static Order from(MLObject object) {
        Order order = new Order();
        order.setId(object.getObjectId());
        order.setTotal(object.getInt("total"));
        Address address = Address.from(object.getMLObject("address"));
        order.setAddress(address);
        List<MLObject> mlOrderProducts = object.getList("order_products");
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (MLObject orderProduct : mlOrderProducts) {
            orderProducts.add(OrderProduct.from(orderProduct));
        }
        order.setOrderProducts(orderProducts);
        order.setDelivery(object.getString("delivery"));
        order.setReceiptTitle(object.getString("receipt_title"));
        order.setReceiptContent(object.getString("receipt_content"));
        order.setReceiptInfo(object.getString("receipt_info"));
        order.setRemarks(object.getString("remarks"));
        order.setPayMethod(object.getString("pay_method"));
        order.setOrderStatus(object.getString("order_status"));
        return order;
    }
}
