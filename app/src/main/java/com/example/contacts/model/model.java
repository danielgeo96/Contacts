package com.example.contacts.model;

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
    int dataIndex = 0;
    int favListIndex = 0;

    List<Contacts> data = new LinkedList<>();
    List<Contacts> favList = new LinkedList<>();

    private final static model instance = new model();

    private model() {

    }

    public static model getInstance() {

        return instance;
    }

    public void changeData(Contacts temp, int position) {
        data.set(position, temp);
    }

    //----------------------------------------------------------------------------------------------
    public List<Contacts> getContentFromDB(View view) {

        //data = new LinkedList<>();
        List<Contacts> tempList = new LinkedList<>();
        int tempDataIndex = 0;

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = view.getContext().getContentResolver().query(uri, null, null, null, null);

        if (contactsCursor.getCount() > 0) {
            while (contactsCursor.moveToNext()) {
                //temp contact
                Contacts tempData = new Contacts();

                //set full-name
                tempData.setFullName(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));

                //check if favorite
                String isFav = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.STARRED));
                if (isFav.equals("1")) {
                    tempData.setFavorite(true);
                }

                String contactsId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                tempData.setDbPosition(contactsId);

                //set Phone Number
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = view.getContext().getContentResolver().query(uriPhone, null, phoneSelection, new String[]{contactsId}, null);
                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    tempData.setPhoneNumber(number);
                }
                phoneCursor.close();

                //set Email address
                Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String emailSelection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
                Cursor emailCursor = view.getContext().getContentResolver().query(uriEmail, null, emailSelection, new String[]{contactsId}, null);
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

    public List<Contacts> getFavContentFromDB(View view) {

        Contacts tempContact = new Contacts();
        List<Contacts> tempList = new LinkedList<>();
        int tempFavListIndex = 0;

        //if enter favorites before data fix empty list
        if (data.size() == 0) {
            getContentFromDB(view);
        }

        //run in data and find all favorites
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

    public void saveContact(Contacts contact, Context context) {

        Uri uri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, new ContentValues());
        long id = ContentUris.parseId(uri);

        ContentValues name = new ContentValues();
        name.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        name.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getFullName());
        name.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, name);

        ContentValues number = new ContentValues();
        number.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        number.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhoneNumber());
        number.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        number.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, number);

        ContentValues email = new ContentValues();
        email.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        email.put(ContactsContract.CommonDataKinds.Email.ADDRESS, contact.getEmail());
        email.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        email.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, email);

        if (contact.getFavorite()) {
            ContentValues isFav = new ContentValues();
            isFav.put(ContactsContract.Contacts.STARRED, 1);
            context.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, isFav, ContactsContract.Contacts._ID + "=" + id, null);
        }

    }

    public void updateContact(Contacts contact ,Context context) {

        saveContact(contact,context);
        removeContact(contact,context);

    }

    public void removeContact(Contacts contact ,Context context) {
        String contactsId = contact.getDbPosition();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,contactsId);
        context.getContentResolver().delete(uri, null, null);
    }

}

