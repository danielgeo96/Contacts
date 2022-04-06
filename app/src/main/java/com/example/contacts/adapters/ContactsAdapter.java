package com.example.contacts.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.model.Contacts;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsViewHolder>{

    public List<Contacts> data;
    LayoutInflater inflater;

    public ContactsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_list, null);
        ContactsViewHolder holder = new ContactsViewHolder(view);
            holder.listener = listener;
            return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
            Contacts contact = data.get(position);
            holder.bindData(contact, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

