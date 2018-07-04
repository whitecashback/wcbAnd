package com.cashback.data;

/**
 * Created by jack on 10-07-2015.
 */
public class StoreCashBackItems
{
    private String Transaction_Id,Retailer,Payment_Type,Amount,Status,DateCreated,ProcessDate,Retailer_Id,Reffer_To,referUser;

    public String getReferUser() {
        return referUser;
    }

    public void setReferUser(String referUser) {
        this.referUser = referUser;
    }

    public String getTransaction_Id() {
        return Transaction_Id;
    }

    public void setTransaction_Id(String transaction_Id) {
        Transaction_Id = transaction_Id;
    }

    public String getRetailer() {
        return Retailer;
    }

    public void setRetailer(String retailer) {
        Retailer = retailer;
    }

    public String getPayment_Type() {
        return Payment_Type;
    }

    public void setPayment_Type(String payment_Type) {
        Payment_Type = payment_Type;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getProcessDate() {
        return ProcessDate;
    }

    public void setProcessDate(String processDate) {
        ProcessDate = processDate;
    }

    public String getRetailer_Id() {
        return Retailer_Id;
    }

    public void setRetailer_Id(String retailer_Id) {
        Retailer_Id = retailer_Id;
    }

    public String getReffer_To() {
        return Reffer_To;
    }

    public void setReffer_To(String reffer_To) {
        Reffer_To = reffer_To;
    }
}
