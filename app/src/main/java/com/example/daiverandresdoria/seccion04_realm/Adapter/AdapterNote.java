package com.example.daiverandresdoria.seccion04_realm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.daiverandresdoria.seccion04_realm.Models.Note;
import com.example.daiverandresdoria.seccion04_realm.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterNote extends BaseAdapter {
    private int layout;
    private List<Note> list;
    private Context context;

    public AdapterNote(int layout, List<Note> list, Context context) {
        this.layout = layout;
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.note = (TextView)convertView.findViewById(R.id.textViewNote);
            vh.creatAT = (TextView)convertView.findViewById(R.id.textViewCreatAtNote);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        Note note = list.get(position);

        vh.note.setText(note.getDescription());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dataform = df.format(note.getCreatAT());
        vh.creatAT.setText(dataform);

        return convertView;
    }

    public class ViewHolder{
        TextView note;
        TextView creatAT;
    }
}
