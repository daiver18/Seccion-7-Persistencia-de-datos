package com.example.daiverandresdoria.seccion04_realm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.daiverandresdoria.seccion04_realm.Models.Board;
import com.example.daiverandresdoria.seccion04_realm.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import io.realm.RealmResults;

public class AdapterBoard extends BaseAdapter {
    private int layout;
    private List<Board> list;
    private Context context;

    public AdapterBoard(int layout, List<Board> list, Context context) {
        this.layout = layout;
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Board getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView ==null){
         convertView = LayoutInflater.from(context).inflate(layout,null);
         vh = new ViewHolder();
         vh.title = (TextView)convertView.findViewById(R.id.textViewBoardTitle);
         vh.notes = (TextView)convertView.findViewById(R.id.textViewNotes);
         vh.date = (TextView)convertView.findViewById(R.id.textViewDate);
         convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }

        Board board = list.get(position);
        ////////////////// titulo
        vh.title.setText(board.getTitle());
        ////////////////// formato para la fecha
        int numberNotes = board.getNotes().size();
        String textForNotes = (numberNotes==1)? numberNotes+" Note":numberNotes+" Notes";
        vh.notes.setText(textForNotes);
        ///////////////// formato para date
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateformat = df.format(board.getCreatAT());
        vh.date.setText(dateformat);
        return convertView;
    }
    public class ViewHolder{
        TextView title;
        TextView notes;
        TextView date;
    }
}
