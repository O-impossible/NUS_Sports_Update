package com.example.android.fireapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayFixturesActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_TO_ADD_FIXTURES = "Sport Details";
    public static final String EXTRA_MESSAGE_TO_EDIT_FIXTURES = "Fixture Details";

    private FloatingActionButton fabAddFixture;

    private boolean userLoggedIn = false;
    private String userId = null;
    private String tournamentId;
    private String sportName;

    private boolean fromSportsList;
    private boolean fromParticipantOptions;
    private boolean fromCreateFixture;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures);
        Intent intent = getIntent();

        if(intent.hasExtra(SportsListActivity.EXTRA_MESSAGE_TO_FIXTURES)){
            tournamentId = intent.getStringArrayExtra(SportsListActivity.EXTRA_MESSAGE_TO_FIXTURES)[0];
            sportName = intent.getStringArrayExtra(SportsListActivity.EXTRA_MESSAGE_TO_FIXTURES)[1];
        }
        else if(intent.hasExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_FIXTURES)){
            tournamentId = intent.getStringArrayExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_FIXTURES)[0];
            sportName = intent.getStringArrayExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_FIXTURES)[1];
        }
        else {
            tournamentId = intent.getStringArrayExtra(CreateFixtureActivity.EXTRA_MESSAGE_FROM_CREATE_FIXTURES)[0];
            sportName = intent.getStringArrayExtra(CreateFixtureActivity.EXTRA_MESSAGE_FROM_CREATE_FIXTURES)[1];
        }

        setTitle("FIXTURES - "+sportName);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final ListView fixturesListView = (ListView) findViewById(R.id.fixtures_list);
        final  View emptyView = findViewById(R.id.empty_view_fixtures);

        fabAddFixture = (FloatingActionButton) findViewById(R.id.fab_add_fixture);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //there is a user logged in
                if(firebaseAuth.getCurrentUser()!=null){
                    userId = firebaseAuth.getCurrentUser().getUid();
                    userLoggedIn = true;
                    DatabaseReference mRef = mDatabase.child("Users").child(userId).child("tournamentStatuses").child(tournamentId);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                TournamentStatus tournamentStatus = dataSnapshot.getValue(TournamentStatus.class);
                                if (!tournamentStatus.isOrganizing()) {
                                    fabAddFixture.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //no user logged in
                else{
                    fabAddFixture.setVisibility(View.GONE);
                }
            }
        };

        final ArrayList<FixtureDetails> fixtureDetailsArrayList = new ArrayList<>();

        DatabaseReference fixturesRef = mDatabase.child("Fixtures").child(tournamentId).child(sportName);
        fixturesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot fixtures : dataSnapshot.getChildren()){
                        FixtureDetails retrievedFixture = fixtures.getValue(FixtureDetails.class);
                        fixtureDetailsArrayList.add(retrievedFixture);
                    }
                }
                FixtureAdapter fixtureAdapter = new FixtureAdapter(DisplayFixturesActivity.this,fixtureDetailsArrayList);
                fixturesListView.setAdapter(fixtureAdapter);
                fixturesListView.setEmptyView(emptyView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        fabAddFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddFixture = new Intent(DisplayFixturesActivity.this,CreateFixtureActivity.class);
                if(userLoggedIn){
                    String[] extras = {tournamentId,sportName};
                    intentToAddFixture.putExtra(EXTRA_MESSAGE_TO_ADD_FIXTURES,extras);
                }
                else{
                    String[] extras = {tournamentId,sportName};
                    intentToAddFixture.putExtra(EXTRA_MESSAGE_TO_ADD_FIXTURES,extras);
                }
                finish();
                startActivity(intentToAddFixture);
            }
        });
    }
}
