package com.example.contacts.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.adapters.contactsAdapter;
import com.example.contacts.databinding.FragmentContactsBinding;
import com.example.contacts.model.Contacts;
import com.example.contacts.model.model;

import java.util.List;


public class ContactsFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private FragmentContactsBinding binding;
    RecyclerView list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Make list
        list = root.findViewById(R.id.fragment_contacts_recyclerView);
        list.hasFixedSize();

        //do something
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        //getting data from the model
        List<Contacts> data = model.instance.getAllContacts();

        //set adapter
        contactsAdapter adapter = new contactsAdapter(getLayoutInflater());
        adapter.data = data;
        list.setAdapter(adapter);

        //Create divider between lines
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(),
                layoutManager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);

        adapter.setOnClickListener(new contactsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Log.d("Tag","row was clicked" + position);

                ContactsFragmentDirections.ContactsToInfo action = ContactsFragmentDirections.contactsToInfo(position);

                Navigation.findNavController(root).navigate(action);
            }
        });

        return root;
    }

}