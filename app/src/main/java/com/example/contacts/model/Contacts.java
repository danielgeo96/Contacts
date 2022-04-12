package com.example.contacts.model;

import android.provider.ContactsContract;

public class Contacts {

    String phoneNumber;
    String fullName;
    String email;
    boolean isFavorite = false;
    String dbPosition;
    int listPosition;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName) {
        this.fullName = firstName;
    }

    public String getDbPosition() {
        return dbPosition;
    }

    public void setDbPosition(String count) {
        this.dbPosition = count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getListPosition() {
        return listPosition;
    }
}
