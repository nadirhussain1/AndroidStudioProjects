package com.edwardvanraak.materialbarcodescannerexample.model.results;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by nadirhussain on 28/03/2017.
 */

public class ListItemData implements Serializable {
    public String identifier;
    public String title;
    public String smallImageUrl;
    public String productGroup;
    public String genre;
    public ListPrice listPrice;
    public String rank;


    public static ListItemData parseListItemData(JSONObject rootResultObject) {
        ListItemData dataItem = new ListItemData();
        try {

            JSONObject attributeJsonObject = rootResultObject.getJSONArray("AttributeSets").getJSONObject(0);
            JSONObject salesRankingJsonObject = null;
            if (rootResultObject.has("SalesRankings")) {
                salesRankingJsonObject = rootResultObject.getJSONObject("SalesRankings");
            }

            dataItem.identifier = rootResultObject.getJSONObject("Identifiers").getJSONObject("MarketplaceASIN").getString("ASIN");
            if (attributeJsonObject.has("Title")) {
                dataItem.title = attributeJsonObject.getString("Title");
            }
            if (attributeJsonObject.has("ProductGroup")) {
                dataItem.productGroup = attributeJsonObject.getString("ProductGroup");
            }
            if (attributeJsonObject.has("Genre")) {
                dataItem.genre = attributeJsonObject.getString("Genre");
            }
            if (attributeJsonObject.has("SmallImage")) {
                dataItem.smallImageUrl = attributeJsonObject.getJSONObject("SmallImage").getString("URL");
            }
            if (attributeJsonObject.has("ListPrice")) {
                ListPrice listPrice = new ListPrice();

                JSONObject listPriceJsonObject = attributeJsonObject.getJSONObject("ListPrice");
                listPrice.setAmount(listPriceJsonObject.getString("Amount"));
                listPrice.setCurrencyCode(listPriceJsonObject.getString("CurrencyCode"));

                dataItem.listPrice = listPrice;
            }
            if (salesRankingJsonObject != null && salesRankingJsonObject.has("SalesRank")) {
                dataItem.rank = salesRankingJsonObject.getJSONArray("SalesRank").getJSONObject(0).getString("Rank");
            }
        } catch (Exception e) {

        }
        return dataItem;

    }
}
