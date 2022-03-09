package com.example.contacts.ui.dialpad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.contacts.R;
import com.example.contacts.databinding.FragmentDialpadBinding;
import com.example.contacts.ui.contacts.ContactsFragmentDirections;
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finalText = finalText.substring(0, finalText.length() - 1);
                    titleNum.setText(finalText);
                }finally {
                    setHasOptionsMenu(false);
                    return;//banana
                }

            }
        });

        return root;
    }

    //when click on row do this function
    @Override
    public void onClick(View v) {
        setHasOptionsMenu(true);
        String selected = (String)v.getTag();

        Log.d("TAG", "the num is " +selected);
        titleNum.append(selected);
        finalText = titleNum.getText().toString();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dialpad_menu ,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DialpadFragmentDirections.DialpadToAddOrEdit action = DialpadFragmentDirections.dialpadToAddOrEdit(finalText,-1,"DialPad");
        Navigation.findNavController(root).navigate(action);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}