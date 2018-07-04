package com.cashback.data;

/**
 * Created by dev on 24/3/17.
 */

public class HotCoupon {

    private String
            coupon_id,title,code,link,start_date,expiry_date,retailer_id,retailer_title,retailer_image,cashback;

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getRetailer_title() {
        return retailer_title;
    }

    public void setRetailer_title(String retailer_title) {
        this.retailer_title = retailer_title;
    }

    public String getRetailer_image() {
        return retailer_image;
    }

    public void setRetailer_image(String retailer_image) {
        this.retailer_image = retailer_image;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }
}
