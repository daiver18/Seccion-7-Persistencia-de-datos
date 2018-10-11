package com.example.daiverandresdoria.seccion04_realm.Models;

import com.example.daiverandresdoria.seccion04_realm.App.MyAplication;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Note extends RealmObject{
    @PrimaryKey
    private int id;
    @Required
    private String description;
    @Required
    private Date creatAT = new Date();

    public Note() {
    }

    public Note(String description) {
        this.id= MyAplication.NoteID.incrementAndGet();
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatAT() {
        return creatAT;
    }
}
