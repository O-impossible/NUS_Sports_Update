package com.example.android.fireapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FixturesActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_TO_ADD_FIXTURES = "Sport Details";
    public static final String EXTRA_MESSAGE_TO_EDIT_FIXTURES = "Fixture Details";

    private ListView fixturesListView;
    private View emptyView;
    private FloatingActionButton fabAddFixture;

    private boolean userLoggedIn = false;
    private String userId = null;
    private String tournamentId;
    private String sportName;

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
        final String[] intentDataNoUser = intent.getStringArrayExtra(SportsListActivity.EXTRA_MESSAGE_TO_FIXTURES);
        final String[] intentDataUser = intent.getStringArrayExtra(ParticipantSportOptions.EXTRA_MESSAGE_TO_FIXTURES);
        if(intentDataNoUser == null){
            userLoggedIn = true;
            tournamentId = intentDataUser[0];
            sportName = intentDataUser[1];
            userId = intentDataUser[2];
        }
        else
        {
            tournamentId = intentDataNoUser[0];
            sportName = intentDataNoUser[1];
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fixturesListView = (ListView) findViewById(R.id.fixtures_list);
        emptyView = findViewById(R.id.empty_view_fixtures);
        fabAddFixture = (FloatingActionButton) findViewById(R.id.fab_add_fixture);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //there is a user logged in
                if(firebaseAuth.getCurrentUser()!=null){
                    userId = firebaseAuth.getCurrentUser().getUid();

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

        fabAddFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddFixture = new Intent(FixturesActivity.this,CreateFixtureActivity.class);
                if(userLoggedIn){
                    intentToAddFixture.putExtra(EXTRA_MESSAGE_TO_ADD_FIXTURES,intentDataUser);
                }
                else{
                    intentToAddFixture.putExtra(EXTRA_MESSAGE_TO_ADD_FIXTURES,intentDataNoUser);
                }
                startActivity(intentToAddFixture);
            }
        });
    }
}
