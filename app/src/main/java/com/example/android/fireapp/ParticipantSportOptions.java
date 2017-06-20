package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private boolean alreadyRequested = false;
    private boolean alreadyDisplayed = false;
    private boolean returned = false;

    private Button mViewFixturesButton;
    private Button mRequestToParticipateButton;
    private Button mLockerRoomButton;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static final String EXTRA_MESSAGE_TO_FIXTURES = "Sport Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_sport_options);

        Intent intent = getIntent();
        String[] extras = intent.getStringArrayExtra(SportsListActivity.EXTRA_MESSAGE_TO_OPTIONS);
        tournamentId = extras[0];
        sportName = extras[1];
        userId = extras[2];

        setTitle(sportName);

        mViewFixturesButton = (Button) findViewById(R.id.view_fixtures_button);
        mRequestToParticipateButton = (Button) findViewById(R.id.request_to_participate_button);
        mLockerRoomButton = (Button) findViewById(R.id.lockerroom_button);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("sports");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.exists() && dataSnapshot.hasChild(sportName))){
                    mLockerRoomButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mViewFixturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToFixtures = new Intent(ParticipantSportOptions.this,FixturesActivity.class);
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
                Toast.makeText(ParticipantSportOptions.this, "Selected Lockerroom", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
