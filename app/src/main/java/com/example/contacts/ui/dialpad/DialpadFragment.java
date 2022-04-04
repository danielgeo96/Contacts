package com.example.contacts.ui.dialpad;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.contacts.R;
import com.example.contacts.databinding.FragmentDialpadBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DialpadFragment extends Fragment implements View.OnClickListener {

    private DialpadViewModel dialpadViewModel;
    private FragmentDialpadBinding binding;
    TextView titleNum;
    String finalText;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dialpadViewModel =
                new ViewModelProvider(this).get(DialpadViewModel.class);

        binding = FragmentDialpadBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        titleNum = root.findViewById(R.id.fragment_dialpad_titleTextView);
        FloatingActionButton oneBtn = root.findViewById(R.id.fragment_dialpad_oneBtn);
        oneBtn.setOnClickListener(this);
        FloatingActionButton twoBtn = root.findViewById(R.id.fragment_dialpad_twoBtn);
        twoBtn.setOnClickListener(this);
        FloatingActionButton threeBtn = root.findViewById(R.id.fragment_dialpad_threeBtn);
        threeBtn.setOnClickListener(this);
        FloatingActionButton fourBtn = root.findViewById(R.id.fragment_dialpad_fourBtn);
        fourBtn.setOnClickListener(this);
        FloatingActionButton fiveBtn = root.findViewById(R.id.fragment_dialpad_fiveBtn);
        fiveBtn.setOnClickListener(this);
        FloatingActionButton sixBtn = root.findViewById(R.id.fragment_dialpad_sixBtn);
        sixBtn.setOnClickListener(this);
        FloatingActionButton sevenBtn = root.findViewById(R.id.fragment_dialpad_sevenBtn);
        sevenBtn.setOnClickListener(this);
        FloatingActionButton eightBtn = root.findViewById(R.id.fragment_dialpad_eightBtn);
        eightBtn.setOnClickListener(this);
        FloatingActionButton nineBtn = root.findViewById(R.id.fragment_dialpad_nineBtn);
        nineBtn.setOnClickListener(this);
        FloatingActionButton zeroBtn = root.findViewById(R.id.fragment_dialpad_zeroBtn);
        zeroBtn.setOnClickListener(this);
        FloatingActionButton asteriskBtn = root.findViewById(R.id.fragment_dialpad_asteriskBtn);
        asteriskBtn.setOnClickListener(this);
        FloatingActionButton hashtagBtn = root.findViewById(R.id.fragment_dialpad_hashtagBtn);
        hashtagBtn.setOnClickListener(this);

        ImageButton deleteBtn = root.findViewById(R.id.fragment_dialpad_deleteBtn);
        FloatingActionButton callBtn = root.findViewById(R.id.fragment_dialpad_callBtn);

        //When press delete button remove the last char on the String.
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finalText = finalText.substring(0, finalText.length() - 1);
                    titleNum.setText(finalText);
                }finally {
                    setHasOptionsMenu(false);
                    return;
                }

            }
        });

        //When press call button send intent to the phone caller and dial to the number in the text view.
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+finalText));//change the number
                startActivity(callIntent);
            }
        });

        return root;
    }

    //When click on one of the number do this.
    @Override
    public void onClick(View v) {
        //Add add button in the menu bar.
        setHasOptionsMenu(true);

        //Set and deploy the new String.
        String selected = (String)v.getTag();
        titleNum.append(selected);
        finalText = titleNum.getText().toString();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Disable add button at the menu bar.
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
    }

    //Create menu bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dialpad_menu ,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //When press the add button at the menu bar Move to AddOrEdit fragment.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DialpadFragmentDirections.DialpadToAddOrEdit action = DialpadFragmentDirections.dialpadToAddOrEdit(finalText,-1,0);
        Navigation.findNavController(root).navigate(action);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}