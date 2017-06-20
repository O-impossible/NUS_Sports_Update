package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TournamentsListRequestActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String tournamentId;
    private FirebaseAuth mAuth;

    private boolean alreadyRequested = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments_list_request);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        populateListView();
    }

    private void populateListView() {
        //tournament_list view to be populated with tournament data
        final ListView tournamentListView = (ListView) findViewById(R.id.tournaments_list_request);

        final ArrayList<String> tournamentNamesList = new ArrayList<String>();
        final DatabaseReference tournamentRef = mDatabase.child("Tournaments");

        final HashMap<String,String> nameIdMatcher = new HashMap<>();
        final HashMap<String,Object> requestDetailsHashMap = new HashMap<>();

        tournamentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tournament : dataSnapshot.getChildren()) {
                    TournamentInformation tournamentInformation = new TournamentInformation();
                    tournamentInformation.setTournamentName(tournament.getValue(TournamentInformation.class).getTournamentName());
                    tournamentNamesList.add(tournamentInformation.getTournamentName());

                    nameIdMatcher.put(tournamentInformation.getTournamentName(),tournament.getKey());
                    Log.d("Tournament Id:",tournament.getKey());
                    Log.d("Main Activity", tournamentInformation.getTournamentName());
                }

                StringAdapter stringAdapter = new StringAdapter(TournamentsListRequestActivity.this,tournamentNamesList,R.color.tournaments_name_requests_page);
                tournamentListView.setAdapter(stringAdapter);

                tournamentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String tournamentName = tournamentNamesList.get(position);

                        tournamentId = nameIdMatcher.get(tournamentName);
                        Log.d("TournamentName(onclick)",tournamentName);
                        Log.d("TournamentId(onclick)",tournamentId);

                        final RequestDetails requestDetails = new RequestDetails();
                        requestDetails.setTournamentId(tournamentId);
                        requestDetails.setUserId(mAuth.getCurrentUser().getUid());
                        requestDetails.setSport("blank");
                        requestDetails.setOCRequest(true);
                        requestDetails.setParticipantRequest(false);

                        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                        requestsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot requests : dataSnapshot.getChildren()){
                                        RequestDetails retrievedRequest = requests.getValue(RequestDetails.class);
                                        if(retrievedRequest.equals(requestDetails)){
                                            alreadyRequested = true;
                                            Toast toast = Toast.makeText(TournamentsListRequestActivity.this, "Request has already been received!\n" +
                                                    "Please wait for approval!", Toast.LENGTH_SHORT);
                                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                            if( v != null) v.setGravity(Gravity.CENTER);
                                            toast.show();
                                            break;
                                        }
                                    }
                                }
                                if(!alreadyRequested){
                                    requestDetailsHashMap.put("tournamentId",tournamentId);
                                    requestDetailsHashMap.put("userId",mAuth.getCurrentUser().getUid());
                                    requestDetailsHashMap.put("isOCRequest",true);
                                    requestDetailsHashMap.put("isParticipantRequest",false);
                                    requestDetailsHashMap.put("sport","blank");
                                    mDatabase.child("Requests").push().setValue(requestDetailsHashMap);

                                    Toast.makeText(TournamentsListRequestActivity.this,"Request successfully sent!",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TournamentsListRequestActivity.this,MainActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else{
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
