package com.edwardvanraak.materialbarcodescannerexample.model.results;

import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflinePriceItem;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflineProductItem;
import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 01/04/2017.
 */

public class ItemDetails implements Serializable {
    public boolean isOffline = false;
    public String id;
    public String title;
    public String smallImageUrl;
    public String productGroup;
    public ListPrice listPrice;
    public String rank;
    public String advisor;

    public String weight;
    public ListingPrice amazonPrice;

    public double partialFee;
    public double initialSalePrice;
    public double calculatedInboundShipping;
    public double cost;
    public double profit;

    public ArrayList<Offer> newOffersList = new ArrayList<>();
    public ArrayList<Offer> usedOffersList = new ArrayList<>();
    public ArrayList<Offer> fbaOffersList = new ArrayList<>();

    public double getUpdatedProfit(double initialSalePrice) {
        return initialSalePrice - calculatedInboundShipping - cost - partialFee;
    }


    public static ItemDetails parseItemDetails(String stringJson) {
        ItemDetails itemDetails = new ItemDetails();
        try {

            JSONObject rootResultObject = new JSONObject(stringJson);
            JSONObject attributeJsonObject = null;
            JSONObject salesRankingJsonObject = null;
            JSONArray offersJsonArray = null;
            JSONObject advisorJsonObject = null;

            if (rootResultObject.has("AttributeSets")) {
                attributeJsonObject = rootResultObject.getJSONArray("AttributeSets").getJSONObject(0);
            }
            if (rootResultObject.has("Identifiers")) {
                JSONObject identifiersObject = rootResultObject.getJSONObject("Identifiers");
                if (identifiersObject.has("MarketplaceASIN") && identifiersObject.getJSONObject("MarketplaceASIN").has("ASIN")) {
                    itemDetails.id = identifiersObject.getJSONObject("MarketplaceASIN").getString("ASIN");
                }
            }

            if (rootResultObject.has("SalesRankings")) {
                salesRankingJsonObject = rootResultObject.getJSONObject("SalesRankings");
            }
            if (rootResultObject.has("Offers")) {
                offersJsonArray = rootResultObject.getJSONArray("Offers");
            }
            if (rootResultObject.has("Advisor")) {
                advisorJsonObject = rootResultObject.getJSONObject("Advisor");
            }
            if (rootResultObject.has("CompetitivePricing") && rootResultObject.getJSONObject("CompetitivePricing").has("CompetitivePrices")) {
                JSONObject competitivePricesObject = rootResultObject.getJSONObject("CompetitivePricing").getJSONObject("CompetitivePrices");
                JSONObject priceJSONObject = null;

                if (competitivePricesObject.has("1") && competitivePricesObject.getJSONObject("1").has("Price")) {
                    priceJSONObject = competitivePricesObject.getJSONObject("1").getJSONObject("Price");
                } else if (competitivePricesObject.has("2") && competitivePricesObject.getJSONObject("2").has("Price")) {
                    priceJSONObject = competitivePricesObject.getJSONObject("2").getJSONObject("Price");
                }

                if (priceJSONObject != null && priceJSONObject.has("LandedPrice")) {
                    JSONObject amazonPriceJsonObject = priceJSONObject.getJSONObject("LandedPrice");
                    ListingPrice listingPrice = new ListingPrice();
                    listingPrice.setAmount(amazonPriceJsonObject.getString("Amount"));
                    listingPrice.setCurrencyCode(amazonPriceJsonObject.getString("CurrencyCode"));

                    itemDetails.amazonPrice = listingPrice;
                }


            }


            if (attributeJsonObject != null) {
                parseObjectAttributes(itemDetails, attributeJsonObject);
            }
            if (salesRankingJsonObject != null && salesRankingJsonObject.has("SalesRank")) {
                itemDetails.rank = salesRankingJsonObject.getJSONArray("SalesRank").getJSONObject(0).getString("Rank");
            }
            if (offersJsonArray != null) {
                parseOffersData(itemDetails, offersJsonArray);
            }
            if (advisorJsonObject != null) {
                parseAdvisorInfo(itemDetails, advisorJsonObject);
            }


        } catch (JSONException e) {
            GlobalUtil.printLog("ParseException", "" + e);
        }
        return itemDetails;

    }

    private static void parseObjectAttributes(ItemDetails itemDetails, JSONObject attributeJsonObject) {
        try {
            if (attributeJsonObject.has("Title")) {
                itemDetails.title = attributeJsonObject.getString("Title");
            }
            if (attributeJsonObject.has("ProductGroup")) {
                itemDetails.productGroup = attributeJsonObject.getString("ProductGroup");
            }

            if (attributeJsonObject.has("SmallImage")) {
                itemDetails.smallImageUrl = attributeJsonObject.getJSONObject("SmallImage").getString("URL");
            }
            if (attributeJsonObject.has("ListPrice")) {
                ListPrice listPrice = new ListPrice();

                JSONObject listPriceJsonObject = attributeJsonObject.getJSONObject("ListPrice");
                listPrice.setAmount(listPriceJsonObject.getString("Amount"));
                listPrice.setCurrencyCode(listPriceJsonObject.getString("CurrencyCode"));

                itemDetails.listPrice = listPrice;
            }
        } catch (JSONException e) {
            GlobalUtil.printLog("ParseException", "" + e);
        }
    }

    private static void parseOffersData(ItemDetails itemDetails, JSONArray offersJsonArray) {
        try {
            for (int count = 0; count < offersJsonArray.length(); count++) {
                if (!offersJsonArray.isNull(count)) {
                    Offer offer = new Offer();
                    JSONObject offerJsonObject = offersJsonArray.getJSONObject(count);
                    if (offerJsonObject.has("Landed")) {
                        offer.amount = offerJsonObject.getJSONObject("Landed").getDouble("Amount");
                    }
                    String subCondition = offerJsonObject.getString("SubCondition");
                    boolean isFulfilledByAmazon = offerJsonObject.getBoolean("IsFulfilledByAmazon");

                    if (isFulfilledByAmazon) {
                        itemDetails.fbaOffersList.add(offer);
                    } else if (subCondition.equalsIgnoreCase("new")) {
                        itemDetails.newOffersList.add(offer);
                    } else {
                        itemDetails.usedOffersList.add(offer);
                    }
                }

            }
        } catch (JSONException jsonException) {
            GlobalUtil.printLog("ParseException", "" + jsonException);
        }
    }

    private static void parseAdvisorInfo(ItemDetails itemDetails, JSONObject advisorJsonObject) {
        try {
            if (advisorJsonObject.has("Message")) {
                itemDetails.advisor = advisorJsonObject.getString("Message");
            }
            if (advisorJsonObject.has("InitialSalePrice")) {
                itemDetails.initialSalePrice = advisorJsonObject.getDouble("InitialSalePrice");
            }
            if (advisorJsonObject.has("CalculatedInboundShipping")) {
                itemDetails.calculatedInboundShipping = advisorJsonObject.getDouble("CalculatedInboundShipping");
            }
            if (advisorJsonObject.has("Cost")) {
                itemDetails.cost = advisorJsonObject.getDouble("Cost");
            }
            if (advisorJsonObject.has("Profit")) {
                itemDetails.profit = advisorJsonObject.getDouble("Profit");

            }
            if (advisorJsonObject.has("Weight")) {
                itemDetails.weight = advisorJsonObject.getString("Weight");
            }
            if (advisorJsonObject.has("PartialFees")) {
                itemDetails.partialFee = advisorJsonObject.getDouble("PartialFees");
            }
        } catch (JSONException jsonException) {
            GlobalUtil.printLog("ParseException", "" + jsonException);
        }
    }

    public static ItemDetails getItemDetailsFromOfflineData(OfflineProductItem offlineProductItem, List<OfflinePriceItem> offlinePriceItems) {
        ListPrice listPrice = new ListPrice();
        ListingPrice amazonPrice = new ListingPrice();
        listPrice.setAmount(offlineProductItem.getPriceTag());
        amazonPrice.setAmount(offlineProductItem.getPriceAmazon());

        ItemDetails itemDetails = new ItemDetails();
        itemDetails.isOffline = true;
        itemDetails.title = offlineProductItem.getProductName();
        itemDetails.listPrice = listPrice;
        itemDetails.amazonPrice = amazonPrice;
        itemDetails.smallImageUrl = offlineProductItem.getPicture();
        itemDetails.rank = offlineProductItem.getRankPrimary();
        itemDetails.weight = offlineProductItem.getShippingWeight();


        if (offlinePriceItems != null) {
            for (OfflinePriceItem offlinePriceItem : offlinePriceItems) {
                Offer offer = new Offer();
                offer.amount = Double.valueOf(offlinePriceItem.getPrice());

                if (offlinePriceItem.getPrime().equalsIgnoreCase("Y")) {
                    itemDetails.fbaOffersList.add(offer);
                } else if (offlinePriceItem.getCondition().equalsIgnoreCase("new")) {
                    itemDetails.newOffersList.add(offer);
                } else {
                    itemDetails.usedOffersList.add(offer);
                }
            }
        }


        return itemDetails;
    }
}
