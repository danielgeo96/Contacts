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

     position = infoFragmentArgs.fromBundle(getArguments()).getRecivePosParam();
     Boolean isFav = infoFragmentArgs.fromBundle(getArguments()).getIsFav();
     String fromFrag = infoFragmentArgs.fromBundle(getArguments()).getFromFrag();

    if(isFav){
         contacts = model.instance.getFavContactByCount(position);
    }else{
        contacts = model.instance.getContactByCount(position);
    }

     fullNameTextView.setText(contacts.getFirstName()+ " " + contacts.getLastName());
     phoneNumberTextView.setText(contacts.getPhoneNumber());
     emailTextView.setText(contacts.getEmail());

     editBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             infoFragmentDirections.ActionInfoFragmentToAddOrEditFragment action = infoFragmentDirections.actionInfoFragmentToAddOrEditFragment("",position,fromFrag);
             Navigation.findNavController(root).navigate(action);
         }
     });

     delBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(fromFrag == "Favorites"){
                 model.instance.removeBothFavAndContact(contacts);
             }else{
                 if(contacts.getFavorite()){
                     model.instance.removeBothFavAndContact(contacts);
                 }else {
                     model.instance.removeContact(position);
                 }
             }

             Navigation.findNavController(root).navigate(R.id.isRemove);

         }
     });

     callBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent callIntent = new Intent(Intent.ACTION_CALL);
             callIntent.setData(Uri.parse("tel:"+phoneNumberTextView.getText().toString()));//change the number
             startActivity(callIntent);
         }
     });



     return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.info_save_btn ,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        model.instance.changeData(contacts,position);
        setHasOptionsMenu(false);

        return super.onOptionsItemSelected(item);
    }

}

