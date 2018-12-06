package com.example.philong.banhang.Objects;

public class Bill {
    private String bill_note;
    private String date_create;
    private String staff_create;
    private String status;
    private String table;
    private String total_price;
    private String total_price_after_promotion;

    public Bill() {
    }

    public Bill(String bill_note, String date_create, String staff_create, String status, String table, String total_price, String total_price_after_promotion) {
        this.bill_note = bill_note;
        this.date_create = date_create;
        this.staff_create = staff_create;
        this.status = status;
        this.table = table;
        this.total_price = total_price;
        this.total_price_after_promotion = total_price_after_promotion;
    }

    public Bill(String bill_note, String date_create, String staff_create, String status, String total_price_after_promotion) {
        this.bill_note = bill_note;
        this.date_create = date_create;
        this.staff_create = staff_create;
        this.status = status;
        this.total_price_after_promotion = total_price_after_promotion;
    }

    public String getBill_note() {
        return bill_note;
    }

    public void setBill_note(String bill_note) {
        this.bill_note = bill_note;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getStaff_create() {
        return staff_create;
    }

    public void setStaff_create(String staff_create) {
        this.staff_create = staff_create;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_price_after_promotion() {
        return total_price_after_promotion;
    }

    public void setTotal_price_after_promotion(String total_price_after_promotion) {
        this.total_price_after_promotion = total_price_after_promotion;
    }
}
