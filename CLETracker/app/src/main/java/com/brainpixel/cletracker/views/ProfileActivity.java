package com.brainpixel.cletracker.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.model.Cycle;
import com.brainpixel.cletracker.model.GlobalDataManager;
import com.brainpixel.cletracker.model.UserProfileDataModel;
import com.brainpixel.cletracker.utils.CycleReqsManagerUtil;
import com.brainpixel.cletracker.utils.GlobalUtil;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.activities.BaseActivity;
import com.brainpixel.cletracker.views.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 04/04/2017.
 */

public class ProfileActivity extends BaseActivity {
    public static final String KEY_USER_PROFILE_DATA = "KEY_USER_PROFILE_DATA";

    @BindView(R.id.firstNameEditor)
    EditText firstNameEditor;
    @BindView(R.id.lNameEditor)
    EditText lastNameEditor;
    @BindView(R.id.barSpinner)
    Spinner barAdmissionSpinner;
    @BindView(R.id.dateOfBirthTextView)
    TextView birthDateTextView;
    @BindView(R.id.admissionDateTextView)
    TextView admissionDateTextView;

    private long mLastClickTime;
    private int birthYear, birthMonth, birthDay = -1;
    private int admissionYear, admissionMonth, admissionDay = -1;
    private String admissionDate = null;
    private String birthDate = null;

    private DatabaseReference firebaseDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_profile, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);


        if (GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel != null) { // In case of edit profile
            setPreviousValues(GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel);
        } else {
            initDate();
            if (getIntent() != null && getIntent().getExtras() != null) {
                UserProfileDataModel userProfileDataModel = (UserProfileDataModel) getIntent().getSerializableExtra(KEY_USER_PROFILE_DATA);
                setPreviousNames(userProfileDataModel);
            }
        }

        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setPreviousValues(UserProfileDataModel currentProfileDataModel) {
        if (currentProfileDataModel.getFirstName() != null) {
            firstNameEditor.setText(currentProfileDataModel.getFirstName());
            lastNameEditor.setText(currentProfileDataModel.getLastName());
        }
        if (!TextUtils.isEmpty(currentProfileDataModel.getBarAdmission())) {
            String state = currentProfileDataModel.getBarAdmission();
            String[] barsOptions = getResources().getStringArray(R.array.bar_states);
            int selectedPosition = 0;
            while (selectedPosition < barsOptions.length && !state.equalsIgnoreCase(barsOptions[selectedPosition])) {
                selectedPosition++;
            }
            barAdmissionSpinner.setSelection(selectedPosition);
        }

        if (!TextUtils.isEmpty(currentProfileDataModel.getDateOfBirth())) {
            birthDate = currentProfileDataModel.getDateOfBirth();
            admissionDate = currentProfileDataModel.getAdmissionDate();
            birthDateTextView.setText(birthDate);
            admissionDateTextView.setText(admissionDate);

            Calendar calendar = Calendar.getInstance(Locale.US);
            calendar.setTime(GlobalUtil.parseStringWithoutTimeToDate(birthDate));
            birthDay = calendar.get(Calendar.DAY_OF_MONTH);
            birthMonth = calendar.get(Calendar.MONTH);
            birthYear = calendar.get(Calendar.YEAR);

            calendar.setTime(GlobalUtil.parseStringWithoutTimeToDate(admissionDate));
            admissionDay = calendar.get(Calendar.DAY_OF_MONTH);
            admissionMonth = calendar.get(Calendar.MONTH);
            admissionYear = calendar.get(Calendar.YEAR);
        }

    }

    private void setPreviousNames(UserProfileDataModel userProfileDataModel) {
        firstNameEditor.setText(userProfileDataModel.getFirstName());
        lastNameEditor.setText(userProfileDataModel.getLastName());
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance(Locale.US);
        birthYear = admissionYear = calendar.get(Calendar.YEAR);
        birthMonth = admissionMonth = calendar.get(Calendar.MONTH) + 1;
        birthDay = admissionDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @OnClick(R.id.admissionDateTextView)
    public void admissionDateClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        showAdmissionDateDialog();
    }

    @OnClick(R.id.dateOfBirthTextView)
    public void dateOfAdmissionClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        showBirthDateDialog();
    }


    private void showBirthDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, birthDateListener, birthYear, birthMonth, birthDay);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void showAdmissionDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, admissionDateListener, admissionYear, admissionMonth, admissionDay);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener birthDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            birthYear = year;
            birthMonth = month;
            birthDay = day;

            birthDate = (birthMonth + 1) + "/" + birthDay + "/" + birthYear;
            birthDateTextView.setText(birthDate);
        }
    };
    private DatePickerDialog.OnDateSetListener admissionDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            admissionYear = year;
            admissionMonth = month;
            admissionDay = day;

            admissionDate = (admissionMonth + 1) + "/" + admissionDay + "/" + admissionYear;
            admissionDateTextView.setText(admissionDate);
        }
    };

    @OnClick(R.id.saveProfileButton)
    public void saveClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (performInternetCheck()) {
            String firstName = firstNameEditor.getText().toString();
            String lastName = lastNameEditor.getText().toString();
            if (performValidation(firstName, lastName)) {

                UserProfileDataModel userProfileDataModel = composeProfileDataModel(firstName, lastName);
                saveUserProfileDataToFirebase(userProfileDataModel);
            }
        }
    }

    private boolean performValidation(String firstName, String lastName) {
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(birthDate) || TextUtils.isEmpty(admissionDate)) {
            GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.fill_all_fields_msg));
            return false;
        }
        return true;
    }

    private UserProfileDataModel composeProfileDataModel(String firstName, String lastName) {
        String barAdmissionState = getResources().getStringArray(R.array.bar_states)[barAdmissionSpinner.getSelectedItemPosition()];

        UserProfileDataModel userProfileDataModel = new UserProfileDataModel(firstName, lastName, birthDate);
        userProfileDataModel.setBarAdmission(barAdmissionState);
        userProfileDataModel.setAdmissionDate(admissionDate);

        return userProfileDataModel;

    }

    private void saveUserProfileDataToFirebase(final UserProfileDataModel userProfileDataModel) {
        showProgressDialog();
        Cycle currentCycle = determineCourseCycles(userProfileDataModel);
        saveProfileAndCycleRequirements(userProfileDataModel, currentCycle);
    }

    private Cycle determineCourseCycles(UserProfileDataModel userProfileDataModel) {
        Calendar admissionCalendar = Calendar.getInstance();
        Calendar birthCalendar = Calendar.getInstance();
        admissionCalendar.set(admissionYear, admissionMonth, admissionDay);
        birthCalendar.set(birthYear, birthMonth, birthDay);

        String lastName = userProfileDataModel.getLastName();

        if (userProfileDataModel.getBarAdmission().equalsIgnoreCase("New York")) {
            return CycleReqsManagerUtil.getCurrentCycleForNewYork(admissionCalendar, birthCalendar);
        } else if (userProfileDataModel.getBarAdmission().equalsIgnoreCase("Illinois")) {
            return CycleReqsManagerUtil.getCourseCycleForIllinois(admissionCalendar, lastName);
        } else if (userProfileDataModel.getBarAdmission().equalsIgnoreCase("California")) {
            return CycleReqsManagerUtil.getCurrentCycleForCalifornia(this, admissionCalendar, lastName);
        } else {
            return CycleReqsManagerUtil.getCurrentCycleForTexas(admissionCalendar, birthCalendar);
        }
    }


    private void saveProfileAndCycleRequirements(final UserProfileDataModel userProfileDataModel, final Cycle currentCycle) {
        String userIdKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabaseReference.child("Users").child(userIdKey).child("profile").setValue(userProfileDataModel, new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel = userProfileDataModel;
                    saveCycleWithRequirements(currentCycle);
                } else {
                    hideProgressDialog();
                    GlobalUtil.showMessageAlertWithOkButton(ProfileActivity.this, getString(R.string.alert_message_title), databaseError.getMessage());
                }
            }
        });
    }

    private void saveCycleWithRequirements(final Cycle currentCycle) {
        String userIdKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabaseReference.child("Users").child(userIdKey).child("currentCycle").setValue(currentCycle, new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if (databaseError == null) {
                    removeCleData();
                } else {
                    GlobalUtil.showMessageAlertWithOkButton(ProfileActivity.this, getString(R.string.alert_message_title), databaseError.getMessage());
                }
            }
        });
    }

    private void removeCleData() {
        String userIdKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabaseReference.child("Users").child(userIdKey).child("cles").removeValue(new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                goToNextScreen();

            }
        });
    }


    private void goToNextScreen() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
