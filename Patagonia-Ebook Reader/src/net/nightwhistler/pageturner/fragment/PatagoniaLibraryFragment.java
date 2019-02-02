package net.nightwhistler.pageturner.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.pageturner.PageTurner;
import net.nightwhistler.pageturner.PlatformUtil;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.activity.LibraryActivity;
import net.nightwhistler.pageturner.activity.ReadingActivity;
import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;
import net.nightwhistler.ui.DialogFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import patagonia.Config;
import patagonia.DownloadEvent;
import patagonia.ParseBook;
import patagonia.ParseBookGridAdapter;
import patagonia.ParseRecyclerQueryAdapter;
import patagonia.PatagoniaAutoresActivity;
import patagonia.PatagoniaDeleteBooksTask;
import patagonia.PatagoniaParse;
import patagonia.PatagoniaSqlLiteLibraryService;
import patagonia.PatagoniaStore;
import patagonia.UserBook;
import patagonia.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatagoniaLibraryFragment extends Fragment {

    private ParseBookGridAdapter adapter;
    private final String LOG_TAG = PatagoniaLibraryFragment.class.getSimpleName();

    private LibraryService libraryService;
    private Context context;
    private ProgressDialog progress;
    private AlertDialog mLogoutAlert;
    private PatagoniaStore mStore;
    private RecyclerView mRecyclerView;
    private Boolean mUseAutores;
    public static final int PATAGONIA_AUTORS_BOOK_REQUEST_CODE = 0;

    private TransferUtility mTransferUtility;

    public PatagoniaLibraryFragment() {
        mUseAutores = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransferUtility = Utils.getTransferUtility(getActivity());
        libraryService = new PatagoniaSqlLiteLibraryService(getActivity());
        DownloadEvent.executeCheckDownloadEventsTask(libraryService, getActivity().getPreferences(Context.MODE_PRIVATE));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_patagonia_library, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.books_container);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Config.GRID_COLUMNS));
        QueryResult<LibraryBook> allByTitle = this.libraryService.findAllByTitle("");
        if (allByTitle.getSize() <= 0 && !PageTurner.getInstance().isConnected()) {
            DialogFactory.buildAboutNoInternetDialog(context).show();
        } else {
            setupAdapter();
        }

    }

    private void setupAdapter() {
        mStore = new PatagoniaStore();
        adapter = new ParseBookGridAdapter(ParseBook.getQueryFactory(), false, getActivity(), libraryService);
        adapter.setOnGridItemClickListener(new mGridItemClickListener());
        adapter.addOnQueryLoadListener(new mQueryLoadListener());
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 2 * Config.GRID_COLUMNS);
        mRecyclerView.setAdapter(adapter);
    }

    private class mQueryLoadListener implements ParseRecyclerQueryAdapter.OnQueryLoadListener<ParseBook> {

        @Override
        public void onLoaded(List<ParseBook> objects, Exception e) {

            if (isAdded()) {
                adapter.notifyDataSetChanged();
            }

            if (objects.size() <= 0) {
                progress = new ProgressDialog(context);
                progress.setMessage("Descargando libros...");
                progress.show();
            }

            mStore.setBooksObjectsListener(new PatagoniaStore.BookObjectsListener() {
                @Override
                public void bookObjectsCallBack(Boolean shouldReload) {
                    if (shouldReload) {
                        adapter.loadObjects();
                    }

                    if (progress != null) {
                        progress.dismiss();
                    }
                }
            });
            mStore.getBooksFromParse();
        }

        @Override
        public void onLoading() {
            Log.d(LOG_TAG, "Loading from Parse ");
        }
    }

    public void onBackPressed() {
        getActivity().finish();
    }

    private class mGridItemClickListener implements PatagoniaParse.OnGridItemClickListener {
        @Override
        public void onGridItemClick(View view, ParseBook book, int position) {
            showBookDetails(book);
        }
    }

    public void selectBookFromAutores(String bookId) {
        ParseQuery<ParseBook> query = ParseQuery.getQuery(ParseBook.class);
        query.fromLocalDatastore();
        query.whereEqualTo("objectId", bookId);
        query.getFirstInBackground(new GetCallback<ParseBook>() {
            @Override
            public void done(ParseBook parseBook, ParseException e) {
                if (e == null) {
                    showBookDetails(parseBook);
                }
            }
        });
    }

    private void showBookDetails(final ParseBook book) {

        if (book == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.book_details);
        LayoutInflater inflater = PlatformUtil.getLayoutInflater(getActivity());

        View layout = inflater.inflate(R.layout.book_details, null);
        builder.setView(layout);

        ImageView coverView = (ImageView) layout.findViewById(R.id.coverImage);

        Glide.with(getActivity())
                .load(book.getCoverUrl())
                .placeholder(R.drawable.unknown_cover)
                .thumbnail(0.3f)
                .centerCrop()
                .crossFade()
                .into(coverView);

        TextView titleView = (TextView) layout.findViewById(R.id.titleField);
        TextView authorView = (TextView) layout.findViewById(R.id.authorField);
        TextView descriptionView = (TextView) layout.findViewById(R.id.bookDescription);

        titleView.setText(book.getTitle());
        authorView.setText(book.getString("author"));

        HtmlSpanner spanner = new HtmlSpanner();
        spanner.unregisterHandler("img"); //We don't want to render images

        String bookDescription = book.getString("description");
        if (bookDescription != null) {
            descriptionView.setText(spanner.fromHtml(bookDescription));
        }

        builder.setNegativeButton(android.R.string.cancel, null);

        LibraryBook libraryBook;
        QueryResult<LibraryBook> allByTitle = this.libraryService.findAllByTitle(book.getTitle());
        if (allByTitle.getSize() > 0) {
            libraryBook = allByTitle.getItemAt(0);

            if (libraryBook.getPurchased() == 0) {
                libraryBook.setPurchased(1);
                //Store the Purchased
                libraryService.updatePurchased(libraryBook.getFileName(), 1);
            }

            builder.setPositiveButton(R.string.read, (dialog, which) -> openBook(libraryBook));
            builder.show();

        } else {
            builder.show();
        }
    }

    private void openBook(LibraryBook libraryBook) {
        if (!libraryBook.isDownloaded()) {

            //Hay red?
            if (!PageTurner.getInstance().isConnected()) {
                DialogFactory.buildAboutNoInternetDialog(context).show();
                return;
            }

            triggerDownloadFile(libraryBook);
        } else {
            showBookView(libraryBook);
        }
    }

    private void triggerDownloadFile(LibraryBook libraryBook) {
        Log.d(LOG_TAG, "Downloading " + libraryBook.getTitle());

        ParseQuery<ParseBook> query = ParseQuery.getQuery("Book");
        query.whereEqualTo("title", libraryBook.getTitle());
        query.fromLocalDatastore();
        query.ignoreACLs();
        query.getFirstInBackground(new GetCallback<ParseBook>() {
            @Override
            public void done(ParseBook book, ParseException e) {
                if (e == null) {

                    book.checkDownloadCredits(new GetCallback<UserBook>() {
                        @Override
                        public void done(UserBook userBook, ParseException e) {
                            if (e == null) {
                                if (userBook.haveCredits()) {
                                    downloadFile(libraryBook, book, userBook);
                                } else {
                                    showNoCreditsDialog();
                                }
                            } else {

                                UserBook.initCredits(ParseUser.getCurrentUser(), book, new UserBook.UserBookInitCreditsCallback() {
                                    @Override
                                    public void done(UserBook newUserBook, ParseException e) {
                                        if (e == null) {
                                            downloadFile(libraryBook, book, newUserBook);
                                        } else {
                                            String error = "Error creating credits object";
                                            Log.e(LOG_TAG, error, e);
                                            showError(error);
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Log.e(LOG_TAG, "Error getting Parse Book", e);
                }
            }
        });

    }

    private void downloadFile(LibraryBook libraryBook, ParseBook book, UserBook userBook) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Descargando " + libraryBook.getTitle() + "...");
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgress(0);

        String filename = book.getObjectId() + ".epub";
        File file = new File(context.getFileStreamPath(filename).getAbsolutePath());
        String key = "Books/" + book.getEISBN() + ".epub";
        TransferObserver observer = mTransferUtility.download(Config.BUCKET_NAME, key, file);
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d(LOG_TAG, "S3 Download StateChanged: " + id + ", " + state);
                if (state == TransferState.IN_PROGRESS) {
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTransferUtility.cancel(id);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }

                if (state == TransferState.COMPLETED || state == TransferState.FAILED) {
                    dialog.dismiss();
                }

                if (state == TransferState.COMPLETED) {
                    finishBookDownload(libraryBook, filename, userBook);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                /*if (dialog.getMax() == 100) {
                    dialog.setMax((int) bytesTotal);
                }
                dialog.setProgress((int)bytesCurrent);*/
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(LOG_TAG, "Error downloading epub from S3: " + id, ex);
            }
        });
    }

    private void finishBookDownload(LibraryBook libraryBook, String filename, UserBook userBook) {
        QueryResult<LibraryBook> allByTitle = libraryService.findAllByTitle(libraryBook.getTitle());
        if (allByTitle.getSize() != 1) return;

        libraryService.updateFilename(libraryBook.getFileName(), context.getFileStreamPath(filename).getAbsolutePath());
        libraryBook.setFileName(context.getFileStreamPath(filename).getAbsolutePath());
        Log.d(LOG_TAG, "Downloaded EBOOK PATH: " + libraryBook.getFileName());
        try {
            userBook.consumeCredits(libraryBook.getDownloadDate());
        } catch (URISyntaxException consumeCreditsException) {
            Log.e(LOG_TAG, "Error consuming credits", consumeCreditsException);
        }
        showBookView(libraryBook);
    }

    private void showBookView(LibraryBook libraryBook) {
        Intent intent = new Intent(getActivity(), ReadingActivity.class);

        intent.setData(Uri.parse(libraryBook.getFileName()));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().startActivityIfNeeded(intent, 99);
    }

    @Override
    public void onStop() {
        this.libraryService.close();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.library_menu, menu);
        if (mUseAutores == false) {
            menu.removeItem(R.id.show_autores);
            menu.removeItem(R.id.show_login);
            menu.removeItem(R.id.show_logout);
        } else {

            if (ParseUser.getCurrentUser() == null) {
                menu.removeItem(R.id.show_logout);
            } else {
                menu.removeItem(R.id.show_login);
            }
        }
        //  super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_books:
                reloadBooks();
                break;
            case R.id.contact_us:
                showContactUs();
                break;
            case R.id.about:
                DialogFactory.buildAboutDialog(context).show();
                break;
            case R.id.prefs:
                startPreferences();
                break;
            case R.id.menu_logout:
                showLogoutDialog();
                break;
            case R.id.show_autores:
                showAutores();
                break;

        }
        return true;
    }


    private void showLogin() {
        ParseLoginBuilder builder = new ParseLoginBuilder(getActivity());
        startActivityForResult(builder.build(), 0);
    }

    private void showAutores() {
        Intent intent = new Intent(getActivity(), PatagoniaAutoresActivity.class);
        getActivity().startActivityForResult(intent, PATAGONIA_AUTORS_BOOK_REQUEST_CODE);
    }

    public void reloadBooks() {
        if (PageTurner.getInstance().isConnected()) {
            setupAdapter();
        } else {
            DialogFactory.buildAboutNoInternetDialog(context).show();
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Deseas Cerrar Sesión?");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton("Cerrar Sesión", (dialog, which) -> logout());
        mLogoutAlert = builder.create();
        mLogoutAlert.show();
    }

    private void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(error);
        builder.setNegativeButton("OK", null);
        builder.setPositiveButton("Contactanos", (dialog, which) -> showContactUs());
        builder.create().show();
    }

    private void showNoCreditsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No hay más creditos de descarga");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton("Contactanos", (dialog, which) -> showContactUs());
        builder.create().show();
    }

    private void logout() {
        if (mLogoutAlert != null) {
            mLogoutAlert.dismiss();
        }

        progress = new ProgressDialog(context);
        progress.setMessage("Borrando libros...");
        progress.show();

        ParseUser.getCurrentUser().logOut();
        PatagoniaDeleteBooksTask deleteTask = new PatagoniaDeleteBooksTask(libraryService, new PatagoniaDeleteBooksTask.PatagoniaAsyncResponse() {
            @Override
            public void deleteFinish() {
                progress.dismiss();
                getActivity().finish();
            }
        });
        deleteTask.execute();
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

    private void startPreferences() {
        LibraryActivity libraryActivity = (LibraryActivity) getActivity();
        libraryActivity.startPreferences();
    }


}
