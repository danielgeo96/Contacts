package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
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
        EditText phoneNumberText = root.findViewById(R.id.add_or_edit_phoneNumber);
        EditText emailText = root.findViewById(R.id.add_or_edit_emailText);
        TextView titleText = root.findViewById(R.id.addOrEdit_fragment_title);

        String phoneNumber = addOrEditFragmentArgs.fromBundle(getArguments()).getPhoneNumberFromDialPad();
        int fromFrag = addOrEditFragmentArgs.fromBundle(getArguments()).getFromLastFrag();
        int position = addOrEditFragmentArgs.fromBundle(getArguments()).getGetPositionFromInfo();

        if(fromFrag == 0){
            titleText.setText("Add new contact: ");
            phoneNumberText.setText(phoneNumber);

        }else if(fromFrag == 1){
            titleText.setText("Add new contact: ");
        }else{
            titleText.setText("Edit contact");
            if(fromFrag == 2){
                contact = model.getInstance().getContactByCount(position);
                if(contact.getFavorite()){
                    addToFav.setChecked(true);
                }
            }else{
                contact = model.getInstance().getFavContactByCount(position);
                addToFav.setChecked(true);
            }
            firstNameText.setText(contact.getFullName());
            phoneNumberText.setText(contact.getPhoneNumber());
            emailText.setText(contact.getEmail());
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setPhoneNumber(phoneNumberText.getText().toString());
                contact.setFullName(firstNameText.getText().toString());
                contact.setEmail(emailText.getText().toString());
                contact.setFavorite(addToFav.isChecked());


                if(fromFrag == 0 || fromFrag == 1) {
                    model.getInstance().saveContact(contact,getContext().getContentResolver());
                }else{
                    model.getInstance().updateContact(contact,getContext().getContentResolver());
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