package com.example.contacts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.model;
import com.example.contacts.ui.contacts.ContactsFragmentDirections;


public class addOrEditFragment extends Fragment {

    Contacts contact = new Contacts();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_or_edit, container, false);
        Button saveBtn = root.findViewById(R.id.add_or_edit_saveBtn);
        Button cancelBtn = root.findViewById(R.id.add_or_edit_cancelBtn);
        Switch addToFav = root.findViewById(R.id.add_or_edit_addToFavSwitch);
        EditText firstNameText = root.findViewById(R.id.fragment_add_or_edit_firstName);
        EditText lastNameText = root.findViewById(R.id.fragment_add_or_edit_lastName);
        EditText phoneNumberText = root.findViewById(R.id.add_or_edit_phoneNumber);
        EditText emailText = root.findViewById(R.id.add_or_edit_emailText);
        TextView titleText = root.findViewById(R.id.addOrEdit_fragment_title);

        String phoneNumber = addOrEditFragmentArgs.fromBundle(getArguments()).getPhoneNumberFromDialPad();
        int position = addOrEditFragmentArgs.fromBundle(getArguments()).getGetPositionFromInfo();
        String fromFrag = addOrEditFragmentArgs.fromBundle(getArguments()).getFromLastFrag();

        Log.d("Tag","the phone is " + phoneNumber);

        if(fromFrag == "DialPad"){

            titleText.setText("Add new contact: ");
            phoneNumberText.setText(phoneNumber);

        }else if(fromFrag == "ContactsCreate"){
            titleText.setText("Add new contact: ");
        }else{
            titleText.setText("Edit contact");
            contact = model.instance.getContactByCount(position);
            firstNameText.setText(contact.getFirstName());
            lastNameText.setText(contact.getLastName());
            phoneNumberText.setText(contact.getPhoneNumber());
            emailText.setText(contact.getEmail());
            if(contact.getFavorite()){
                addToFav.setChecked(true);
            }
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setPhoneNumber(phoneNumberText.getText().toString());
                contact.setFirstName(firstNameText.getText().toString());
                contact.setLastName(lastNameText.getText().toString());
                contact.setEmail(emailText.getText().toString());
                if(fromFrag == "DialPad" || fromFrag == "ContactsCreate") {
                    if(addToFav.isChecked()){
                        contact.setFavorite(true);
                        model.instance.setFavList(contact);
                    }else {
                        contact.setFavorite(false);
                    }
                    model.instance.setData(contact);
                }else{
                    if(fromFrag == "Favorites"){
                        if(addToFav.isChecked()){
                            model.instance.changeFavList(contact,position);
                        }else {
                            contact.setFavorite(false);
                            model.instance.removeFavContact(position);
                        }
                    }else{
                        if(addToFav.isChecked()){
                            contact.setFavorite(true);
                            model.instance.setFavList(contact);
                        }else{
                            model.instance.changeData(contact,position);
                        }
                    }
                }
                Navigation.findNavController(root).navigate(R.id.action_addOrEditFragment_pop);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_addOrEditFragment_pop);
            }
        });


        return root;
    }
}