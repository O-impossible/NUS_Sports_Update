package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ParticipantSportOptions extends AppCompatActivity {

    private String tournamentId;
    private String sportName;
    private String userId;

    private String faculty;
    private String userName;

    private boolean alreadyRequested = false;
    private boolean alreadyDisplayed = false;
    private boolean returned = false;

    private Button mViewFixturesButton;
    private Button mRequestToParticipateButton;
    private Button mLockerRoomButton;
    private Button mViewParticipantsButton;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static final String EXTRA_MESSAGE_TO_FIXTURES = "Sport Details (from P)";
    public static final String EXTRA_MESSAGE_TO_LOCKERROOM = "User Details for LockerRoom";
    public static final String EXTRA_MESSAGE_TO_VIEW_PARTICIPANTS = "Extras to view participants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_sport_options);

        Intent intent = getIntent();
        String[] extras = intent.getStringArrayExtra(SportsListActivity.EXTRA_MESSAGE_TO_OPTIONS);
        tournamentId = extras[0];
        sportName = extras[1];
        userId = extras[2];

        setTitle(sportName.toUpperCase());

        mViewFixturesButton = (Button) findViewById(R.id.view_fixtures_button);
        mRequestToParticipateButton = (Button) findViewById(R.id.request_to_participate_button);
        mLockerRoomButton = (Button) findViewById(R.id.lockerroom_button);
        mViewParticipantsButton = (Button) findViewById(R.id.view_participants_button);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("sports").child(tournamentId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.exists() && dataSnapshot.hasChild(sportName))){
                    mLockerRoomButton.setVisibility(View.GONE);
                }
                if(dataSnapshot.exists() && dataSnapshot.hasChild(sportName)){
                    mRequestToParticipateButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("tournamentStatuses").child(tournamentId);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    TournamentStatus status = dataSnapshot.getValue(TournamentStatus.class);

                    if(status.isOrganizing()){
                        Log.d("isOrganizing","true");
                        mRequestToParticipateButton.setVisibility(View.GONE);
                        mViewParticipantsButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mViewFixturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToFixtures = new Intent(ParticipantSportOptions.this,DisplayFixturesActivity.class);
                String[] extrasToFixtures = {tournamentId, sportName};
                intentToFixtures.putExtra(EXTRA_MESSAGE_TO_FIXTURES,extrasToFixtures);
                startActivity(intentToFixtures);
            }
        });

        mRequestToParticipateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String,Object> requestDetailsHashMap = new HashMap<String, Object>();
                requestDetailsHashMap.put("tournamentId",tournamentId);
                requestDetailsHashMap.put("userId",userId);
                requestDetailsHashMap.put("sport",sportName);
                requestDetailsHashMap.put("isOCRequest",false);
                requestDetailsHashMap.put("isParticipantRequest",true);

                final RequestDetails requestDetails = new RequestDetails();
                requestDetails.setTournamentId(tournamentId);
                requestDetails.setUserId(userId);
                requestDetails.setSport(sportName);
                requestDetails.setParticipantRequest(true);
                requestDetails.setOCRequest(false);

                final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot request : dataSnapshot.getChildren()){
                                RequestDetails retrievedDetails = request.getValue(RequestDetails.class);
                                if(retrievedDetails.equals(requestDetails)){
                                    alreadyRequested = true;
//                                    finish();
//                                    startActivity(getIntent());
                                    break;
                                }
                            }
                        }
                        if(!alreadyRequested){
                            //alreadyDisplayed = true;
                            mDatabase.child("Requests").push().setValue(requestDetailsHashMap);
                            Toast.makeText(ParticipantSportOptions.this, "Request Successfully Sent!", Toast.LENGTH_SHORT).show();
//                            finish();
//                            startActivity(getIntent());

                        }
                        else if(alreadyRequested){
                            Toast toast = Toast.makeText(ParticipantSportOptions.this, "Request has already been received!\n" +
                                            "Please wait for approval!", Toast.LENGTH_SHORT);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            if( v != null) v.setGravity(Gravity.CENTER);
                            toast.show();
//                            Toast.makeText(ParticipantSportOptions.this, "Request has already been received!\nPlease wait for approval!",
//                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        mLockerRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ParticipantSportOptions.this, "Selected LockerRoom", Toast.LENGTH_SHORT).show();

                final Intent intentToLockerRoom = new Intent(ParticipantSportOptions.this,LockerRoomActivity.class);
                DatabaseReference userRef = mDatabase.child("Users").child(userId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation user = dataSnapshot.getValue(UserInformation.class);
                        faculty = user.getFaculty();
                        userName = user.getName();

                        Log.d("name:",userName);
                        Log.d("faculty:",faculty);
                        String [] extrasToLockerRoom = {tournamentId,sportName,userId,userName,faculty};
                        intentToLockerRoom.putExtra(EXTRA_MESSAGE_TO_LOCKERROOM,extrasToLockerRoom);

                        startActivity(intentToLockerRoom);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        mViewParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToViewParticipants = new Intent(ParticipantSportOptions.this,DisplayParticipantsActivity.class);
                String [] extras = {tournamentId,sportName};
                intentToViewParticipants.putExtra(EXTRA_MESSAGE_TO_VIEW_PARTICIPANTS,extras);
                startActivity(intentToViewParticipants);
            }
        });

    }
}
