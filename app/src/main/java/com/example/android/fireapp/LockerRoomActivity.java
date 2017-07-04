package com.example.android.fireapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LockerRoomActivity extends AppCompatActivity {

    private String tournamentId;
    private String sportName;
    private String userId;
    private String userName;
    private String faculty;

    private EditText messageInput;
    private FloatingActionButton sendFAB;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

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

        messageInput = (EditText) findViewById(R.id.message_input);
        sendFAB = (FloatingActionButton) findViewById(R.id.send_fab);


        sendFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                ChatMessage chatMessage = new ChatMessage(message,userName);
                DatabaseReference pushRef = mDatabase.child(Integer.toString(R.string.lockerroom)).child(tournamentId).child(sportName).
                        child(faculty).push();
                pushRef.setValue(chatMessage);
                messageInput.setText("");
            }
        });
    }
}
