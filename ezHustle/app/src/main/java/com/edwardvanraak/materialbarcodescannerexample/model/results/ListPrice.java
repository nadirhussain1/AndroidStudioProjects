
package com.edwardvanraak.materialbarcodescannerexample.model.results;

import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListPrice implements Serializable {

    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("CurrencyCode")
    @Expose
    private String currencyCode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDisplayValue() {
        return GlobalUtil.formatAmount(Double.valueOf(amount)) + "$";
    }

}
