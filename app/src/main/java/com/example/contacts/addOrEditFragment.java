package com.example.contacts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.model;
import com.example.contacts.ui.contacts.ContactsFragmentDirections;


public class addOrEditFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_or_edit, container, false);
        Button saveBtn = root.findViewById(R.id.add_or_edit_saveBtn);
        Button cancelBtn = root.findViewById(R.id.add_or_edit_cancelBtn);
        Button addToFavBtn = root.findViewById(R.id.add_or_edit_addToFavBtn);
        EditText firstNameText = root.findViewById(R.id.fragment_add_or_edit_firstName);
        EditText lastNameText = root.findViewById(R.id.fragment_add_or_edit_lastName);
        EditText phoneNumberText = root.findViewById(R.id.add_or_edit_phoneNumber);
        EditText emailText = root.findViewById(R.id.add_or_edit_emailText);
        TextView titleText = root.findViewById(R.id.addOrEdit_fragment_title);

        String phoneNumber = addOrEditFragmentArgs.fromBundle(getArguments()).getPhoneNumberFromDialPad();
        int position = addOrEditFragmentArgs.fromBundle(getArguments()).getGetPositionFromInfo();

        Log.d("Tag","the phone is " + phoneNumber);

        Contacts contact;

        if(phoneNumber != "" && position == -1){

            titleText.setText("Add new contact: ");
            phoneNumberText.setText(phoneNumber);
        }else if(phoneNumber == "" && position == -1){
            titleText.setText("Add new contact: ");
        }else{
            contact = model.instance.getContactByCount(position);
            firstNameText.setText(contact.getFirstName());
            lastNameText.setText(contact.getLastName());
            phoneNumberText.setText(contact.getPhoneNumber());
            emailText.setText(contact.getEmail());
        }

        //contact.setFirstName() = firstNameText.getText();


        return root;
    }
}