package com.example.contacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.model.Contacts;

import java.util.List;
import java.util.zip.Inflater;

public class contactsAdapter extends RecyclerView.Adapter<contactsViewHolder>{

    public List<Contacts> data;
    LayoutInflater inflater;

    public contactsAdapter(LayoutInflater inflater) {
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
    public contactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_list, null);
        contactsViewHolder holder = new contactsViewHolder(view);
            holder.listener = listener;
            return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull contactsViewHolder holder, int position) {
            Contacts contact = data.get(position);
            holder.bindData(contact, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

