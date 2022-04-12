package com.example.contacts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.contacts.model.Contacts;
import com.example.contacts.model.Model;


public class AddOrEditFragment extends Fragment {

    private final int DIALPAD_INDEX = 0;
    private final int CONTACT_INDEX = 2;
    private final int FAVORITES_INDEX = 3;

    Contacts contact = new Contacts();
    TextView titleText;
    EditText phoneNumberText, firstNameText, emailText;
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

        String phoneNumber = AddOrEditFragmentArgs.fromBundle(getArguments()).getPhoneNumberFromDialPad();
        int fromFrag = AddOrEditFragmentArgs.fromBundle(getArguments()).getFromLastFrag();
        int position = AddOrEditFragmentArgs.fromBundle(getArguments()).getGetPositionFromInfo();

        //Change the fragment to the relevant style.
        openFragment(phoneNumber, fromFrag, position);

        saveBtn.setOnClickListener(v -> {
//            setContact(firstNameText.getText().toString(), phoneNumberText.getText().toString(), emailText.getText().toString(), addToFav.isChecked());

            if (fromFrag == 0 || fromFrag == 1) {
                Model.getInstance().saveContact(contact, getContext().getContentResolver());
            } else {
                Model.getInstance().updateContact(contact, getContext().getContentResolver());
            }

            Navigation.findNavController(root).navigate(R.id.action_addOrEditFragment_pop);
        });

        cancelBtn.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_addOrEditFragment_pop));

        return root;
    }

    //Set contact with update data
    public void setContact(String name, String phone, String email, boolean isFav) {
        contact.setFullName(name);
        contact.setPhoneNumber(phone);
        contact.setEmail(email);
        contact.setFavorite(isFav);
    }

    //Change the fragment to the relevant style.
    public void openFragment(String phoneNumber, int fromFrag, int position) {
        //Check if need to open add frag or edit Frag.
        if (fromFrag == 0 || fromFrag == 1) {
            titleText.setText("Add new contact: ");

            if (phoneNumber != null) {
                phoneNumberText.setText(phoneNumber);
            }
        } else {
            //Check if open from contacts frag or favorites frag.
            if (fromFrag == 2) {
                contact = Model.getInstance().getContactByCount(position);

                if (contact.getFavorite()) {
                    addToFav.setChecked(true);
                }
            } else {
                contact = Model.getInstance().getFavContactByCount(position);
                addToFav.setChecked(true);
            }

            //Set textview.
            titleText.setText("Edit contact");
            firstNameText.setText(contact.getFullName());
            phoneNumberText.setText(contact.getPhoneNumber());
            emailText.setText(contact.getEmail());
        }

    }
}