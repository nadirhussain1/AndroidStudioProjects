package com.nippt.arabicamharicdictionary.free;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

class CustomKeyboard {
    private Activity mHostActivity;
    private KeyboardView mKeyboardView;
    //private OnKeyboardActionListener mOnKeyboardActionListener;

    public static final int CodeDelete = -5;
    public static final int CodeLeft = 55002;
    public static final int CodeRight = 55003;
    public static final int CodeSarch = -3;


    OnKeyboardActionListener actionListener = new OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            View focusCurrent = CustomKeyboard.this.mHostActivity.getWindow().getCurrentFocus();
            if (focusCurrent != null && focusCurrent instanceof  EditText) {
                EditText edittext = (EditText) focusCurrent;
                Editable editable = edittext.getText();
                int start = edittext.getSelectionStart();
                if (primaryCode == CodeDelete) {
                    if (editable != null && start > 0) {
                        editable.delete(start - 1, start);
                    }
                } else if (primaryCode == CodeLeft) {
                    if (start > 0) {
                        edittext.setSelection(start - 1);
                    }
                } else if (primaryCode == CodeSarch) {
                    CustomKeyboard.this.hideCustomKeyboard();
                } else if (primaryCode != CodeRight) {
                    editable.insert(start, Character.toString((char) primaryCode));
                } else if (start < edittext.length()) {
                    edittext.setSelection(start + 1);
                }
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };


    OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                CustomKeyboard.this.showCustomKeyboard(view);
            } else {
                CustomKeyboard.this.hideCustomKeyboard();
            }
        }
    };
    OnClickListener editClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            showCustomKeyboard(view);
        }
    };
    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            EditText edittext = (EditText) view;
            int inType = edittext.getInputType();
            edittext.setInputType(0);
            edittext.onTouchEvent(motionEvent);
            edittext.setInputType(inType);
            return true;
        }
    };


    public CustomKeyboard(Activity host, int viewid, int layoutid) {
        this.mHostActivity = host;
        this.mKeyboardView = (KeyboardView) this.mHostActivity.findViewById(viewid);
        this.mKeyboardView.setKeyboard(new Keyboard(this.mHostActivity, layoutid));
        this.mKeyboardView.setPreviewEnabled(false);
        this.mKeyboardView.setOnKeyboardActionListener(actionListener);
        this.mHostActivity.getWindow().setSoftInputMode(3);
    }

    public boolean isCustomKeyboardVisible() {
        return this.mKeyboardView.getVisibility() == View.VISIBLE;
    }

    public void showCustomKeyboard(View v) {
        this.mKeyboardView.setVisibility(View.VISIBLE);
        this.mKeyboardView.setEnabled(true);
        if (v != null) {
            ((InputMethodManager) this.mHostActivity.getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void hideCustomKeyboard() {
        this.mKeyboardView.setVisibility(View.INVISIBLE);
        this.mKeyboardView.setEnabled(false);
    }

    public void registerEditText(int resid) {
        EditText edittext = (EditText) this.mHostActivity.findViewById(resid);
        edittext.setOnFocusChangeListener(focusChangeListener);
        edittext.setOnClickListener(editClickListener);
        edittext.setOnTouchListener(touchListener);
        edittext.setInputType(edittext.getInputType() | AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
    }
}
