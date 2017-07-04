package com.example.android.fireapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class LockerRoomActivity extends AppCompatActivity {

    private String tournamentId;
    private String sportName;
    private String userId;
    private String userName;
    private String faculty;

    private EditText messageInput;
    private FloatingActionButton sendFAB;

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
        String [] extras = intent.getStringArrayExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_LOCKERROOM);
        tournamentId = extras[0];
        sportName = extras[1];
        userId = extras[2];
        userName = extras[3];
        faculty = extras[4];

        Log.d("tournamentID:",tournamentId);
        Log.d("name:",userName);
        Log.d("faculty:",faculty);
        Log.d("sport:",sportName);
        Log.d("checking string value:",getString(R.string.lockerroom));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.lockerroom));
        messageInput = (EditText) findViewById(R.id.message_input);
        sendFAB = (FloatingActionButton) findViewById(R.id.send_fab);

        populateView();
    }

    private void populateView() {

    }

    private void callFAB() {
        sendFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = messageInput.getText().toString().trim();
                ChatMessage chatMessage = new ChatMessage(message,userName);
                HashMap<String, Object> chatMessageHashMap = new HashMap<String,Object>();
                chatMessageHashMap.put("messageText",message);
                chatMessageHashMap.put("messageUser",userName);
                chatMessageHashMap.put("messageTime",chatMessage.getMessageTime());
                DatabaseReference pushedRef = mDatabase.child(tournamentId).child(sportName).child(faculty).push();
                pushedRef.updateChildren(chatMessageHashMap);
//                mDatabase.child(Integer.toString(R.string.lockerroom)).child(tournamentId).child(sportName).
//                        child(faculty).updateChildren(chatMessageHashMap);
                messageInput.setText("");
            }
        });
    }


}
