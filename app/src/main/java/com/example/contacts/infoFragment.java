package com.example.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.model;
import com.example.contacts.ui.contacts.ContactsFragmentDirections;

import org.w3c.dom.Text;


public class infoFragment extends Fragment {

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

        position = infoFragmentArgs.fromBundle(getArguments()).getRecivePosParam();
        Boolean isFav = infoFragmentArgs.fromBundle(getArguments()).getIsFav();
        int fromFrag = infoFragmentArgs.fromBundle(getArguments()).getFromFrag();

        if (isFav) {
            contacts = model.getInstance().getFavContactByCount(position);
        } else {
            contacts = model.getInstance().getContactByCount(position);
        }

        fullNameTextView.setText(contacts.getFullName());
        phoneNumberTextView.setText(contacts.getPhoneNumber());
        emailTextView.setText(contacts.getEmail());

        //Move to edit contact frag
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoFragmentDirections.ActionInfoFragmentToAddOrEditFragment action = infoFragmentDirections.actionInfoFragmentToAddOrEditFragment("", position, fromFrag);
                Navigation.findNavController(root).navigate(action);
            }
        });

        //Delete contact from db.
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getInstance().removeContact(contacts, getContext().getContentResolver());
                Navigation.findNavController(root).navigate(R.id.isRemove);
            }
        });

        //Call to the contact using intent.
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumberTextView.getText().toString()));//change the number
                startActivity(callIntent);
            }
        });

        //send email to the contact using intent.
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //send message to the contact using intent.
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                startActivity(intent);
            }
        });

        return root;
    }

}

