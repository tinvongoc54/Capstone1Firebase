package com.example.philong.banhang.Objects;

public class Bill_History extends Bill {
    private String key;
    private String customer_name;
    private String customer_phone;

    public Bill_History(){}

    public Bill_History(String bill_note, String date_create, String staff_create, String status, String total_price_after_promotion, String key, String customer_name, String customer_phone) {
        super(bill_note, date_create, staff_create, status, total_price_after_promotion);
        this.key = key;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }
}
