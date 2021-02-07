package com.example.personlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    //private static final Object persons = ;

    public DataBase(@Nullable Context context) {
        super(context, "persons.sqlite", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE persons(person_id INTEGER PRIMARY KEY AUTOINCREMENT, person_name TEXT, person_surname TEXT, person_birth_date TEXT, person_mail TEXT, person_phone TEXT, person_note TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXIST persons");
        onCreate(sqLiteDatabase);
    }
}
