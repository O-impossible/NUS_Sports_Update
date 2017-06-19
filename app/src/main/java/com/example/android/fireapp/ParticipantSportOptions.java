package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ParticipantSportOptions extends AppCompatActivity {

    private String tournamentId;
    private String sportName;
    private String userId;

    private Button mViewFixturesButton;
    private Button mRequestToParticipateButton;
    private Button mLockerRoomButton;

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

        mViewFixturesButton = (Button) findViewById(R.id.view_fixtures_button);
        mRequestToParticipateButton = (Button) findViewById(R.id.request_to_participate_button);
        mLockerRoomButton = (Button) findViewById(R.id.lockerroom_button);

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
                HashMap<String,Object> requestDetails = new HashMap<String, Object>();
                requestDetails.put("tournamentId",tournamentId);
                requestDetails.put("userId",userId);
                requestDetails.put("sport",sportName);
                requestDetails.put("isOCRequest",false);
                requestDetails.put("isParticipantRequest",true);

                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                requestRef.push().setValue(requestDetails);

                Toast.makeText(ParticipantSportOptions.this, "Request Successfully Sent!", Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }
}
