package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SportsListActivity extends AppCompatActivity {

    private String tournamentId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_list);

        Intent intent = getIntent();
        tournamentId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.d("tournieId(slActivity)",tournamentId);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
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
                TournamentInformation tournamentInformation = dataSnapshot.getValue(TournamentInformation.class);
                setTitle(tournamentInformation.getTournamentName());
                if(tournamentInformation.isBadminton()){    sportsList.add("Badminton");    }
                if(tournamentInformation.isBasketball()){    sportsList.add("Basketball");    }
                if(tournamentInformation.isChess()){    sportsList.add("Chess");    }
                if(tournamentInformation.isContractBridge()){    sportsList.add("Contract Bridge");    }
                if(tournamentInformation.isDodgeball()){    sportsList.add("Dodgeball");    }
                if(tournamentInformation.isDota2()){    sportsList.add("Dota 2");    }
                if(tournamentInformation.isFloorball()){    sportsList.add("Floorball");    }
                if(tournamentInformation.isHandball()){    sportsList.add("Handball");    }
                if(tournamentInformation.isNetball()){    sportsList.add("Netball");    }
                if(tournamentInformation.isReversi()){    sportsList.add("Reversi");    }
                if(tournamentInformation.isRoadRelay()){    sportsList.add("Road Relay");    }
                if(tournamentInformation.isSoccer()){    sportsList.add("Soccer");    }
                if(tournamentInformation.isTableTennis()){    sportsList.add("Table Tennis");    }
                if(tournamentInformation.isTchoukball()){    sportsList.add("Tchoukball");    }
                if(tournamentInformation.isTennis()){    sportsList.add("Tennis");    }
                if(tournamentInformation.isTouchFootball()){    sportsList.add("Touch Football");    }
                if(tournamentInformation.isUltimate()){    sportsList.add("Ultimate Frisbee");    }
                if(tournamentInformation.isVolleyball()){    sportsList.add("Volleyball");    }

                StringAdapter stringAdapter = new StringAdapter(SportsListActivity.this,sportsList,R.color.sports_list);
                sportsListView.setAdapter(stringAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
