package com.example.android.fireapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mWelcomeUserMessage;
    private Button mLogOutButton;
    private Button mLogInButton;
    private Button mSignUpButton;

    private FloatingActionButton mFab;

    private FirebaseAuth.AuthStateListener mAuthStatelistener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String userName;
    private String userId = null;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStatelistener);
        populateListView();
    }

    private void populateListView() {
        //list view to be populated with tournament data
        final ListView tournamentListView = (ListView) findViewById(R.id.list);

        //finding and setting the empty view in the listView when the list has 0 items
        View emptyView = findViewById(R.id.empty_view);
        tournamentListView.setEmptyView(emptyView);

        final ArrayList<String> tournamentNamesList = new ArrayList<String>();
        DatabaseReference tournamentRef = mDatabase.child("Tournaments");
        tournamentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tournament : dataSnapshot.getChildren()) {
                    TournamentInformation tournamentInformation = new TournamentInformation();
                    tournamentInformation.setTournamentName(tournament.getValue(TournamentInformation.class).getTournamentName());
                    tournamentNamesList.add(tournamentInformation.getTournamentName());
                    Log.d("Main Activity", tournamentInformation.getTournamentName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, tournamentNamesList);
                tournamentListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mLogInButton = (Button) findViewById(R.id.main_log_in_button);
        mSignUpButton = (Button) findViewById(R.id.main_sign_up_button);

        mWelcomeUserMessage = (TextView) findViewById(R.id.welcome_text_view);
        mLogOutButton = (Button) findViewById(R.id.log_out_button);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthStatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if user is logged in, the sign_in and sing_up buttons should not be displayed
                //however, welcome message and log_out message should be visible
                //Also, the options menu should be visible
                if (firebaseAuth.getCurrentUser() != null) {
                    userId = firebaseAuth.getCurrentUser().getUid();
                    Log.d("Main Activity", "Current userId : " + userId);
                    alterTextView(userId);
                    mLogInButton.setVisibility(View.INVISIBLE);
                    mSignUpButton.setVisibility(View.INVISIBLE);

                    //Invalidate the options menu if the user isGod.
                    //Start by creating a database reference of the user
                    //Add a value event listener, and use the snapshot to retrieve the required data
                    DatabaseReference mRef = mDatabase.child("Users").child(userId);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInformation user = dataSnapshot.getValue(UserInformation.class);
                            if (user.isGod()) {
                                invalidateOptionsMenu();
                            } else {
                                //debugging message
                                Log.d("Main Activity", "Floating Action Button made invisible");

                                mFab.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mLogOutButton.setVisibility(View.VISIBLE);
                } else {
                    //sign_in and sign_up views should be visible
                    //log_out button and welcome view should be invisible
                    //also, the FAB should be invisible
                    mFab.setVisibility(View.INVISIBLE);
                    invalidateOptionsMenu();
                    mLogInButton.setVisibility(View.VISIBLE);
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mWelcomeUserMessage.setVisibility(View.GONE);
                    mLogOutButton.setVisibility(View.GONE);
                }
            }
        };


        //on clicking the sign-up button, sign-up activity is started
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //on clicking the log_in button, log-in activity is started
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //on clicking the fab, create_tournament activity is started
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTournamentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userId != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.tournament_oc_register) {
            // TODO: Open up an activity to choose the tournaments for which user wants to be in OC

        }


        return true;
    }

    private void alterTextView(final String id) {
        if (id != null) {
            mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    userName = userInformation.getName();
                    Log.d("Main Activity", userName);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWelcomeUserMessage.setText("Welcome, " + userName);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    private void signOut() {
        mAuth.signOut();
        userId = null;
        invalidateOptionsMenu();
        mLogInButton.setVisibility(View.VISIBLE);
        mSignUpButton.setVisibility(View.VISIBLE);
        mWelcomeUserMessage.setVisibility(View.INVISIBLE);
        mLogOutButton.setVisibility(View.INVISIBLE);
    }
}
