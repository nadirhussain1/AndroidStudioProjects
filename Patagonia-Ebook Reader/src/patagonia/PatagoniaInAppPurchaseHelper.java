package patagonia;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;

import java.util.ArrayList;
import java.util.List;

import util.IabHelper;
import util.IabResult;

/**
 * Created by rod on 3/15/16.
 */
public class PatagoniaInAppPurchaseHelper {

    private final String LOG_TAG = PatagoniaInAppPurchaseHelper.class.getSimpleName();
    private IabHelper mHelper;
    private Context context;
    private LibraryService libraryService;

    public PatagoniaInAppPurchaseHelper(Context context, LibraryService libraryService) {
        this.context = context;
        this.libraryService = libraryService;
    }

    private void disposeHelper() {
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    private void setupHelper() {
        mHelper = new IabHelper(context, Config.Base64EncodedPublicKey);
    }

    public void buyBook(LibraryBook libraryBook, Activity activity, IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener) {
        disposeHelper();
        setupHelper();

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(LOG_TAG, "Problem setting up In-app Billing: " + result);
                    disposeHelper();
                } else {
                    Log.d(LOG_TAG, "IAP Buy Book: " + libraryBook.getStoreProductID());
                    mHelper.launchPurchaseFlow(activity, libraryBook.getStoreProductID(), 10001,
                            mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                }
            }
        });
    }

    public void queryInventory(LibraryBook libraryBook, IabHelper.QueryInventoryFinishedListener mQueryFinishedListener) {
        List additionalSkuList = new ArrayList<>();
        Log.d(LOG_TAG, "SKU for Inventory: " + libraryBook.getStoreProductID());
        additionalSkuList.add(libraryBook.getStoreProductID());

        disposeHelper();
        setupHelper();

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(LOG_TAG, "Problem setting up In-app Billing: " + result);
                    disposeHelper();
                } else {
                    mHelper.queryInventoryAsync(true, additionalSkuList, mQueryFinishedListener);
                }
            }
        });
    }
}
