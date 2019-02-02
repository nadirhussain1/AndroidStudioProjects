package com.edwardvanraak.materialbarcodescannerexample.storage;

import com.activeandroid.query.Select;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflinePriceItem;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflineProductItem;

import java.util.List;

/**
 * Created by nadirhussain on 16/04/2017.
 */

public final class DbManager {
    private DbManager() {

    }

    public static void saveProductItem(OfflineProductItem offlineProductItem) {
        offlineProductItem.save();
    }

    public static void savePriceItem(OfflinePriceItem offlinePriceItem) {
        offlinePriceItem.save();
    }

    public static List<OfflineProductItem> getProductsByText(String text) {
        List<OfflineProductItem> list = new Select()
                .from(OfflineProductItem.class)
                .where("product_name like ?", "%" + text + "%")
                .execute();

        return list;
    }

    public static OfflineProductItem getProductsByAsin(String asin) {
        List<OfflineProductItem> list = new Select()
                .from(OfflineProductItem.class)
                .where("asin = ?", asin)
                .execute();

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    public static OfflineProductItem getProductsByISBN(String isbn) {
        List<OfflineProductItem> list = new Select()
                .from(OfflineProductItem.class)
                .where("isbn_10 = ? OR isbn_13 = ?", isbn, isbn)
                .execute();

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public static List<OfflinePriceItem> getPriceItemsByProductId(String productId) {
        List<OfflinePriceItem> list = new Select()
                .from(OfflinePriceItem.class)
                .where("product_id = ?", productId)
                .execute();

        return list;
    }

}
