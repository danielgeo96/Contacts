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
    TextView titleText;
    EditText phoneNumberText, firstNameText, emailText;
    String phoneNumber;
    Switch addToFav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_or_edit, container, false);
        Button saveBtn = root.findViewById(R.id.add_or_edit_saveBtn);
        Button cancelBtn = root.findViewById(R.id.add_or_edit_cancelBtn);
        addToFav = root.findViewById(R.id.add_or_edit_addToFavSwitch);
        firstNameText = root.findViewById(R.id.fragment_add_or_edit_firstName);
        phoneNumberText = root.findViewById(R.id.add_or_edit_phoneNumber);
        emailText = root.findViewById(R.id.add_or_edit_emailText);
        titleText = root.findViewById(R.id.addOrEdit_fragment_title);

        String phoneNumber = addOrEditFragmentArgs.fromBundle(getArguments()).getPhoneNumberFromDialPad();
        int fromFrag = addOrEditFragmentArgs.fromBundle(getArguments()).getFromLastFrag();
        int position = addOrEditFragmentArgs.fromBundle(getArguments()).getGetPositionFromInfo();

        //Change the fragment to the relevant style.
        openFragment(phoneNumber, fromFrag, position);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContact(firstNameText.getText().toString(), phoneNumberText.getText().
                        toString(), emailText.getText().toString(), addToFav.isChecked());

                if (fromFrag == 0 || fromFrag == 1) {
                    model.getInstance().saveContact(contact, getContext().getContentResolver());
                } else {
                    model.getInstance().updateContact(contact, getContext().getContentResolver());
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

    //Set contact with update data
    public Contacts setContact(String name, String phone, String email, Boolean isFav) {
        contact.setFullName(name);
        contact.setPhoneNumber(phone);
        contact.setEmail(email);
        contact.setFavorite(isFav);
        return contact;
    }

    //Change the fragment to the relevant style.
    public void openFragment(String phoneNumber, int fromFrag, int position) {

        //Check if need to open add frag or edit Frag.
        if (phoneNumber == null) {
            titleText.setText("Add new contact: ");
        } else {
            if (fromFrag == 0) {
                titleText.setText("Add new contact: ");
                phoneNumberText.setText(phoneNumber);
            } else {
                titleText.setText("Edit contact");
                //Check if open from contacts frag or favorites frag.
                if (fromFrag == 2) {
                    contact = model.getInstance().getContactByCount(position);
                    if (contact.getFavorite()) {
                        addToFav.setChecked(true);
                    }
                } else {
                    contact = model.getInstance().getFavContactByCount(position);
                    addToFav.setChecked(true);
                }
                //Set textview.
                firstNameText.setText(contact.getFullName());
                phoneNumberText.setText(contact.getPhoneNumber());
                emailText.setText(contact.getEmail());
            }
        }
    }
}