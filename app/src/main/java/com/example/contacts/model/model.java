package com.example.contacts.model;

import android.app.DownloadManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class model {
    int dataIndex, favListIndex = 0;
    List<Contacts> data, favList = new LinkedList<>();

    private final static model instance = new model();

    private model() {

    }

    public static model getInstance() {

        return instance;
    }

    public List<Contacts> getContentFromDB(ContentResolver contentResolver) {

        List<Contacts> tempList = new LinkedList<>();
        int tempDataIndex = 0;

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = contentResolver.query(uri, null, null, null, null);

        if (contactsCursor.getCount() > 0) {
            while (contactsCursor.moveToNext()) {

                Contacts tempData = new Contacts();

                //set full-name
                tempData.setFullName(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));

                //check if favorite
                String isFav = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.STARRED));
                if (isFav.equals("1")) {
                    tempData.setFavorite(true);
                }

                //Get contact id from DB
                String contactsId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                tempData.setDbPosition(contactsId);

                //Set Phone Number
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = contentResolver.query(uriPhone, null, phoneSelection, new String[]{contactsId}, null);
                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    tempData.setPhoneNumber(number);
                }
                phoneCursor.close();

                //set Email address
                Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String emailSelection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
                Cursor emailCursor = contentResolver.query(uriEmail, null, emailSelection, new String[]{contactsId}, null);
                if (emailCursor.moveToNext()) {
                    String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    tempData.setEmail(email);
                }
                emailCursor.close();

                tempData.setListPosition(tempDataIndex);
                tempList.add(tempData);
                tempDataIndex++;
            }
        }
        contactsCursor.close();
        data = tempList;
        dataIndex = tempDataIndex;
        return data;

    }

    public Contacts getContactByCount(int count) {
        return data.get(count);
    }

    public List<Contacts> getFavContentFromDB(ContentResolver contentResolver) {

        Contacts tempContact;
        List<Contacts> tempList = new LinkedList<>();
        int tempFavListIndex = 0;

        //if enter favorites before data fix empty list

        if (data == null) {
            getContentFromDB(contentResolver);
        }

        //run in data list and find all favorites
        for (int i = 0; i < data.size(); i++) {

            if (data.get(i).getFavorite()) {
                tempContact = data.get(i);
                tempContact.setListPosition(tempFavListIndex);
                tempList.add(tempContact);
                tempFavListIndex++;
            }
        }

        favList = tempList;
        favListIndex = tempFavListIndex;
        return favList;
    }

    public Contacts getFavContactByCount(int count) {
        return favList.get(count);
    }

    public void saveContact(Contacts contact, ContentResolver contentResolver) {

        Uri uri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, new ContentValues());
        long id = ContentUris.parseId(uri);
        ContentValues contentValues;

        contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getFullName());
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);

        contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhoneNumber());
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);

        contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Email.ADDRESS, contact.getEmail());
        contentValues.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);

        if (contact.getFavorite()) {
            ContentValues isFav = new ContentValues();
            isFav.put(ContactsContract.Contacts.STARRED, 1);
            contentResolver.update(ContactsContract.Contacts.CONTENT_URI, isFav, ContactsContract.Contacts._ID + "=" + id, null);
        }

    }

    public void updateContact(Contacts contact, ContentResolver contentResolver) {

        saveContact(contact, contentResolver);
        removeContact(contact, contentResolver);

    }

    public void removeContact(Contacts contact, ContentResolver contentResolver) {
        String contactsId = contact.getDbPosition();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactsId);
        contentResolver.delete(uri, null, null);
    }

}

