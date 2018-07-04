package com.cashback.data;

/**
 * Created by jack on 09-05-2016.
 */
public class PaymentModel
{

    private String _PaymentMethod,_PaymentId,_PaypalAccount,_HolderName,_BankName,_City,_AccountNumber,_IFSCCODE,_AccountType;

    public String get_PaymentMethod() {
        return _PaymentMethod;
    }

    public void set_PaymentMethod(String _PaymentMethod) {
        this._PaymentMethod = _PaymentMethod;
    }

    public String get_PaymentId() {
        return _PaymentId;
    }

    public void set_PaymentId(String _PaymentId) {
        this._PaymentId = _PaymentId;
    }

    public String get_PaypalAccount() {
        return _PaypalAccount;
    }

    public void set_PaypalAccount(String _PaypalAccount) {
        this._PaypalAccount = _PaypalAccount;
    }

    public String get_HolderName() {
        return _HolderName;
    }

    public void set_HolderName(String _HolderName) {
        this._HolderName = _HolderName;
    }

    public String get_BankName() {
        return _BankName;
    }

    public void set_BankName(String _BankName) {
        this._BankName = _BankName;
    }

    public String get_City() {
        return _City;
    }

    public void set_City(String _City) {
        this._City = _City;
    }

    public String get_AccountNumber() {
        return _AccountNumber;
    }

    public void set_AccountNumber(String _AccountNumber) {
        this._AccountNumber = _AccountNumber;
    }

    public String get_IFSCCODE() {
        return _IFSCCODE;
    }

    public void set_IFSCCODE(String _IFSCCODE) {
        this._IFSCCODE = _IFSCCODE;
    }

    public String get_AccountType() {
        return _AccountType;
    }

    public void set_AccountType(String _AccountType) {
        this._AccountType = _AccountType;
    }
}
