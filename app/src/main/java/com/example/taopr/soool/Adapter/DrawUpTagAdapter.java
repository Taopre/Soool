package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.taopr.soool.R;

import java.util.ArrayList;

public class DrawUpTagAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    LayoutInflater inflater;

    String TAG = "DrawUpTagAdapter";

    public DrawUpTagAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data != null) return data.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflater.from(context).inflate(R.layout.drawup_tag_spinner, parent, false);
        }

        TextView drawupTagItem = convertView.findViewById(R.id.drawupTagItem);
        drawupTagItem.setText(data.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.from(context).inflate(R.layout.drawup_tag_spinner, parent, false);
        }

        TextView drawupTagItem = convertView.findViewById(R.id.drawupTagItem);
        drawupTagItem.setText(data.get(position));

        return convertView;
    }

}
