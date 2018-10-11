package com.example.daiverandresdoria.seccion04_realm.App;

import android.app.Application;

import com.example.daiverandresdoria.seccion04_realm.Models.Board;
import com.example.daiverandresdoria.seccion04_realm.Models.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyAplication extends Application {
    public static AtomicInteger BoardID = new AtomicInteger();
    public static AtomicInteger NoteID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setRealmConfig();

        Realm realm = Realm.getDefaultInstance();
        BoardID = getIdTable(realm,Board.class);
        NoteID = getIdTable(realm,Note.class);
        realm.close();
    }

    private void setRealmConfig(){
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    private  <T extends RealmObject> AtomicInteger getIdTable(Realm realm, Class<T> Anyclass){
        RealmResults<T> results = realm.where(Anyclass).findAll();
        return (results.size()>0)? new AtomicInteger(results.max("id").intValue()):new AtomicInteger();
    }
}
