package patagonia;

/**
 * Created by nadirhussain on 25/08/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ui.ParseLoginBuilder;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.pageturner.PageTurner;
import net.nightwhistler.pageturner.PlatformUtil;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.activity.LibraryActivity;
import net.nightwhistler.pageturner.activity.ReadingActivity;
import net.nightwhistler.pageturner.fragment.PatagoniaLibraryFragment;
import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;
import net.nightwhistler.ui.DialogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatagoniaFbLibraryFragment extends Fragment {

    private PatagoniaFirebaseAdapter adapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth auth;
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

    public PatagoniaFbLibraryFragment() {
        mUseAutores = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DbDebug","OnCreate");
        mTransferUtility = Utils.getTransferUtility(getActivity());
        libraryService = new PatagoniaSqlLiteLibraryService(getActivity());
        Log.d("DbDebug","After libraryService");
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onStop() {
        this.libraryService.close();
        if (firebaseAuthStateListener != null) {
            auth.removeAuthStateListener(firebaseAuthStateListener);
        }

        super.onStop();
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
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 2 * Config.GRID_COLUMNS);

        showLoadingDialog();
        loadBooksFromFirebase();
    }

    private void loadBooksFromFirebase() {
        Log.d("DbDebug","Firebase load");
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference booksReference = mFirebaseDatabaseReference.child("users").child(uId).child("books");
        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DbDebug","Firebase Success load");
                ArrayList<String> isbnBookList = new ArrayList<String>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String eiSbn = (String) messageSnapshot.getKey();
                    isbnBookList.add(eiSbn);
                }
                if (progress != null) {
                    progress.cancel();
                }
                adapter = new PatagoniaFirebaseAdapter(isbnBookList, getActivity());
                adapter.setOnBookItemClickListener(new mGridItemClickListener());
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DbDebug","Firebase fail load");
                if (progress != null) {
                    progress.cancel();
                }
                if (!PageTurner.getInstance().isConnected()) {
                    DialogFactory.buildAboutNoInternetDialog(context).show();
                }
            }
        });
    }

    private void showLoadingDialog() {
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait while data is being loaded");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }


    public void onBackPressed() {
        getActivity().finish();
    }

    private class mGridItemClickListener implements PatagoniaParse.OnBookItemClickListener {

        @Override
        public void onGridItemClick(View view, SimpleBook book, int position) {
            showBookDetails(book);
        }
    }


    private void showBookDetails(final SimpleBook book) {

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
                .load(book.getCoverPhoto())
                .placeholder(R.drawable.unknown_cover)
                .thumbnail(0.3f)
                .centerCrop()
                .crossFade()
                .into(coverView);

        TextView titleView = (TextView) layout.findViewById(R.id.titleField);
        TextView authorView = (TextView) layout.findViewById(R.id.authorField);
        TextView descriptionView = (TextView) layout.findViewById(R.id.bookDescription);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());

        HtmlSpanner spanner = new HtmlSpanner();
        spanner.unregisterHandler("img"); //We don't want to render images

        String bookDescription = book.getDescription();
        if (bookDescription != null) {
            descriptionView.setText(spanner.fromHtml(bookDescription));
        }

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.setPositiveButton(R.string.read, (dialog, which) -> openBook(book));
        builder.show();
    }

    private void openBook(SimpleBook book) {
        QueryResult<LibraryBook> allByTitle = this.libraryService.findAllByTitle(book.getTitle());
        if (allByTitle != null && allByTitle.getSize() > 0) {
            LibraryBook libraryBook = allByTitle.getItemAt(0);
            showBookView(libraryBook);
        } else {
            if (!PageTurner.getInstance().isConnected()) {
                DialogFactory.buildAboutNoInternetDialog(context).show();
                return;
            }

            triggerDownloadFile(book);
        }
    }

    private void triggerDownloadFile(SimpleBook book) {
        downloadFile(book);
    }

    private void downloadFile(final SimpleBook book) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Descargando " + book.getTitle() + "...");
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgress(0);

        String filename = book.geteISBN() + ".epub";
        File file = new File(context.getFileStreamPath(filename).getAbsolutePath());
        String key = "Books/" + book.geteISBN() + ".epub";
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
                    finishBookDownload(book, filename);
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

    private void finishBookDownload(SimpleBook book, String fileName) {
        try {
            String downloadFilePath = context.getFileStreamPath(fileName).getAbsolutePath();
            Book downloadedBook = new Book();

            downloadedBook.getMetadata().addTitle(book.getTitle());
            downloadedBook.getMetadata().getAuthors().add(new Author(book.getTitle()));

            downloadedBook.getMetadata().getDescriptions().clear();

            List<String> descriptions = new ArrayList<>();
            descriptions.add(book.getDescription());

            downloadedBook.getMetadata().setDescriptions(descriptions);
            libraryService.storeBookByCoverFile(downloadFilePath, null, downloadedBook, false, false);
            openBook(book);
            Log.d(LOG_TAG, "Downloaded EBOOK PATH: " + downloadFilePath);
        } catch (Exception exception) {
            Log.d(LOG_TAG, "Downloaded Book not saved to library DB: " + exception);
        }


    }

    private void showBookView(LibraryBook libraryBook) {
        Intent intent = new Intent(getActivity(), ReadingActivity.class);

        intent.setData(Uri.parse(libraryBook.getFileName()));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().startActivityIfNeeded(intent, 99);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.library_menu, menu);
        if (mUseAutores == false) {
            menu.removeItem(R.id.show_autores);
            menu.removeItem(R.id.show_login);
            menu.removeItem(R.id.show_logout);
        } else {
            menu.removeItem(R.id.show_login);
        }

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
            loadBooksFromFirebase();
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

        PatagoniaDeleteBooksTask deleteTask = new PatagoniaDeleteBooksTask(libraryService, new PatagoniaDeleteBooksTask.PatagoniaAsyncResponse() {
            @Override
            public void deleteFinish() {
                auth.signOut();
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

    private FirebaseAuth.AuthStateListener firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                if (progress != null) {
                    progress.cancel();
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }
    };


}

