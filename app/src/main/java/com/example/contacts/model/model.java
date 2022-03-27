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
    int dataIndex=0;
    int favListIndex=0;

    List<Contacts> data = new LinkedList<>();
    List<Contacts> favList = new LinkedList<>();

    private final static model instance = new model();

    private model(){

    }

    public static model getInstance(){

        return instance;
    }

    public void setData(Contacts temp) {
        temp.setCount(dataIndex);
        data.add(temp);
        dataIndex++;
    }

    public void setFavList(Contacts temp) {
        temp.setCount(dataIndex);
        favList.add(temp);
    }

    public void changeData(Contacts temp , int position){
        data.set(position,temp);
    }

    public void changeFavList(Contacts temp , int position){
        favList.set(position,temp);
    }

    //    public List<Contacts> getAllContacts() {
//        return data;
//    }

//    public List<Contacts> getAllFavorites(){
//
//        return favList;
//    }

    public void removeContact(int count){
        data.remove(count);
    }

    public void removeFavContact(int count){
        favList.remove(count);
    }

    public void removeBothFavAndContact(Contacts contacts){

        for(int i=0;i<data.size();i++){

            for (int j=0;j<favList.size();j++){

                if(data.get(i).getCount() == favList.get(j).getCount()){

                    data.remove(i);
                    favList.remove(j);
                    break;
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //public List<Contacts> getContentFromDB(Context context) {
    public List<Contacts> getContentFromDB(View view) {

        data = new LinkedList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = view.getContext().getContentResolver().query(uri,null,null,null,null);

        if(contactsCursor.getCount() > 0 ){
            while (contactsCursor.moveToNext()){
                Contacts tempData = new Contacts();
                String isFav = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.STARRED));
                tempData.setFullName(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";
                String contactsId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = view.getContext().getContentResolver().query(uriPhone,null,selection,new String[]{contactsId},null);
                if(phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    tempData.setPhoneNumber(number);
                }
                if(isFav.equals("1")) {
                    tempData.setFavorite(true);
                }
                tempData.setCount(dataIndex);
                data.add(tempData);
                dataIndex++;
                phoneCursor.close();
            }
        }
        contactsCursor.close();
        return data;

    }

    public Contacts getContactByCount(int count) {
        return data.get(count);
    }

    public List<Contacts> getFavContentFromDB(View view) {

        favList = new LinkedList<>();
        Contacts tempData = new Contacts();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = view.getContext().getContentResolver().query(uri,null,null,null,null);

        if(contactsCursor.getCount() > 0 ){
            while (contactsCursor.moveToNext()){
                String contactsId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String isFav = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.STARRED));
                tempData.setFullName(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                //tempData.setPhoneNumber(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";
                Cursor phoneCursor = view.getContext().getContentResolver().query(uriPhone,null,selection,new String[]{contactsId},null);
                if(phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    tempData.setPhoneNumber(number);
                }
                if(isFav.equals("1")) {
                    tempData.setFavorite(true);
                    tempData.setCount(favListIndex);
                    favList.add(tempData);
                    favListIndex++;
                }else{
                    continue;
                }
                phoneCursor.close();
            }
        }
        contactsCursor.close();
        return favList;
    }

    public Contacts getFavContactByCount(int count) {
        return favList.get(count);
    }

    public void saveContact(Contacts contact,Context context){

        Uri uri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,new ContentValues());
        long id = ContentUris.parseId(uri);

        ContentValues name = new ContentValues();
        name.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        name.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,contact.getFullName());
        name.put(ContactsContract.Data.RAW_CONTACT_ID,id);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,name);

        ContentValues number = new ContentValues();
        number.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        number.put(ContactsContract.CommonDataKinds.Phone.NUMBER,contact.getPhoneNumber());
        number.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        number.put(ContactsContract.Data.RAW_CONTACT_ID,id);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,number);

    }


}

