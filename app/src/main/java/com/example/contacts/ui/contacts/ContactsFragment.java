package com.example.contacts.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.contacts.adapters.ContactsAdapter;
import com.example.contacts.databinding.FragmentContactsBinding;
import com.example.contacts.model.Contacts;
import com.example.contacts.model.Model;

import java.util.List;


public class ContactsFragment extends Fragment {

    private ContactsViewModel contactsViewModel;
    private FragmentContactsBinding binding;
    RecyclerView list;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Show the add button in the menu bar.
        setHasOptionsMenu(true);
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        //Make list.
        list = root.findViewById(R.id.fragment_contacts_recyclerView);

        //Set list layout manager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        //Getting data from the model.
        List<Contacts> data = Model.getInstance().getContentFromDB(getContext().getContentResolver());

        //Set adapter.
        ContactsAdapter adapter = new ContactsAdapter(getLayoutInflater());
        adapter.data = data;
        list.setAdapter(adapter);

        //Create divider between lines.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(),
                layoutManager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);

        //When click on row move to info fragment.
        adapter.setOnClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                ContactsFragmentDirections.ContactsToInfo action = ContactsFragmentDirections.contactsToInfo(position,false,2);
                Navigation.findNavController(root).navigate(action);
                
            }
        });

        return root;
    }

    //Create menu bar.
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dialpad_menu ,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //When press on menu bar add button move to add or edit fragment.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ContactsFragmentDirections.ContactsToAddOrEdit action = ContactsFragmentDirections.contactsToAddOrEdit(null,-1,1);
        Navigation.findNavController(root).navigate(action);

        return super.onOptionsItemSelected(item);
    }

}