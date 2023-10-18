package com.example.logbook_ex_3.Adapter;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logbook_ex_3.R;
import com.example.logbook_ex_3.Models.Person;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private final List<Person> persons;

    public ContactAdapter(List<Person> persons) {
        this.persons = persons;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_card, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Person person = persons.get(position);
        holder.personName.setText(person.name);
        holder.personDetails.setText(person.dob + " " + person.email);
        int imageId = holder.itemView.getResources().getIdentifier(person.url_avatar, "drawable", holder.itemView.getContext().getPackageName());
        holder.personAvatar.setImageResource(imageId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView personName, personDetails;
        ImageView personAvatar;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.personName);
            personDetails = itemView.findViewById(R.id.personDetails);
            personAvatar = itemView.findViewById(R.id.avatarImage);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
