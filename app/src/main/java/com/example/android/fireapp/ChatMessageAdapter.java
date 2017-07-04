package com.example.android.fireapp;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Yash Chowdhary on 04-07-2017.
 */

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage>{
    private DatabaseReference lockerroomRef;

    private TextView userView;
    private TextView timeView;
    private TextView messageView;
    private ArrayList<ChatMessage> mMessagesList;

    public ChatMessageAdapter(Activity context, ArrayList<ChatMessage> messages){
        super(context,0,messages);
        mMessagesList = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listViewItem = convertView;
        if(listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_list_item, parent, false);
        }

        userView = (TextView) listViewItem.findViewById(R.id.message_user);
        timeView = (TextView) listViewItem.findViewById(R.id.message_time);
        messageView = (TextView) listViewItem.findViewById(R.id.message_text);

        final ChatMessage message = getItem(position);

        //updateAdapter(mMessagesList);
        userView.setText(message.getMessageUser());
        timeView.setText(Long.toString(message.getMessageTime()));
        messageView.setText(message.getMessageText());


        return listViewItem;
    }

    private synchronized void updateAdapter(ArrayList<ChatMessage> messageArrayList) {
        mMessagesList.clear();
        mMessagesList.addAll(messageArrayList);
        notifyDataSetChanged();
    }
}
