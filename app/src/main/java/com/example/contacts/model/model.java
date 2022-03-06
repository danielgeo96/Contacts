package com.example.contacts.model;

import java.util.LinkedList;
import java.util.List;

public class model {
    int index=0;

    List<Contacts> data = new LinkedList<Contacts>();

    public final static model instance = new model();

    private model(){
        Contacts temp = new Contacts();
        temp.setFirstName("Daniel");
        temp.setLastName("George");
        temp.setPhoneNumber("0502776661");
        temp.setEmail("Daniel96Geo@gmail.com");
        for(int j=0;j<10;j++){
            data.add(i,temp);
        }
    }

    public void setData(Contacts temp) {
        temp.setCount(index);
        data.add(temp);
        index++;
    }

    public void changeData(Contacts temp , int position){
        data.set(position,temp);
    }

    public List<Contacts> getAllContacts() {
        return data;
    }

    public Contacts getContactByCount(int count) {
        return data.get(count);
    }

    public void removeContact(int count){
        data.remove(count);
    }

    public List<Contacts> getAllFavorites(){

        List<Contacts> temp = new LinkedList<Contacts>();
        int tempIndex=0;

        for(int i =0;i < data.size();i++){
            if(data.get(i).getFavorite()){
                temp.add(tempIndex,data.get(i));
                tempIndex++;
            }
        }

        return temp;
    }


}

