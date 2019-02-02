package com.brainpixel.cletracker.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 06/04/2017.
 */

public class UserProfileDataModel implements Serializable {
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String dateOfBirth;
    private String barAdmission;
    private String admissionDate;

    public UserProfileDataModel() {

    }

    public UserProfileDataModel(String firstName, String lastName, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBarAdmission() {
        return barAdmission;
    }

    public void setBarAdmission(String barAdmission) {
        this.barAdmission = barAdmission;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }


}
