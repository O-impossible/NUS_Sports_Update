package com.example.android.fireapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yash Chowdhary on 12-06-2017.
 */

public class StringAdapter extends ArrayAdapter {
    private int bgColor;

    public StringAdapter(Activity context, ArrayList<String> words){
        super(context, 0, words);
    }

    public StringAdapter(Activity context, ArrayList<String> words, int bgColor){
        super(context, 0, words);
        this.bgColor=bgColor;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;

        if(listViewItem==null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        String currentString = (String) getItem(position);

        TextView textView = (TextView) listViewItem.findViewById(R.id.text1);
        textView.setText(currentString);

        int color = ContextCompat.getColor(getContext(), bgColor);
        textView.setBackgroundColor(color);

        return listViewItem;
    }
}