package com.example.contacts.ui.favorites;

import android.os.Bundle;
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
import com.example.contacts.adapters.ContactsAdapter;
import com.example.contacts.databinding.FragmentFavoritesBinding;
import com.example.contacts.model.Contacts;
import com.example.contacts.model.Model;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private FragmentFavoritesBinding binding;
    RecyclerView list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Make list.
        list = root.findViewById(R.id.fragment_favorite_recycler_view);

        //Set list layout manager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        //Getting data from the model.
        List<Contacts> data = Model.getInstance().getFavContentFromDB(getContext().getContentResolver());

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

                FavoritesFragmentDirections.ActionNavigationFavoritesToInfoFragment action = FavoritesFragmentDirections.actionNavigationFavoritesToInfoFragment(position,true,3);
                Navigation.findNavController(root).navigate(action);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}