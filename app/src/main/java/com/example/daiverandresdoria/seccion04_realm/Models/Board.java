package com.example.daiverandresdoria.seccion04_realm.Models;

import com.example.daiverandresdoria.seccion04_realm.App.MyAplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Board extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date creatAT = new Date();
    private RealmList<Note> notes;

    public Board() {
    }

    public Board(String title) {
        this.id= MyAplication.BoardID.incrementAndGet();
        this.title = title;
        this.notes = new RealmList<Note>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatAT() {
        return creatAT;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

}

