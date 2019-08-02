package com.example.change.foodorderserver.Model;

import java.util.List;

public class Request {
    private String email;
    private String phone;
    private String name;

    private String address;
    private String total;
    private String status;
    private String comment;
    private String txnStatus;
    private String latLng;
    private String supplierEmail;
    private List<Order> foods;

    public Request() {
    }

    public Request(String email,String phone, String name, String address, String total, String status, String comment,String txnStatus, String latlng, List<Order> foods) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.comment = comment;
        this.txnStatus = txnStatus;
        this.latLng = latlng;
        this.foods = foods;
    }

    public String getLatLng() {
        return latLng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }
}