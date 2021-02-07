package com.example.personlist.model;

public class Persons {
    private int person_id;
    private String person_name, person_surname, person_phone, person_email, person_date_birth, person_note;

    public Persons(int person_id, String person_name, String person_surname, String person_date_birth,  String person_email, String person_phone, String person_note) {
        this.person_id = person_id;
        this.person_name = person_name;
        this.person_surname = person_surname;
        this.person_date_birth = person_date_birth;
        this.person_email = person_email;
        this.person_phone = person_phone;
        this.person_note = person_note;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_surname() {
        return person_surname;
    }

    public void setPerson_surname(String person_surname) {
        this.person_surname = person_surname;
    }

    public String getPerson_phone() {
        return person_phone;
    }

    public void setPerson_phone(String person_phone) {
        this.person_phone = person_phone;
    }

    public String getPerson_email() {
        return person_email;
    }

    public void setPerson_email(String person_email) {
        this.person_email = person_email;
    }

    public String getPerson_date_birth() {
        return person_date_birth;
    }

    public void setPerson_date_birth(String person_date_birth) {
        this.person_date_birth = person_date_birth;
    }

    public String getPerson_note() {
        return person_note;
    }

    public void setPerson_note(String person_note) {
        this.person_note = person_note;
    }
}
