
package com.edwardvanraak.materialbarcodescannerexample.model.offline;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ListItemData;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ListPrice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Table(name = "table_products")
public class OfflineProductItem extends Model {

    @SerializedName("product_id")
    @Expose
    @Column(name = "product_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String productId;

    @SerializedName("asin")
    @Expose
    @Column(name = "asin")
    private String asin;

    @SerializedName("product_name")
    @Expose
    @Column(name = "product_name")
    private String productName;

    @SerializedName("author")
    @Expose
    @Column(name = "author")
    private String author;

    @SerializedName("picture")
    @Expose
    @Column(name = "picture")
    private String picture;

    @SerializedName("isbn_10")
    @Expose
    @Column(name = "isbn_10")
    private String isbn10;

    @SerializedName("isbn_13")
    @Expose
    @Column(name = "isbn_13")
    private String isbn13;

    @SerializedName("shipping_weight")
    @Expose
    @Column(name = "shipping_weight")
    private String shippingWeight;

    @SerializedName("rank_primary")
    @Expose
    @Column(name = "rank_primary")
    private String rankPrimary;

    @SerializedName("rank_secondary")
    @Expose
    @Column(name = "rank_secondary")
    private String rankSecondary;

    @SerializedName("link")
    @Expose
    @Column(name = "link")
    private String link;

    @SerializedName("price_link")
    @Expose
    @Column(name = "price_link")
    private String priceLink;

    @SerializedName("price_tag")
    @Expose
    @Column(name = "price_tag")
    private String priceTag;

    @SerializedName("price_amazon")
    @Expose
    @Column(name = "price_amazon")
    private String priceAmazon;

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

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public String getRankPrimary() {
        return rankPrimary;
    }

    public void setRankPrimary(String rankPrimary) {
        this.rankPrimary = rankPrimary;
    }

    public String getRankSecondary() {
        return rankSecondary;
    }

    public void setRankSecondary(String rankSecondary) {
        this.rankSecondary = rankSecondary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPriceLink() {
        return priceLink;
    }

    public void setPriceLink(String priceLink) {
        this.priceLink = priceLink;
    }

    public String getPriceTag() {
        return priceTag;
    }

    public void setPriceTag(String priceTag) {
        this.priceTag = priceTag;
    }

    public String getPriceAmazon() {
        return priceAmazon;
    }

    public void setPriceAmazon(String priceAmazon) {
        this.priceAmazon = priceAmazon;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }


    public ListItemData getListItemReplica() {
        ListItemData listItemData = new ListItemData();

        ListPrice listPrice = new ListPrice();
        listPrice.setAmount(priceAmazon);

        listItemData.identifier = asin;
        listItemData.title = productName;
        listItemData.listPrice = listPrice;
        listItemData.smallImageUrl = picture;
        listItemData.rank = getRankPrimary();

        return listItemData;
    }

}
