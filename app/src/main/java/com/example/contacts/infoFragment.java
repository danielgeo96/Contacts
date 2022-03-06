package com.example.contacts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.model;
import com.example.contacts.ui.contacts.ContactsFragmentDirections;

import org.w3c.dom.Text;


public class infoFragment extends Fragment {

 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_info, container, false);

     TextView fullNameTextView = root.findViewById(R.id.fragment_info_title);
     TextView phoneNumberTextView = root.findViewById(R.id.fragment_info_phoneNumber);
     TextView emailTextView = root.findViewById(R.id.fragemnt_info_emailAdress);
     Button editBtn = root.findViewById(R.id.fragment_info_editBtn);
     Button favBtn = root.findViewById(R.id.fragment_info_addToFavorite);

     int position = infoFragmentArgs.fromBundle(getArguments()).getRecivePosParam();

     Contacts contacts = model.instance.getContactByCount(position);

     fullNameTextView.setText(contacts.getFirstName()+ " " + contacts.getLastName());
     phoneNumberTextView.setText(contacts.getPhoneNumber());
     emailTextView.setText(contacts.getEmail());

     editBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             infoFragmentDirections.ActionInfoFragmentToAddOrEditFragment action = infoFragmentDirections.actionInfoFragmentToAddOrEditFragment("",position);
             Navigation.findNavController(root).navigate(action);
         }
     });

     favBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             contacts.setFavorite(true);
             model.instance.changeData(contacts,position);
         }
     });

        return root;
    }
}