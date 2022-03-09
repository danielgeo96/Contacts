package com.example.contacts.model;

import java.util.LinkedList;
import java.util.List;

public class model {
    int dataIndex=0;
    int favListIndex=0;

    List<Contacts> data = new LinkedList<Contacts>();
    List<Contacts> favList = new LinkedList<Contacts>();

    public final static model instance = new model();

    private model(){

    }

    public void setData(Contacts temp) {
        temp.setCount(dataIndex);
        data.add(temp);
        dataIndex++;
    }

    public void setFavList(Contacts temp) {
//        temp.setCount(favListIndex);
//        favList.add(temp);
//        favListIndex++;
        temp.setCount(dataIndex);
        favList.add(temp);
    }

    public void changeData(Contacts temp , int position){
        data.set(position,temp);
    }

    public void changeFavList(Contacts temp , int position){
        favList.set(position,temp);
    }

    public List<Contacts> getAllContacts() {
        return data;
    }

    public List<Contacts> getAllFavorites(){

//        int tempIndex=0;
//
//        for(int i =0;i < data.size();i++){
//            for(int j=0;j<favList.size();j++) {
//                if (data.get(i).getFavorite() && data.get(i).getCount() != favList.get(j).getCount()) {
//                    favList.add(tempIndex, data.get(i));
//                    tempIndex++;
//                }
//            }
//        }

        return favList;
    }

    public Contacts getContactByCount(int count) {
        return data.get(count);
    }

    public Contacts getFavContactByCount(int count) {
        return favList.get(count);
    }

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




}

