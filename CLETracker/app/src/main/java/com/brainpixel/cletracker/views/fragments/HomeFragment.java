package com.brainpixel.cletracker.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.interfaces.NewCleAdded;
import com.brainpixel.cletracker.model.Category;
import com.brainpixel.cletracker.model.Cycle;
import com.brainpixel.cletracker.model.GlobalDataManager;
import com.brainpixel.cletracker.model.UserProfileDataModel;
import com.brainpixel.cletracker.utils.CycleReqsManagerUtil;
import com.brainpixel.cletracker.utils.GlobalUtil;
import com.brainpixel.cletracker.utils.GridSpacingItemDecoration;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.activities.AddCLEActivity;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 04/04/2017.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.addCleButton)
    Button addCleButton;
    @BindView(R.id.userExpStatusView)
    TextView userExperienceStatusView;
    @BindView(R.id.dateValueTextView)
    TextView dateValueTextView;
    @BindView(R.id.arcProgressIndicator)
    ArcProgress arcProgressBar;
    @BindView(R.id.progressCreditsTextView)
    TextView progressCreditsTextView;
    @BindView(R.id.requirementsRecyclerView)
    RecyclerView requirementsRecyclerView;
    @BindView(R.id.progressBar)

    ProgressBar progressBar;
    private Cycle userCycleAndRequirementsDataModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readUserCycleAndRequirements();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void setValuesToViews() {
        int progress = 0;
        if (userCycleAndRequirementsDataModel.getRequirements().getTotalCreditHours() > 0) {
            progress = (userCycleAndRequirementsDataModel.getCreditsDone() * 100) / userCycleAndRequirementsDataModel.getRequirements().getTotalCreditHours();
            if (progress > 100) {
                progress = 100;
            }
        }
        String creditsRatioText = "" + userCycleAndRequirementsDataModel.getCreditsDone() + "/" + userCycleAndRequirementsDataModel.getRequirements().getTotalCreditHours();
        userExperienceStatusView.setText(userCycleAndRequirementsDataModel.getTitle());
        String cycleDate = userCycleAndRequirementsDataModel.getStartDate() + " --- " + userCycleAndRequirementsDataModel.getEndDate();
        dateValueTextView.setText(cycleDate);
        arcProgressBar.setProgress(progress);
        progressCreditsTextView.setText(creditsRatioText);

        configureRecylcerView(userCycleAndRequirementsDataModel.getRequirements().getRequiredCategories());
        Date endDate = GlobalUtil.parseStringWithoutTimeToDate(userCycleAndRequirementsDataModel.getEndDate());
        Date currentDate = new Date();
        if (endDate.before(currentDate)) {
            addCleButton.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    private void checkExpiryOfCycleAndRenewIt(Cycle currentCycle) {
        Date endDate = GlobalUtil.parseStringWithoutTimeToDate(currentCycle.getEndDate());
        Date currentDate = new Date();
        if (!endDate.before(currentDate)) {
            userCycleAndRequirementsDataModel = currentCycle;
            setValuesToViews();
        } else {
            renewCycle();
        }
    }

    private void renewCycle() {
        UserProfileDataModel profileDataModel = GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel;

        String admissionDate = profileDataModel.getAdmissionDate();
        String birthDate = profileDataModel.getDateOfBirth();
        String lastName = profileDataModel.getLastName();

        Calendar birthCalendar = Calendar.getInstance(Locale.US);
        Calendar admissionCalendar = Calendar.getInstance(Locale.US);
        birthCalendar.setTime(GlobalUtil.parseStringWithoutTimeToDate(birthDate));
        admissionCalendar.setTime(GlobalUtil.parseStringWithoutTimeToDate(admissionDate));

        Cycle cycle;
        if (profileDataModel.getBarAdmission().equalsIgnoreCase("New York")) {
            cycle = CycleReqsManagerUtil.getCurrentCycleForNewYork(admissionCalendar, birthCalendar);
        } else if (profileDataModel.getBarAdmission().equalsIgnoreCase("Illinois")) {
            cycle = CycleReqsManagerUtil.getCourseCycleForIllinois(admissionCalendar, lastName);
        } else if (profileDataModel.getBarAdmission().equalsIgnoreCase("California")) {
            cycle = CycleReqsManagerUtil.getCurrentCycleForCalifornia(getActivity(), admissionCalendar, lastName);
        } else {
            cycle = CycleReqsManagerUtil.getCurrentCycleForTexas(admissionCalendar, birthCalendar);
        }
        saveCycleWithRequirements(cycle);
    }

    private void configureRecylcerView(ArrayList<Category> categories) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        requirementsRecyclerView.setLayoutManager(mLayoutManager);
        requirementsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        requirementsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, GlobalUtil.dpToPx(getActivity(), 10), true));

        RequirementsAdapter requirementsAdapter = new RequirementsAdapter(getActivity(), categories);
        requirementsRecyclerView.setAdapter(requirementsAdapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NewCleAdded newCleAdded) {
        userCycleAndRequirementsDataModel.setCreditsDone(userCycleAndRequirementsDataModel.getCreditsDone() + newCleAdded.cleDataModel.getHours());
        ArrayList<Category> categories = userCycleAndRequirementsDataModel.getRequirements().getRequiredCategories();
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(newCleAdded.cleDataModel.getCategory())) {

                category.setHoursDone(category.getHoursDone() + newCleAdded.cleDataModel.getHours());
                break;
            }
        }
        saveCycleWithRequirements(userCycleAndRequirementsDataModel);
    }

    @OnClick(R.id.addCleButton)
    public void addCleClicked() {
        Intent intent = new Intent(getActivity(), AddCLEActivity.class);
        intent.putExtra(AddCLEActivity.KEY_USER_CYCLE, userCycleAndRequirementsDataModel);
        startActivity(intent);
    }

    private void readUserCycleAndRequirements() {
        showProgressBar();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ValueEventListener readCycleRequirementsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressBar();
                Cycle cycle = dataSnapshot.getValue(Cycle.class);
                checkExpiryOfCycleAndRenewIt(cycle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressBar();
            }
        };
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("currentCycle").addListenerForSingleValueEvent(readCycleRequirementsListener);
    }

    private void saveCycleWithRequirements(final Cycle currentCycle) {
        showProgressBar();
        String userIdKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userIdKey).child("currentCycle").setValue(currentCycle, new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    readUserCycleAndRequirements();
                } else {
                    hideProgressBar();
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), databaseError.getMessage());
                }
            }
        });
    }

}
