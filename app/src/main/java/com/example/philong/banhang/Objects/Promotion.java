package com.example.philong.banhang.Objects;

public class Promotion {
    private String promotion_code;
    private String promotion_discount;
    private String promotion_type;

    public Promotion() {
    }

    public Promotion(String promotion_code, String promotion_discount, String promotion_type) {
        this.promotion_code = promotion_code;
        this.promotion_discount = promotion_discount;
        this.promotion_type = promotion_type;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }

    public String getPromotion_discount() {
        return promotion_discount;
    }

    public void setPromotion_discount(String promotion_discount) {
        this.promotion_discount = promotion_discount;
    }

    public String getPromotion_type() {
        return promotion_type;
    }

    public void setPromotion_type(String promotion_type) {
        this.promotion_type = promotion_type;
    }
}
