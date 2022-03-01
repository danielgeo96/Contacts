package com.example.contacts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.model;

import org.w3c.dom.Text;


public class infoFragment extends Fragment {

 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

     TextView fullNameTextView = view.findViewById(R.id.fragment_info_title);
     TextView phoneNumberTextView = view.findViewById(R.id.fragment_info_phoneNumber);
     TextView emailTextView = view.findViewById(R.id.fragemnt_info_emailAdress);

     int position = infoFragmentArgs.fromBundle(getArguments()).getRecivePosParam();

     Contacts contacts = model.instance.getContactByCount(position);

     fullNameTextView.setText(contacts.getFirstName()+ " " + contacts.getLastName());
     phoneNumberTextView.setText(contacts.getPhoneNumber());
     emailTextView.setText(contacts.getEmail());

        return view;
    }
}