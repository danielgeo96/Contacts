package com.example.contacts.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.contacts.ui.dialpad.DialpadFragmentDirections;

import java.util.List;


public class ContactsFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private FragmentContactsBinding binding;
    RecyclerView list;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dialpad_menu ,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ContactsFragmentDirections.ContactsToAddOrEdit action = ContactsFragmentDirections.contactsToAddOrEdit("",-1);
        Navigation.findNavController(root).navigate(action);

        return super.onOptionsItemSelected(item);
    }

}