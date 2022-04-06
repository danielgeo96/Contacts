package com.example.contacts.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.LinkedList;
import java.util.List;

public class Model {
    int dataIndex, favListIndex = 0;
    List<Contacts> data, favList = new LinkedList<>();

    private final static Model instance = new Model();

    private Model() {

    }

    public static Model getInstance() {

        return instance;
    }

    //Get contacts list from db.
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

    //Get contact from list by position.
    public Contacts getContactByCount(int count) {
        return data.get(count);
    }

    //Get favorite contacts list from db.
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

    //Get favorite contact from favorite list by position.
    public Contacts getFavContactByCount(int count) {
        return favList.get(count);
    }

    //Save contact to db.
    public void saveContact(Contacts contact, ContentResolver contentResolver) {

        Uri uri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, new ContentValues());
        long id = ContentUris.parseId(uri);
        ContentValues contentValues;

        //Edit name.
        editName(id,contact.getFullName(),contentResolver,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

        //Edit phone number.
        editEmailOrPhone(id,contact.getPhoneNumber(),contentResolver,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_HOME);

        //Edit email.
        editEmailOrPhone(id,contact.getPhoneNumber(),contentResolver,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.TYPE,ContactsContract.CommonDataKinds.Email.TYPE_HOME);

        //Check if contact is favorite, if so change in db.
        if (contact.getFavorite()) {
            editIsFav(id,true,contentResolver);
        }else {
            editIsFav(id,false,contentResolver);
        }

    }

    //Update contacts using save and delete.
    public void updateContact(Contacts contact, ContentResolver contentResolver) {
        saveContact(contact, contentResolver);
        removeContact(contact, contentResolver);
    }

    //Remove contacts.
    public void removeContact(Contacts contact, ContentResolver contentResolver) {
        String contactsId = contact.getDbPosition();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactsId);
        contentResolver.delete(uri, null, null);
    }

    //Edit email or phone number.
    private void editEmailOrPhone(long id, String data , ContentResolver contentResolver, String contentItemType, String dataUri, String typeUri, int typeDataUri){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.MIMETYPE, String.valueOf(contentItemType));
        contentValues.put(String.valueOf(dataUri), data);
        contentValues.put(String.valueOf(typeUri), String.valueOf(typeDataUri));
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
    }

    //Edit name.
    private void editName(long id, String data , ContentResolver contentResolver, String contentItemType, String dataUri){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.MIMETYPE, String.valueOf(contentItemType));
        contentValues.put(String.valueOf(dataUri), data);
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
    }

    //Edit isFav.
    private void editIsFav(long id,Boolean isFav,ContentResolver contentResolver){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Contacts.STARRED, isFav);
        contentResolver.update(ContactsContract.Contacts.CONTENT_URI, contentValues, ContactsContract.Contacts._ID + "=" + id, null);
    }
}

