package com.example.daiverandresdoria.seccion04_realm.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.daiverandresdoria.seccion04_realm.Adapter.AdapterBoard;
import com.example.daiverandresdoria.seccion04_realm.Models.Board;
import com.example.daiverandresdoria.seccion04_realm.R;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , RealmChangeListener<RealmResults<Board>>{
    private FloatingActionButton fabBoard;
    private Realm realm;
    private AdapterBoard adapterBoard;
    private ListView listView;
    private RealmResults<Board> boards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);


        fabBoard=findViewById(R.id.fabBoard);
        listView=findViewById(R.id.listView);

        adapterBoard = new AdapterBoard(R.layout.listview_board_item,boards,this);
        listView.setAdapter(adapterBoard);
        listView.setOnItemClickListener(this);
        fabBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBoard("Add new Board","input a new title for the board");
            }
        });

        registerForContextMenu(listView);
    }

    public void newBoard(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title!=null)builder.setTitle(title);
        if (message!=null)builder.setMessage(message);

        View v = LayoutInflater.from(this).inflate(R.layout.alert_dialog_create_board,null);
        builder.setView(v);

        final EditText imput = (EditText) v.findViewById(R.id.alertNewBoard);
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = imput.getText().toString().trim();
                if (boardName.length()>0){
                    createNewBoard(boardName);
                }else{
                    Toast.makeText(BoardActivity.this,"imput a valid title",Toast.LENGTH_SHORT).show();
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.contexmenu_board_activity,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.deleteBoard:
                deleteBoard(boards.get(info.position));
                return true;
            case R.id.EditBoard:
                editBoard("edit name board","input a new board name valid",boards.get(info.position));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editBoard(String title, String message, final Board board){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title!=null)builder.setTitle(title);
        if (message!=null)builder.setMessage(message);

        View v = LayoutInflater.from(this).inflate(R.layout.alert_dialog_create_board,null);
        builder.setView(v);

        final EditText imput = (EditText) v.findViewById(R.id.alertNewBoard);
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = imput.getText().toString().trim();
                if (boardName.length()==0)
                    Toast.makeText(BoardActivity.this,"input a valid board name",Toast.LENGTH_SHORT).show();
                else if (boardName.equals(board.getTitle()))
                    Toast.makeText(BoardActivity.this,"the new board name is the same of current board name",Toast.LENGTH_SHORT).show();
                else{
                    if (boardName.length()>0){
                        editBoard(boardName,board);
                    }else{
                        Toast.makeText(BoardActivity.this,"imput a valid title",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    ///**CRUD***
    private void deleteBoard(Board board){
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }
    private void createNewBoard(final String boardName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Board board = new Board(boardName);
                realm.copyToRealm(board);
            }
        });
    }

    private void editBoard(String newboardname,Board board){
        realm.beginTransaction();
        board.setTitle(newboardname);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("id",boards.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        adapterBoard.notifyDataSetChanged();
    }
}
