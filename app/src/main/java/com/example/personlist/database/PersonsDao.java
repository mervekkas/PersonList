package com.example.personlist.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.personlist.model.Persons;

import java.util.ArrayList;

public class PersonsDao {

    public ArrayList<Persons> allPersons(DataBase dataBase) {

        ArrayList<Persons> personsArrayList = new ArrayList<>();

        SQLiteDatabase db = dataBase.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM persons ORDER BY person_name", null);

        while (c.moveToNext()) {

            Persons p = new Persons(
                    c.getInt(c.getColumnIndex("person_id")),
                    c.getString(c.getColumnIndex("person_name")),
                    c.getString(c.getColumnIndex("person_surname")),
                    c.getString(c.getColumnIndex("person_birth_date")),
                    c.getString(c.getColumnIndex("person_mail")),
                    c.getString(c.getColumnIndex("person_phone")),
                    c.getString(c.getColumnIndex("person_note")));
            personsArrayList.add(p);
        }
        db.close();
        return personsArrayList;
    }

    //arama
    public ArrayList<Persons> searchPerson(DataBase dataBase, String searchString) {

        ArrayList<Persons> personsArrayList = new ArrayList<>();

        SQLiteDatabase db = dataBase.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM persons WHERE person_name like '%" + searchString + "%'", null);

        while (c.moveToNext()) {

            Persons p = new Persons(
                    c.getInt(c.getColumnIndex("person_id")),
                    c.getString(c.getColumnIndex("person_name")),
                    c.getString(c.getColumnIndex("person_surname")),
                    c.getString(c.getColumnIndex("person_birth_date")),
                    c.getString(c.getColumnIndex("person_mail")),
                    c.getString(c.getColumnIndex("person_phone")),
                    c.getString(c.getColumnIndex("person_note")));
            personsArrayList.add(p);
        }
        db.close();
        return personsArrayList;
    }

    public void personsRemove(DataBase dataBase, int person_id) {
        SQLiteDatabase db = dataBase.getWritableDatabase();
        db.delete("persons", "person_id=?", new String[]{String.valueOf(person_id)});
        db.close();
    }

    public void personAdd(DataBase dataBase, String person_name, String person_surname, String person_date_birth, String person_email, String person_phone, String person_note) {
        SQLiteDatabase db = dataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("person_name", person_name);
        values.put("person_surname", person_surname);
        values.put("person_birth_date",person_date_birth);
        values.put("person_mail",person_email);
        values.put("person_phone",person_phone);
        values.put("person_note", person_note);

        db.insertOrThrow("persons", null, values);
        db.close();
    }

    public void personUpdate(DataBase dataBase, int person_id, String person_name, String person_surname, String person_date_birth, String person_email, String person_phone, String person_note) {
        SQLiteDatabase db = dataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("person_name", person_name);
        values.put("person_surname", person_surname);
        values.put("person_birth_date",person_date_birth);
        values.put("person_mail",person_email);
        values.put("person_phone",person_phone);
        values.put("person_note", person_note);

        db.update("persons", values, "person_id=?", new String[]{String.valueOf(person_id)});
        db.close();
    }
}
