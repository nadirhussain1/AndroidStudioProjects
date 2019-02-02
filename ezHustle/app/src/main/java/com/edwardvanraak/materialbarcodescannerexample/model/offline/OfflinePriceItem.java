
package com.edwardvanraak.materialbarcodescannerexample.model.offline;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Table(name = "table_product_prices")
public class OfflinePriceItem  extends Model{
    @SerializedName("product_id")
    @Expose
    @Column(name = "product_id")
    private String productId;

    @SerializedName("price")
    @Expose
    @Column(name = "price")
    private String price;

    @SerializedName("shipping")
    @Expose
    @Column(name = "shipping")
    private String shipping;

    @SerializedName("condition")
    @Expose
    @Column(name = "condition")
    private String condition;

    @SerializedName("prime")
    @Expose
    @Column(name = "prime")
    private String prime;

    @SerializedName("seller")
    @Expose
    @Column(name = "seller")
    private String seller;

    @SerializedName("seller_link")
    @Expose
    @Column(name = "seller_link")
    private String sellerLink;

    @SerializedName("date")
    @Expose
    @Column(name = "date")
    private Integer date;





    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrime() {
        return prime;
    }

    public void setPrime(String prime) {
        this.prime = prime;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerLink() {
        return sellerLink;
    }

    public void setSellerLink(String sellerLink) {
        this.sellerLink = sellerLink;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

}
