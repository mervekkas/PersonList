package com.example.personlist.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import com.example.personlist.database.DataBase;
import com.example.personlist.database.PersonsDao;
import com.example.personlist.R;
import com.example.personlist.adapter.PersonsAdapter;
import com.example.personlist.model.Persons;

import java.util.ArrayList;

public class PersonListFragment extends Fragment implements PersonsAdapter.PersonAdapterListener {

    private ImageView imgPersonAdd;
    private RecyclerView recyclerView;
    private ArrayList<Persons> personsArrayList;
    private PersonsAdapter adapter;
    private DataBase db;
    private SearchView searchView;

    public PersonListFragment() {
    }

    public static PersonListFragment newInstance(String param1, String param2) {
        PersonListFragment fragment = new PersonListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViews(view);
    }

    private void getViews(View v) {

        imgPersonAdd = v.findViewById(R.id.img_person_add);
        clickAddPerson();

        db = new DataBase(getContext());
        personsArrayList = new PersonsDao().allPersons(db);
        adapter = new PersonsAdapter();
        adapter.addList(personsArrayList);
        adapter.mListener = this;

        recyclerView = v.findViewById(R.id.rv_person_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchView = v.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                personsArrayList = new PersonsDao().searchPerson(db, newText);
                adapter.updateList(personsArrayList);
                return false;
            }
        });
    }

    private void clickAddPerson() {
        imgPersonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPersonFragment newPersonFragment = NewPersonFragment.newInstance(NewPersonFragment.NewPersonType.NEW);
                fragmentTransaction(newPersonFragment);
            }
        });
    }

    public void fragmentTransaction(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_main, fragment, null).addToBackStack("tag").commit();
    }

    @Override
    public void onItemClicked(Persons persons) {
        NewPersonFragment newPersonFragment = NewPersonFragment.newInstance(NewPersonFragment.NewPersonType.EDIT,
                persons.getPerson_id(),
                persons.getPerson_name(),
                persons.getPerson_surname(),
                persons.getPerson_date_birth(),
                persons.getPerson_email(),
                persons.getPerson_phone(),
                persons.getPerson_note());
        fragmentTransaction(newPersonFragment);
    }

    @Override
    public void onItemDeleted(Persons person) {
        new PersonsDao().personsRemove(db, person.getPerson_id());
        personsArrayList = new PersonsDao().allPersons(db);
        adapter.updateList(personsArrayList);
    }
}