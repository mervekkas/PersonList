package com.example.personlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personlist.R;
import com.example.personlist.database.DataBase;
import com.example.personlist.model.Persons;

import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonViewHolder> {
    private List<Persons> personsList;
    private DataBase db;
    public PersonAdapterListener mListener;

    @NonNull
    @Override
    public PersonsAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_persons_layout, parent, false);

        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonsAdapter.PersonViewHolder holder, int position) {
        final Persons persons = personsList.get(position);
        holder.itemNameSurname.setText(persons.getPerson_name() + " " + persons.getPerson_surname());
        holder.itemPhone.setText((persons.getPerson_phone()));
        holder.itemNote.setText(persons.getPerson_note());
        if (persons.getPerson_note().isEmpty()) {
            holder.itemNote.setVisibility(View.GONE);
            holder.itemNoteTitle.setVisibility(View.GONE);
        }
        holder.itemMail.setText(persons.getPerson_email());
        holder.itemDateBirth.setText(persons.getPerson_date_birth());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(persons);
            }
        });

        holder.clickDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemDeleted(persons);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

    public void addList(List<Persons> personsList){
        this.personsList = personsList;
        notifyDataSetChanged();
    }
    public void updateList(List<Persons> personsList) {
        this.personsList = personsList;
        notifyDataSetChanged();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameSurname, itemPhone, itemDateBirth, itemMail, itemNote, itemNoteTitle;
        private CardView cardView;
        final public Button clickDelete;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameSurname = itemView.findViewById(R.id.tv_item_person_name);
            itemDateBirth = itemView.findViewById(R.id.tv_item_person_birthday);
            itemPhone = itemView.findViewById(R.id.tv_item_person_phone);
            itemMail = itemView.findViewById(R.id.tv_item_person_eposta);
            itemNote = itemView.findViewById(R.id.tv_item_person_note);
            itemNoteTitle = itemView.findViewById(R.id.tv_item_note);
            cardView = itemView.findViewById(R.id.cardView);
            clickDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface PersonAdapterListener {
        void onItemClicked(Persons persons);

        void onItemDeleted(Persons person);
    }
}
