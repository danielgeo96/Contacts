package com.example.contacts.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.model.Contacts;

public class ContactsViewHolder extends RecyclerView.ViewHolder {
    public ContactsAdapter.OnItemClickListener listener;
    TextView contactsFullName;
    ImageView contactsImage;
    int position;

    public ContactsViewHolder(@NonNull View itemView) {
        super(itemView);
        contactsFullName = itemView.findViewById(R.id.row_list_textView);
        contactsImage = itemView.findViewById(R.id.row_list_imageView);

        itemView.setOnClickListener(v -> listener.OnItemClick(position));
    }

    public void bindData(Contacts contact, int position) {
        String fullName;

        fullName = contact.getFullName();

        //real version when DB is finish
        //fullName = contact.getFirstName() + contact.getLastName();

        contactsFullName.setText(fullName);
        this.position = position;
    }
}