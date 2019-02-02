package com.brainpixel.cletracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brainpixel.cletracker.model.GlobalDataManager;
import com.brainpixel.cletracker.model.UserProfileDataModel;
import com.brainpixel.cletracker.utils.GlobalUtil;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.ProfileActivity;
import com.brainpixel.cletracker.views.SignUpActivity;
import com.brainpixel.cletracker.views.activities.BaseActivity;
import com.brainpixel.cletracker.views.activities.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

import static com.brainpixel.cletracker.R.id.loginPasswordEditor;

/**
 * Created by nadirhussain on 30/03/2017.
 */

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;


    @BindView(R.id.fbSignInButton)
    LoginButton fbSignInButton;
    @BindView(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(loginPasswordEditor)
    EditText passwordEditor;


    private CallbackManager mFacebookCallbackManager;
    private long mLastClickTime = 0;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSocialSdkSetup();
        bindSignInLayoutView();

        mAuth = FirebaseAuth.getInstance();
        // getHashKeyForFacebook();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void bindSignInLayoutView() {
        View view = View.inflate(this, R.layout.activity_login, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);

        fbSignInButton.setReadPermissions("email", "public_profile");
        fbSignInButton.registerCallback(mFacebookCallbackManager, facebookCallback);
        twitterLoginButton.setCallback(twitterSessionCallback);
    }

    private void initSocialSdkSetup() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
    }

    // This method is just to setup facebook initially...
    private void getHashKeyForFacebook() {
        try {

            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "Facebook Hash=" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (NameNotFoundException e) {
            Log.e("KeyHash", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash", e.toString());
        }

    }

    @OnClick(R.id.googleSignButton)
    public void googleSignInClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            Log.d("AudioRecorder", "startPlaying ignored");
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        doGoogleLogin();
    }

    @OnClick(R.id.signUpTextView)
    public void goToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.SignInButton)
    public void signInClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        doSignIn();
    }

    @OnEditorAction(loginPasswordEditor)
    public boolean passwordEditAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doSignIn();
            return true;
        }
        return false;
    }

    @OnClick(R.id.forgotPassTextView)
    public void forgotPasswordClicked() {
        showForgotPassEmailAlert();
    }

    private void doSignIn() {
        if (performInternetCheck()) {
            String email = emailEditor.getText().toString();
            String password = passwordEditor.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.fill_all_fields_msg));
                return;
            }
            if (!GlobalUtil.isEmailValid(email)) {
                GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.invalid_email_msg));
                return;
            }
            doLoginByEmail(email, password);
        }
    }

    private void doLoginByEmail(String email, String password) {
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            GlobalUtil.showMessageAlertWithOkButton(LoginActivity.this, getString(R.string.alert_message_title), getLoginErrorMessage(task));
                        }
                    }
                });
    }

    private void doGoogleLogin() {
        if (performInternetCheck()) {
            showProgressDialog();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                googleSignout();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                hideProgressDialog();
                displayMessageAlert(getString(R.string.login_failed_error_msg));
            }

        }

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            displayMessageAlert(getLoginCredentialsErrorMessage(task));
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        displayMessageAlert(getString(R.string.login_failed_error_msg));
    }

    private FacebookCallback facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            fbLogout();
            final AccessToken accessToken = loginResult.getAccessToken();
            handleFacebookAccessToken(accessToken);

        }

        @Override
        public void onCancel() {
            displayMessageAlert("Login Cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            String message = getString(R.string.login_failed_error_msg) + "\n" + error.getMessage();
            displayMessageAlert(message);
        }
    };
    private Callback<TwitterSession> twitterSessionCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            twitterLogout();
            handleTwitterSession(result.data);
        }

        @Override
        public void failure(TwitterException exception) {
            displayMessageAlert(getString(R.string.login_failed_error_msg));
        }
    };

    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            displayMessageAlert(getLoginCredentialsErrorMessage(task));
                        }
                    }
                });
    }

    private void handleTwitterSession(TwitterSession session) {
        showProgressDialog();
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token, session.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            displayMessageAlert(getLoginCredentialsErrorMessage(task));
                        }
                    }
                });
    }


    private FirebaseAuth.AuthStateListener mAuthListener = new AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                decideNextScreen(user.getUid());
            }
        }
    };

    private void decideNextScreen(String userId) {
        ValueEventListener readProfileDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfileDataModel userProfileDataModel = dataSnapshot.getValue(UserProfileDataModel.class);
                if (userProfileDataModel != null) {
                    GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel = userProfileDataModel;
                    showMainScreen();
                } else {
                    showProfileScreen();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profile").addListenerForSingleValueEvent(readProfileDataListener);
    }

    private void showProfileScreen() {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String getLoginErrorMessage(Task<AuthResult> task) {
        String error = "";
        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            error = getString(R.string.user_not_exists_error);
        } catch (FirebaseAuthInvalidCredentialsException e) {
            error = getString(R.string.wrong_password_error);
        } catch (Exception e) {
            error = getString(R.string.login_failed_error_msg);
        }
        return error;
    }

    private String getLoginCredentialsErrorMessage(Task<AuthResult> task) {
        String error = "";
        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            error = getString(R.string.user_not_exists_error);
        } catch (FirebaseAuthInvalidCredentialsException e) {
            error = getString(R.string.wrong_password_error);
        } catch (Exception e) {
            error = getString(R.string.login_failed_error_msg) + "\n" + e.getMessage();
        }
        return error;
    }

    public void showForgotPassEmailAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.forgot_password));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.single_editor_alert, null);
        final EditText emailInputEditor = (EditText) dialogView.findViewById(R.id.inputEditor);
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.done_label), null);
        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button doneButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                doneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailInputEditor.getText().toString();
                        if (TextUtils.isEmpty(email) || !GlobalUtil.isEmailValid(email)) {
                            GlobalUtil.displayToastInfoMessage(LoginActivity.this, getString(R.string.email_not_valid_message));
                        } else {
                            alertDialog.dismiss();
                            sendForgotPasswordEmail(email);
                        }
                    }
                });
            }
        });

        alertDialog.show();

    }

    private void sendForgotPasswordEmail(String email) {
        showProgressDialog();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            GlobalUtil.displayToastInfoMessage(LoginActivity.this, getString(R.string.reset_email_sent));
                        } else {
                            GlobalUtil.displayToastInfoMessage(LoginActivity.this, getString(R.string.reset_email_fail));
                        }
                    }
                });
    }

    private void googleSignout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    private void twitterLogout() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        TwitterCore.getInstance().logOut();
    }

    private void fbLogout() {
        LoginManager.getInstance().logOut();
    }


}
