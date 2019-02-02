/*
 * Copyright (C) 2012 Alex Kuiper
 *
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 *//*

package net.nightwhistler.pageturner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.Configuration.ColourProfile;
import net.nightwhistler.pageturner.Configuration.LibrarySelection;
import net.nightwhistler.pageturner.Configuration.LibraryView;
import net.nightwhistler.pageturner.PlatformUtil;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.activity.LibraryActivity;
import net.nightwhistler.pageturner.activity.ReadingActivity;
import net.nightwhistler.pageturner.library.ImportCallback;
import net.nightwhistler.pageturner.library.ImportTask;
import net.nightwhistler.pageturner.library.KeyedResultAdapter;
import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;
import net.nightwhistler.pageturner.scheduling.QueueableAsyncTask;
import net.nightwhistler.pageturner.scheduling.TaskQueue;
import net.nightwhistler.pageturner.view.BookCaseView;
import net.nightwhistler.pageturner.view.FastBitmapDrawable;
import net.nightwhistler.ui.DialogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jedi.option.Option;
import patagonia.PatagoniaImportTask;
import roboguice.inject.InjectView;

import static java.lang.Character.toUpperCase;
import static jedi.functional.FunctionalPrimitives.isEmpty;
import static jedi.option.Options.none;
import static jedi.option.Options.option;
import static jedi.option.Options.some;
import static net.nightwhistler.ui.UiUtils.onMenuPress;

public class LibraryFragment extends RoboSherlockFragment implements ImportCallback {

    protected static final int REQUEST_CODE_GET_CONTENT = 2;

    @Inject
    private LibraryService libraryService;

    @Inject
    private DialogFactory dialogFactory;

    @InjectView(R.id.libraryList)
    private ListView listView;

    @InjectView(R.id.bookCaseView)
    private BookCaseView bookCaseView;

    @InjectView(R.id.libHolder)
    private ViewSwitcher switcher;

    @Inject
    private Context context;

    @Inject
    private Configuration config;

    @Inject
    private TaskQueue taskQueue;

    private Drawable backupCover;
    private Handler handler;

    private KeyedResultAdapter bookAdapter;

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG);
    private static final int ALPHABET_THRESHOLD = 20;

    private ProgressDialog waitDialog;
    private ProgressDialog importDialog;

    private AlertDialog importQuestion;

    private boolean askedUserToImport;
    private boolean oldKeepScreenOn;

    private static final Logger LOG = LoggerFactory.getLogger("LibraryActivity");

    private IntentCallBack intentCallBack;
    private List<CoverCallback> callbacks = new ArrayList<>();
    private Map<String, FastBitmapDrawable> coverCache = new HashMap<>();

    private MenuItem searchMenuItem;

    private interface IntentCallBack {
        void onResult(int resultCode, Intent data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap backupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_refresh);
        this.backupCover = new FastBitmapDrawable(backupBitmap);

        this.handler = new Handler();

        if (savedInstanceState != null) {
            this.askedUserToImport = savedInstanceState.getBoolean("import_q", false);
        }

        this.taskQueue.setTaskQueueListener(this::onTaskQueueEmpty);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        this.bookCaseView.setOnScrollListener(new CoverScrollListener());
        this.listView.setOnScrollListener(new CoverScrollListener());

        this.bookAdapter = new BookCaseAdapter();
        this.bookCaseView.setAdapter(bookAdapter);

        if (switcher.getDisplayedChild() == 0) {
            switcher.showNext();
        }

        this.waitDialog = new ProgressDialog(context);
        this.waitDialog.setOwnerActivity(getActivity());

        this.importDialog = new ProgressDialog(context);

        this.importDialog.setCanceledOnTouchOutside(false);

        this.importDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        this.importDialog.setOwnerActivity(getActivity());
        importDialog.setTitle(R.string.importing_books);
        importDialog.setMessage(getString(R.string.scanning_epub));
        registerForContextMenu(this.listView);

        this.listView.setOnItemClickListener(this::onItemClick);
        this.listView.setOnItemLongClickListener(this::onItemLongClick);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        refreshView();
    }


    private <A, B, C> void executeTask(QueueableAsyncTask<A, B, C> task, A... parameters) {
        setSupportProgressBarIndeterminateVisibility(true);
        this.taskQueue.executeTask(task, parameters);
    }

    */
/**
     * Triggered by the TaskQueue when all tasks are finished.
     *//*

    private void onTaskQueueEmpty() {
        LOG.debug("Got onTaskQueueEmpty()");
        setSupportProgressBarIndeterminateVisibility(false);
    }

    private void clearCoverCache() {
        for (Map.Entry<String, FastBitmapDrawable> draw : coverCache.entrySet()) {
            draw.getValue().destroy();
        }

        coverCache.clear();
    }

    private void onItemClick(AdapterView<?> parent, View view, int position,
                             long id) {

        if (config.getLongShortPressBehaviour() == Configuration.LongShortPressBehaviour.NORMAL) {
            this.bookAdapter.getResultAt(position).forEach(this::showBookDetails);
        } else {
            this.bookAdapter.getResultAt(position).forEach(this::openBook);
        }
    }

    private boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {

        if (config.getLongShortPressBehaviour() == Configuration.LongShortPressBehaviour.NORMAL) {
            this.bookAdapter.getResultAt(position).forEach(this::openBook);
        } else {
            this.bookAdapter.getResultAt(position).forEach(this::showBookDetails);
        }

        return true;
    }

    private Option<Drawable> getCover(LibraryBook book) {

        try {
            if (!coverCache.containsKey(book.getFileName())) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(book.getCoverImage(), 0, book.getCoverImage().length);
                FastBitmapDrawable drawable = new FastBitmapDrawable(bitmap);
                coverCache.put(book.getFileName(), drawable);
            }

            return option(coverCache.get(book.getFileName()));

        } catch (OutOfMemoryError outOfMemoryError) {
            clearCoverCache();
            return none();
        }
    }

    */
/**
     * Este diálogo debe ser igual al de ReadingFragment -> checkSampleLimit
     *//*

    private void showBookDetails(final LibraryBook libraryBook) {

        if (!isAdded() || libraryBook == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.book_details);
        LayoutInflater inflater = PlatformUtil.getLayoutInflater(getActivity());

        View layout = inflater.inflate(R.layout.book_details, null);
        builder.setView(layout);

        ImageView coverView = (ImageView) layout.findViewById(R.id.coverImage);

        if (libraryBook.getCoverImage() != null) {
            Drawable coverDrawable = getCover(libraryBook).getOrElse(
                    getResources().getDrawable(R.drawable.cloud_refresh));

            coverView.setImageDrawable(coverDrawable);
        }

        TextView titleView = (TextView) layout.findViewById(R.id.titleField);
        TextView authorView = (TextView) layout.findViewById(R.id.authorField);
        TextView descriptionView = (TextView) layout.findViewById(R.id.bookDescription);

        titleView.setText(libraryBook.getTitle());
        String authorText = String.format(getString(R.string.book_by),
                libraryBook.getAuthor().getFirstName() + " "
                        + libraryBook.getAuthor().getLastName());
        authorView.setText(authorText);

        HtmlSpanner spanner = new HtmlSpanner();
        spanner.unregisterHandler("img"); //We don't want to render images

        descriptionView.setText(spanner.fromHtml(libraryBook.getDescription()));

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(R.string.read, (dialog, which) -> openBook(libraryBook));

        builder.show();
    }

    private void openBook(LibraryBook libraryBook) {

        //Primero reviso si el archivo está descargado:
        if (libraryBook.getFileName().matches("http://(.*)")) {

            //Hay red?
            if (!isNetworkAvailable()) {
                dialogFactory.buildAboutNoInternetDialog().show();
                return;
            }

            triggerDownloadFile(libraryBook);


        } else {
            this.showBookView(libraryBook);
        }
    }

    private void triggerDownloadFile(LibraryBook libraryBook) {

        //Download the file Via Parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book");
        query.whereEqualTo("title", libraryBook.getTitle());

        ParseObject book;

        try {
            book = query.getFirst();
        } catch (ParseException e) {
            LOG.debug("PARSE EXCEPTION " + e.getMessage());
            dialogFactory.buildAboutNoInternetDialog().show();
            return;
        }

        ParseFile file = book.getParseFile("container");

        try {
            ProgressDialog dialog = new ProgressDialog(this.getActivity());
            dialog.setMessage(getString(R.string.downloading));
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    file.cancel();
                    dialog.dismiss();
                }
            });

            dialog.show();

            LibraryFragment currentInstance = this;
            file.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    dialog.dismiss();
                    if (e == null) {
                        String filename = book.getObjectId() + ".epub";
                        FileOutputStream outputStream;

                        try {

                            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(data);
                            outputStream.close();

                            //Actualizar el epub en el Servicio
                            QueryResult<LibraryBook> allByTitle = libraryService.findAllByTitle(libraryBook.getTitle());
                            if (allByTitle.getSize() != 1) return;

                            libraryService.updateFilename(libraryBook.getFileName(), context.getFileStreamPath(filename).getAbsolutePath());
                            libraryBook.setFileName(context.getFileStreamPath(filename).getAbsolutePath());
                            LOG.debug("EBOOK PATH: " + libraryBook.getFileName());

                            //Listo - Mostrar Vista
                            currentInstance.showBookView(libraryBook);

                        } catch (Exception bookSavingException) {
                        }
                    } else {
                        LOG.debug("PARSE EXCEPTION Downloading book" + e.getMessage());
                    }
                }
            }, new ProgressCallback() {
                public void done(Integer percentDone) {
                    dialog.setProgress(percentDone);
                }
            });
        } catch (Exception e) {
        }
    }

    private void showBookView(LibraryBook libraryBook) {

        Intent intent = new Intent(getActivity(), ReadingActivity.class);

        intent.setData(Uri.parse(libraryBook.getFileName()));
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().startActivityIfNeeded(intent, 99);

    }

    private void buyBook(LibraryBook libraryBook) {

		*/
/*if (mHelper != null) mHelper.dispose();
        mHelper = null;

		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlcB+0DB0WYyC4T/662jKd9r92VcHSpNKCmlq/tmxuWKMnp+L5M4Nln6qMBpzxhC6mya/JbhI497v/vwmgdgShcDDzFQuzbkvI7+d/5eLyhVdSoG5ZeFVC4dPSM6i8W0UWJk/k+OKn6IuMGviKx0peIJV7bKIjxnqv7CpYIrCLHScpxWL7WBIjha9QuhkavNPTENc2fvG1bl+u/0BB/wxmg726gkejlaZA2cQ3SMh7VkB2KGeStIcTjR6pASWc5zsarZ0Plws0NEnJpiTZLifQDIPvSMGWXdKw9e1DK5B/QRVPWhm6x3KCcB5txqZrqu1QYUh3/1CdU8ooUfll/slOwIDAQAB";
		mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					LOG.error("Problem setting up In-app Billing: " + result);
					mHelper.dispose();
					mHelper = null;
				} else {
					LOG.debug("IAP Buy Book: " + libraryBook.getStoreProductID());
					IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
							= new IabHelper.OnIabPurchaseFinishedListener() {
						public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
							if (result.isFailure()) {
								LOG.debug("Error purchasing: " + result);
								return;
							} else {
								LOG.debug("Success purchasing: " + purchase.getSku());
								libraryBook.setPurchased(1);
								//Store the Purchased
								libraryService.updatePurchased(libraryBook.getFileName(), libraryBook.getPurchased());
							}
						}
					};
					mHelper.launchPurchaseFlow(getActivity(), libraryBook.getStoreProductID(), 10001,
							mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
				}
			}
		});*//*

    }

    private void startImport(File startFolder, boolean copy) {

        ImportTask importTask = new PatagoniaImportTask(context, libraryService, this, config, copy, false);
        importDialog.setOnCancelListener(importTask);
        importDialog.show();

        this.oldKeepScreenOn = listView.getKeepScreenOn();
        listView.setKeepScreenOn(true);

        this.taskQueue.clear();
        executeTask(importTask, startFolder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.intentCallBack != null) {
            this.intentCallBack.onResult(resultCode, data);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.library_menu, menu);
        onMenuPress(menu, R.id.scan_books).thenDo(this::showImportDialog);
        onMenuPress(menu, R.id.contact_us).thenDo(this::showContactUs);
        onMenuPress(menu, R.id.about).thenDo(dialogFactory.buildAboutDialog()::show);
        onMenuPress(menu, R.id.prefs).thenDo(this::startPreferences);
        onMenuPress(menu, R.id.profile_day).thenDo(() -> switchToColourProfile(ColourProfile.DAY));
        onMenuPress(menu, R.id.profile_night).thenDo(() -> switchToColourProfile(ColourProfile.NIGHT));

    }

    private void startPreferences() {
        LibraryActivity libraryActivity = (LibraryActivity) getActivity();
        libraryActivity.startPreferences();
    }


    private void switchToColourProfile(ColourProfile profile) {
        config.setColourProfile(profile);
        Intent intent = new Intent(getActivity(), LibraryActivity.class);
        startActivity(intent);
        onStop();
        getActivity().finish();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        boolean bookCaseActive = config.getLibraryView() == LibraryView.BOOKCASE;

        menu.findItem(R.id.profile_day).setVisible(config.getColourProfile() == ColourProfile.NIGHT);
        menu.findItem(R.id.profile_night).setVisible(config.getColourProfile() == ColourProfile.DAY);
    }

    private void showImportDialog() {

        Option<File> storageBase = config.getStorageBase();
        if (isEmpty(storageBase)) {
            return;
        }

        File folderToScan;
        File default_storage = storageBase.unsafeGet();
        folderToScan = new File(default_storage.getAbsolutePath());
        startImport(folderToScan, true);

    }

    private void showContactUs() {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@ebookspatagonia.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Contacto Patagonia Ebooks");

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("import_q", askedUserToImport);
    }

    @Override
    public void onStop() {
        this.libraryService.close();
        this.waitDialog.dismiss();
        this.importDialog.dismiss();
		*/
/*if (mHelper != null) mHelper.dispose();
		mHelper = null;*//*

        super.onStop();
    }

    public void onBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onPause() {

        this.bookAdapter.clear();
        //We clear the list to free up memory.

        this.taskQueue.clear();
        this.clearCoverCache();

        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        LibrarySelection lastSelection = LibrarySelection.BY_TITLE;
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        checkImport();
    }

    @Override
    public void importCancelled(int booksImported, List<String> failures, boolean emptyLibrary, boolean silent) {
        LOG.debug("Got importCancelled() ");
        afterImport(booksImported, failures, emptyLibrary, silent, true);
    }

    @Override
    public void importComplete(int booksImported, List<String> errors, boolean emptyLibrary, boolean silent) {
        LOG.debug("Got importComplete() ");
        afterImport(booksImported, errors, emptyLibrary, silent, false);
    }

    private void afterImport(int booksImported, List<String> errors, boolean emptyLibrary, boolean silent,
                             boolean cancelledByUser) {

        if (!isAdded() || getActivity() == null) {
            return;
        }

        if (silent) {
            if (booksImported > 0) {
                //Schedule refresh without clearing the queue
                executeTask(new LoadBooksTask(LibrarySelection.BY_TITLE));
            }
            return;
        }

        importDialog.hide();

        //If the user cancelled the import, don't bug him/her with alerts.
        if ((!errors.isEmpty())) {
			*/
/*
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.import_errors);

            builder.setItems( errors.toArray(new String[errors.size()]), null );

            builder.setNeutralButton(android.R.string.ok, (dialog, which) -> dialog.dismiss() );

            builder.show();*//*

        }

        listView.setKeepScreenOn(oldKeepScreenOn);

        if (booksImported > 0) {

            loadView(LibrarySelection.BY_TITLE, "importComplete()");

        } else if (!cancelledByUser) {

            loadView(LibrarySelection.BY_TITLE, "importComplete()");

        }

    }


    @Override
    public void importFailed(String reason, boolean silent) {

        LOG.debug("Got importFailed()");

        if (silent || !isAdded() || getActivity() == null) {
            return;
        }

        importDialog.hide();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.import_failed);
        builder.setMessage(reason);
        builder.setNeutralButton(android.R.string.ok, null);
        builder.show();
    }

    @Override
    public void importStatusUpdate(String update, boolean silent) {

        if (silent || !isAdded() || getActivity() == null) {
            return;
        }

        importDialog.setMessage(update);
    }


    */
/**
     * Based on example found here:
     * http://www.vogella.de/articles/AndroidListView/article.html
     *
     * @author work
     *//*

    private class BookListAdapter extends KeyedResultAdapter {

        private Context context;

        public BookListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public View getView(int index, final LibraryBook book, View convertView,
                            ViewGroup parent) {

            View rowView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.book_row, parent, false);
            } else {
                rowView = convertView;
            }


            final ImageView imageView = (ImageView) rowView.findViewById(R.id.bookCover);

            loadCover(imageView, book, index);

            return rowView;
        }

    }

    private void loadView(LibrarySelection selection, String from) {
        LOG.debug("Loading view: " + selection + " from " + from);
        this.taskQueue.clear();
        executeTask(new LoadBooksTask(selection));


    }

    private void refreshView() {
        loadView(LibrarySelection.BY_TITLE, "refreshView()");
        checkImport();
    }

    protected void checkImport() {

        if (this.libraryService.findAllByTitle("").getSize() == 0) {
            LOG.debug("IMPORTAR FORZADO");
            this.showImportDialog();
        }

        if (this.bookAdapter.getCount() == 0) {
            LOG.debug("IMPORTAR FORZADO");
            this.showImportDialog();
        }

    }

    private void loadCover(ImageView imageView, LibraryBook book, int index) {
        Drawable draw = coverCache.get(book.getFileName());

        if (draw != null) {
            imageView.setImageDrawable(draw);
        } else {

            imageView.setImageDrawable(backupCover);

            if (book.getCoverImage() != null) {
                callbacks.add(new CoverCallback(book, index, imageView));
            }
        }
    }

    private class CoverScrollListener implements AbsListView.OnScrollListener {

        private Runnable lastRunnable;
        private Character lastCharacter;

        private Drawable holoDrawable;

        public CoverScrollListener() {
            try {
                this.holoDrawable = getResources().getDrawable(R.drawable.list_activated_holo);
            } catch (IllegalStateException i) {
                //leave it null
            }
        }

        @Override
        public void onScroll(AbsListView view, final int firstVisibleItem,
                             final int visibleItemCount, final int totalItemCount) {

            if (visibleItemCount == 0) {
                return;
            }

            if (this.lastRunnable != null) {
                handler.removeCallbacks(lastRunnable);
            }

            this.lastRunnable = () -> {

                if (bookAdapter.isKeyed()) {

                    String key = bookAdapter.getKey(firstVisibleItem).getOrElse("");

                    if (key.length() > 0) {
                        Character keyChar = toUpperCase(key.charAt(0));

                        if (keyChar.equals(lastCharacter)) {

                            lastCharacter = keyChar;
                            List<Character> alphabet = bookAdapter.getAlphabet();

                        }
                    }
                }

                List<CoverCallback> localList = new ArrayList<>(callbacks);
                callbacks.clear();

                int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;

                LOG.debug("Loading items " + firstVisibleItem + " to " + lastVisibleItem + " of " + totalItemCount);

                for (CoverCallback callback : localList) {
                    if (callback.viewIndex >= firstVisibleItem && callback.viewIndex <= lastVisibleItem) {
                        callback.run();
                    }
                }

            };

            handler.postDelayed(lastRunnable, 550);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }

    private class CoverCallback {
        protected LibraryBook book;
        protected int viewIndex;
        protected ImageView view;

        public CoverCallback(LibraryBook book, int viewIndex, ImageView view) {
            this.book = book;
            this.view = view;
            this.viewIndex = viewIndex;
        }

        public void run() {
            try {
                getCover(book).forEach(view::setImageDrawable);
            } catch (IllegalStateException i) {
                //Do nothing, happens when we're no longer attached.
            }
        }
    }


    private class BookCaseAdapter extends KeyedResultAdapter {

        @Override
        public View getView(final int index, final LibraryBook object, View convertView,
                            ViewGroup parent) {

            View result;

            if (convertView == null) {
                LayoutInflater inflater = PlatformUtil.getLayoutInflater(getActivity());
                result = inflater.inflate(R.layout.bookcase_row, parent, false);

            } else {
                result = convertView;
            }

            result.setTag(index);

            result.setOnClickListener(v -> LibraryFragment.this.onItemClick(null, null, index, 0));
            result.setOnLongClickListener(v -> LibraryFragment.this.onItemLongClick(null, null, index, 0));

            final ImageView image = (ImageView) result.findViewById(R.id.bookCover);
            image.setImageDrawable(backupCover);

            loadCover(image, object, index);

            return result;
        }

    }

    private void setSupportProgressBarIndeterminateVisibility(boolean enable) {
        SherlockFragmentActivity activity = getSherlockActivity();
        if (activity != null) {
            LOG.debug("Setting progress bar to " + enable);
            activity.setSupportProgressBarIndeterminateVisibility(enable);
        } else {
            LOG.debug("Got null activity.");
        }
    }

    private class AlphabetAdapter extends ArrayAdapter<Character> {

        private List<Character> data;

        private Character highlightChar;

        public AlphabetAdapter(Context context, int layout, int view, List<Character> input) {
            super(context, layout, view, input);
            this.data = input;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            Character tag = data.get(position);
            view.setTag(tag);

            if (tag.equals(highlightChar)) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_activated_holo));
            } else {
                view.setBackgroundDrawable(null);
            }

            return view;
        }

        public void setHighlightChar(Character highlightChar) {
            this.highlightChar = highlightChar;
        }

        public Character getHighlightChar() {
            return highlightChar;
        }
    }

    private void loadQueryData(QueryResult<LibraryBook> result) {
        if (!isAdded() || getActivity() == null) {
            return;
        }

        bookAdapter.setResult(result);

    }

    private class LoadBooksTask extends QueueableAsyncTask<String, Integer, QueryResult<LibraryBook>> {

        private Configuration.LibrarySelection sel;
        private String filter;

        public LoadBooksTask(LibrarySelection selection) {
            this.sel = selection;
        }

        public LoadBooksTask(LibrarySelection selection, String filter) {
            this(selection);
            this.filter = filter;
        }

        @Override
        public void doOnPreExecute() {
            if (this.filter == null) {
                coverCache.clear();
            }
        }

        @Override
        public Option<QueryResult<LibraryBook>> doInBackground(String... params) {
            return some(libraryService.findAllByTitle(this.filter));
        }

        @Override
        public void doOnPostExecute(Option<QueryResult<LibraryBook>> result) {

            result.match(r -> {

                loadQueryData(r);

                askedUserToImport = true;
            }, () -> {
                //Toast.makeText(context, R.string.library_failed, Toast.LENGTH_SHORT).show();
            });

        }

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    return true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    return true;
        }

        return false;
    }

}
*/
