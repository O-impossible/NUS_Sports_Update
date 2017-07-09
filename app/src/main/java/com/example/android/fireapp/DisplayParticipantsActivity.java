package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayParticipantsActivity extends AppCompatActivity {

    private String tournamentId;
    private String sportName;

    private String userName;

    private ListView listOfParticipants;
    private View emptyView;

    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_participants);

        Intent intent = getIntent();
        String[] extras = intent.getStringArrayExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_VIEW_PARTICIPANTS);
        tournamentId = extras[0];
        sportName = extras[1];

        setTitle("PARTICIPANTS - "+ sportName.toUpperCase());

        final ArrayList<UserInformation> participantList = new ArrayList<>();
        final ListView listOfParticipants = (ListView) findViewById(R.id.list_of_participants);
        final View emptyView = findViewById(R.id.empty_view_no_participants);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    if(user.child("sports").child(tournamentId).hasChild(sportName)){
                        UserInformation userInformation = user.getValue(UserInformation.class);
                        participantList.add(userInformation);
                    }
                }

                UserAdapter userAdapter = new UserAdapter(DisplayParticipantsActivity.this, participantList,tournamentId,sportName);
                listOfParticipants.setAdapter(userAdapter);
                if(participantList.size()==0){
                    listOfParticipants.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else{
                    listOfParticipants.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
