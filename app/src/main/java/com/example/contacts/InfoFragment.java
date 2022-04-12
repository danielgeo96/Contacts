package com.example.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.Model;


public class InfoFragment extends Fragment {

    int position;
    Contacts contacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        TextView fullNameTextView = root.findViewById(R.id.fragment_info_title);
        TextView phoneNumberTextView = root.findViewById(R.id.fragment_info_phoneNumber);
        TextView emailTextView = root.findViewById(R.id.fragemnt_info_emailAdress);
        Button editBtn = root.findViewById(R.id.fragment_info_editBtn);
        Button delBtn = root.findViewById(R.id.fragment_info_delBtn);
        Button callBtn = root.findViewById(R.id.fragment_info_callBtn);
        Button emailBtn = root.findViewById(R.id.fragment_info_emailBtn);
        Button msgBtn = root.findViewById(R.id.fragment_info_msgBtn);

        position = InfoFragmentArgs.fromBundle(getArguments()).getRecivePosParam();
        boolean isFav = InfoFragmentArgs.fromBundle(getArguments()).getIsFav();
        int fromFrag = InfoFragmentArgs.fromBundle(getArguments()).getFromFrag();

        if (isFav) {
            contacts = Model.getInstance().getFavContactByCount(position);
        } else {
            contacts = Model.getInstance().getContactByCount(position);
        }

        fullNameTextView.setText(contacts.getFullName());
        phoneNumberTextView.setText(contacts.getPhoneNumber());
        emailTextView.setText(contacts.getEmail());

        //Move to edit contact frag
        editBtn.setOnClickListener(v -> {
            InfoFragmentDirections.ActionInfoFragmentToAddOrEditFragment action = InfoFragmentDirections.actionInfoFragmentToAddOrEditFragment("", position, fromFrag);
            Navigation.findNavController(root).navigate(action);
        });

        //Delete contact from db.
        delBtn.setOnClickListener(v -> {
            Model.getInstance().removeContact(contacts, getContext().getContentResolver());
            Navigation.findNavController(root).navigate(R.id.isRemove);
        });

        //Call to the contact using intent.
        callBtn.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumberTextView.getText().toString()));//change the number
            startActivity(callIntent);
        });

        //send email to the contact using intent.
        emailBtn.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        });

        //send message to the contact using intent.
        msgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
            startActivity(intent);
        });

        return root;
    }

}

