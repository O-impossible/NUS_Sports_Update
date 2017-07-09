package com.example.android.fireapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class LockerRoomActivity extends AppCompatActivity {

    private String tournamentId;
    private String sportName;
    private String userId;
    private String userName;
    private String faculty;

    RelativeLayout activity_lockerroom;

    private EditText messageInput;
    private FloatingActionButton sendFAB;

    private FirebaseListAdapter<ChatMessage> adapter;

    private DatabaseReference mDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        callFAB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker_room);

        Intent intent = getIntent();
        String [] extrasForLockerRoom = intent.getStringArrayExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_LOCKERROOM);
        String [] extrasForChatRoom = intent.getStringArrayExtra(SportsListActivity.EXTRA_MESSAGE_TO_CHATROOM);
        if(extrasForLockerRoom!=null) {
            tournamentId = extrasForLockerRoom[0];
            sportName = extrasForLockerRoom[1];
            userId = extrasForLockerRoom[2];
            userName = extrasForLockerRoom[3];
            faculty = extrasForLockerRoom[4];
            setTitle(faculty.toUpperCase() + "'s " + "LOCKERROOM - "+ sportName.toUpperCase());
            mDatabase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.lockerroom)).child(tournamentId).
                    child(sportName).child(faculty);
        }
        else {
            tournamentId = extrasForChatRoom[0];
            userId = extrasForChatRoom[1];
            userName = extrasForChatRoom[2];
            setTitle("ADMIN CHATROOM");
            mDatabase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.chatroom)).child(tournamentId);
        }

        /*Log.d("tournamentID:",tournamentId);
        Log.d("name:",userName);
        Log.d("faculty:",faculty);
        Log.d("sport:",sportName);
        Log.d("checking string value:",getString(R.string.lockerroom));
        */

        messageInput = (EditText) findViewById(R.id.message_input);
        sendFAB = (FloatingActionButton) findViewById(R.id.send_fab);

        activity_lockerroom = (RelativeLayout) findViewById(R.id.activity_lockerroom);

        View emptyView = findViewById(R.id.empty_view_no_messages);
        ListView listOfMessage = (ListView)findViewById(R.id.list_of_messages);
        listOfMessage.setEmptyView(emptyView);

        displayChat();
    }

    private void displayChat() {
        ListView listOfMessage = (ListView)findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.message_list_item,mDatabase)
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        listOfMessage.setAdapter(adapter);
    }

    /*private void populateView() {
        final ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<>();

        final ListView messagesListView = (ListView) findViewById(R.id.list_of_messages);

        mDatabase.child(tournamentId).child(sportName).child(faculty).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot message:dataSnapshot.getChildren()){
                        ChatMessage retrievedMessage = message.getValue(ChatMessage.class);
                        chatMessageArrayList.add(retrievedMessage);
                    }
                }

                ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(LockerRoomActivity.this,chatMessageArrayList);

                View emptyView = findViewById(R.id.empty_view_no_messages);

                messagesListView.setAdapter(chatMessageAdapter);

                if(chatMessageArrayList.size()==0)

                {
                    emptyView.setVisibility(View.VISIBLE);
                    messagesListView.setVisibility(View.GONE);
                }

                else

                {
                    emptyView.setVisibility(View.GONE);
                    messagesListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/


    private void callFAB() {

        sendFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String message = messageInput.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Snackbar.make(activity_lockerroom,"Please enter a message",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ChatMessage chatMessage = new ChatMessage(message,userName);
                HashMap<String, Object> chatMessageHashMap = new HashMap<String,Object>();
                chatMessageHashMap.put("messageText",message);
                chatMessageHashMap.put("messageUser",userName);
                chatMessageHashMap.put("messageTime",chatMessage.getMessageTime());
                DatabaseReference pushedRef = mDatabase.push();
                pushedRef.updateChildren(chatMessageHashMap);
//                mDatabase.child(Integer.toString(R.string.lockerroom)).child(tournamentId).child(sportName).
//                        child(faculty).updateChildren(chatMessageHashMap);
                messageInput.setText("");
                displayChat();
            }
        });
    }


}
