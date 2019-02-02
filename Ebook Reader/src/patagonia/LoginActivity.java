package patagonia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import net.nightwhistler.pageturner.PageTurner;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.activity.LibraryActivity;
import net.nightwhistler.ui.DialogFactory;

/**
 * Created by nadirhussain on 25/08/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditor, passwordEditor;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            goToMainActivity();
        } else {
            setContentView(R.layout.activity_signin);
            initViews();
        }
    }

    private void initViews() {
        usernameEditor = (EditText) findViewById(R.id.emailEditor);
        passwordEditor = (EditText) findViewById(R.id.passwordEditor);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginClickListener);
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!PageTurner.getInstance().isConnected()) {
                DialogFactory.buildAboutNoInternetDialog(LoginActivity.this).show();
            } else {
                login();
            }
        }
    };

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
        startActivity(intent);
        finish();
    }

    private void login() {
        String userName = usernameEditor.getText().toString();
        String password = passwordEditor.getText().toString();

        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter both username and password", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            String error = "";
                            // there was an error
                            if (password.length() < 6) {
                                error = getString(R.string.minimum_password);
                            } else {
                                error = getString(R.string.auth_failed);
                            }
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                        } else {
                            goToMainActivity();
                        }
                    }
                });
    }

}
