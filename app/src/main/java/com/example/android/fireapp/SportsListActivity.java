package com.example.android.fireapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SportsListActivity extends AppCompatActivity {

    private String tournamentId;
    private DatabaseReference mDatabase;
    private boolean mIsOrganizing = false;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userId;

    public static String EXTRA_MESSAGE_TO_EDIT = "Tournament ID";
    public static final String EXTRA_MESSAGE_TO_FIXTURES = "Sport Details";
    public static final String EXTRA_MESSAGE_TO_OPTIONS = EXTRA_MESSAGE_TO_FIXTURES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_list);

        Intent intent = getIntent();
        tournamentId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.d("tournieId(slActivity)",tournamentId);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_edit_tournament);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //there is a user logged in
                if(firebaseAuth.getCurrentUser()!=null){
                    userId = firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference mRef = mDatabase.child("Users").child(userId);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                UserInformation user = dataSnapshot.getValue(UserInformation.class);
                                if (!user.isGod()) {
                                    mFab.setVisibility(View.INVISIBLE);
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
                    mFab.setVisibility(View.INVISIBLE);
                }
            }
        };

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditTournament = new Intent(SportsListActivity.this,EditTournamentActivity.class);
                intentToEditTournament.putExtra(EXTRA_MESSAGE_TO_EDIT, tournamentId);
                startActivity(intentToEditTournament);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(mAuthStateListener);
        DatabaseReference tournamentRef = mDatabase.child("Tournaments").child(tournamentId);
        populateView(tournamentRef);

        super.onStart();
    }

    private void populateView (DatabaseReference dbRef){
        final ListView sportsListView = (ListView) findViewById(R.id.sports_list);
        final ArrayList<String> sportsList = new ArrayList<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    TournamentInformation tournamentInformation = dataSnapshot.getValue(TournamentInformation.class);
                    setTitle(tournamentInformation.getTournamentName());
                    if (tournamentInformation.hasBadminton()) {
                        sportsList.add("Badminton");
                    }
                    if (tournamentInformation.hasBasketball()) {
                        sportsList.add("Basketball");
                    }
                    if (tournamentInformation.hasChess()) {
                        sportsList.add("Chess");
                    }
                    if (tournamentInformation.hasContractBridge()) {
                        sportsList.add("Contract Bridge");
                    }
                    if (tournamentInformation.hasDodgeball()) {
                        sportsList.add("Dodgeball");
                    }
                    if (tournamentInformation.hasDota2()) {
                        sportsList.add("Dota 2");
                    }
                    if (tournamentInformation.hasFloorball()) {
                        sportsList.add("Floorball");
                    }
                    if (tournamentInformation.hasHandball()) {
                        sportsList.add("Handball");
                    }
                    if (tournamentInformation.hasNetball()) {
                        sportsList.add("Netball");
                    }
                    if (tournamentInformation.hasReversi()) {
                        sportsList.add("Reversi");
                    }
                    if (tournamentInformation.hasRoadRelay()) {
                        sportsList.add("Road Relay");
                    }
                    if (tournamentInformation.hasSoccer()) {
                        sportsList.add("Soccer");
                    }
                    if (tournamentInformation.hasTableTennis()) {
                        sportsList.add("Table Tennis");
                    }
                    if (tournamentInformation.hasTchoukball()) {
                        sportsList.add("Tchoukball");
                    }
                    if (tournamentInformation.hasTennis()) {
                        sportsList.add("Tennis");
                    }
                    if (tournamentInformation.hasTouchFootball()) {
                        sportsList.add("Touch Football");
                    }
                    if (tournamentInformation.hasUltimate()) {
                        sportsList.add("Ultimate Frisbee");
                    }
                    if (tournamentInformation.hasVolleyball()) {
                        sportsList.add("Volleyball");
                    }

                    StringAdapter stringAdapter = new StringAdapter(SportsListActivity.this, sportsList, R.color.sports_list);
                    sportsListView.setAdapter(stringAdapter);

                    sportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String sportName = sportsList.get(position);

                            if(userId == null){
                                //go directly to the fixtures page
                                Intent intent = new Intent(SportsListActivity.this, FixturesActivity.class);
                                String[] extras = {tournamentId, sportName};
                                intent.putExtra(EXTRA_MESSAGE_TO_FIXTURES,extras);
                                startActivity(intent);
                            }
                            else{
                                //go to the page where participant can choose to
                                //a) view fixtures/scores ;
                                //b) request to take part in the sport
                                //c) access the sport's locker-room
                                Intent intent = new Intent(SportsListActivity.this, ParticipantSportOptions.class);
                                String[] extras = {tournamentId, sportName, userId};
                                intent.putExtra(EXTRA_MESSAGE_TO_OPTIONS,extras);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(userId != null){
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("tournamentStatuses").
                    child(tournamentId);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        TournamentStatus tournamentStatus = dataSnapshot.getValue(TournamentStatus.class);
                        if(tournamentStatus.isOrganizing()){
                            invalidateOptionsMenu();
                            mIsOrganizing = true;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mIsOrganizing){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.admin_options_menu,menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.tournament_access_requests){
            Intent intent = new Intent(SportsListActivity.this,DisplayRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.tournament_chatroom){
            //TODO: create an intent to go to the admin chatroom
        }

        return true;
    }
}


