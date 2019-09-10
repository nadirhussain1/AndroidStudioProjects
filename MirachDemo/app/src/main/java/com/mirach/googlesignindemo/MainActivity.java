package com.mirach.googlesignindemo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_TOKEN_INFO = "key_token_info";
    public static final String KEY_TOKEN_VALUE = "key_token_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tokenInfoLabelView = findViewById(R.id.tokenInfoLabelView);
        EditText tokenEditor = findViewById(R.id.tokenEditor);


        if (getIntent() != null) {
            String tokenInfo = getIntent().getStringExtra(KEY_TOKEN_INFO);
            String tokenValue = getIntent().getStringExtra(KEY_TOKEN_VALUE);

            tokenInfoLabelView.setText(tokenInfo);
            tokenEditor.setText(tokenValue);
        }

    }
}
