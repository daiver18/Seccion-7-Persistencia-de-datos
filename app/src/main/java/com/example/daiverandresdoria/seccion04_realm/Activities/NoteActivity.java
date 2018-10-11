package com.example.daiverandresdoria.seccion04_realm.Activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.daiverandresdoria.seccion04_realm.Adapter.AdapterNote;
import com.example.daiverandresdoria.seccion04_realm.Models.Board;
import com.example.daiverandresdoria.seccion04_realm.Models.Note;
import com.example.daiverandresdoria.seccion04_realm.R;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board>{
    private ListView listView;
    private FloatingActionButton fabNote;
    private AdapterNote adapterNote;
    private RealmList<Note> notes;
    private Realm realm;
    private int BoardID;
    private Board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras()!=null)
            BoardID = getIntent().getExtras().getInt("id");

        board = realm.where(Board.class).equalTo("id",BoardID).findFirst();
        board.addChangeListener(this);
        notes = board.getNotes();

        this.setTitle(board.getTitle());

        listView= findViewById(R.id.listViewNote);
        fabNote=findViewById(R.id.fabNote);

        adapterNote=new AdapterNote(R.layout.listview_note_item,notes,this);
        listView.setAdapter(adapterNote);

        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote("Add new Note","Type the new Note for "+board.getTitle()+".");
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Delete_all:
                deletAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        getMenuInflater().inflate(R.menu.contexmenu_board_activity,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.deleteBoard:
                deleteNote(notes.get(info.position));
                return true;
            case R.id.EditBoard:
                editcurrentNote("edit current note","input a valid note",notes.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void createNote(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title!=null)builder.setTitle(title);
        if (message!=null)builder.setMessage(message);

        View v = LayoutInflater.from(this).inflate(R.layout.alert_dialog_create_note,null);
        builder.setView(v);

        final EditText input = (EditText) v.findViewById(R.id.alertNewNote);
       builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               String note = input.getText().toString().trim();
               if (note.length()>0){
                   createNewNote(note);
               }
               else{
                   Toast.makeText(NoteActivity.this,"input a note valid",Toast.LENGTH_SHORT).show();
               }
           }
       });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void editcurrentNote(String title, String message, final Note note){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title!=null)builder.setTitle(title);
        if (message!=null)builder.setMessage(message);

        View v = LayoutInflater.from(this).inflate(R.layout.alert_dialog_create_note,null);
        builder.setView(v);

        final EditText input = (EditText) v.findViewById(R.id.alertNewNote);
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String notename = input.getText().toString().trim();
                if (notename.length()==0)
                    Toast.makeText(NoteActivity.this,"input a note valid",Toast.LENGTH_SHORT).show();
                else if (notename.equals(note.getDescription()))
                    Toast.makeText(NoteActivity.this,"input a note valid",Toast.LENGTH_SHORT).show();
                else
                    if (notename.length()>0){
                        editNote(notename,note);
                    }
                    else{
                        Toast.makeText(NoteActivity.this,"input a note valid",Toast.LENGTH_SHORT).show();
                    }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    ///**CRUD***
    private void deleteNote(Note note){
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }
    private void createNewNote(String Note) {
        realm.beginTransaction();
        Note note = new Note(Note);
        realm.copyToRealm(note);
        board.getNotes().add(note);
        realm.commitTransaction();
    }
    private void editNote(String newNotename,Note note){
        realm.beginTransaction();
        note.setDescription(newNotename);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    private void deletAll(){
        realm.beginTransaction();
        board.getNotes().deleteAllFromRealm();
        realm.commitTransaction();

    }

    @Override
    public void onChange(Board board) {
        adapterNote.notifyDataSetChanged();
    }
}
