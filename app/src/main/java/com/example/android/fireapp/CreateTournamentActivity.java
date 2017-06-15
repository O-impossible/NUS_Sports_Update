package com.example.android.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class CreateTournamentActivity extends AppCompatActivity {

    private EditText mTournamentName;
    private CheckBox mBadmintonCheckbox;
    private CheckBox mBasketballCheckbox;
    private CheckBox mContractBridgeCheckbox;
    private CheckBox mDodgeballCheckbox;
    private CheckBox mDota2Checkbox;
    private CheckBox mFloorballCheckbox;
    private CheckBox mHandballCheckbox;
    private CheckBox mChessCheckbox;
    private CheckBox mNetballCheckbox;
    private CheckBox mReversiCheckbox;
    private CheckBox mRoadRelayCheckbox;
    private CheckBox mSoccerCheckbox;
    private CheckBox mTableTennisCheckbox;
    private CheckBox mTchoukballCheckbox;
    private CheckBox mTennisCheckbox;
    private CheckBox mTouchFootballCheckbox;
    private CheckBox mUltimateCheckbox;
    private CheckBox mVolleyballCheckbox;

    private boolean badminton;
    private boolean basketball;
    private boolean contractBridge;
    private boolean dodgeball;
    private boolean dota2;
    private boolean floorball;
    private boolean handball;
    private boolean chess;
    private boolean netball;
    private boolean reversi;
    private boolean roadRelay;
    private boolean soccer;
    private boolean tableTennis;
    private boolean tchoukball;
    private boolean tennis;
    private boolean touchFootball;
    private boolean ultimate;
    private boolean volleyball;

    private Button mCreateTournamentButton;

    private DatabaseReference mDatabase;

    private boolean editMode = true;
    private String tournamentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tournaments");

        mTournamentName = (EditText) findViewById(R.id.tournament_name_text_field);

        mBadmintonCheckbox = (CheckBox) findViewById(R.id.badminton_checkbox);
        mBasketballCheckbox = (CheckBox) findViewById(R.id.basketball_checkbox);
        mContractBridgeCheckbox = (CheckBox) findViewById(R.id.contract_bridge_checkbox);
        mDodgeballCheckbox = (CheckBox) findViewById(R.id.dodgeball_checkbox);
        mDota2Checkbox = (CheckBox) findViewById(R.id.dota2_checkbox);
        mFloorballCheckbox = (CheckBox) findViewById(R.id.floorball_checkbox);
        mHandballCheckbox = (CheckBox) findViewById(R.id.handball_checkbox);
        mChessCheckbox = (CheckBox) findViewById(R.id.chess_checkbox);
        mNetballCheckbox = (CheckBox) findViewById(R.id.netball_checkbox);
        mReversiCheckbox = (CheckBox) findViewById(R.id.reversi_checkbox);
        mRoadRelayCheckbox = (CheckBox) findViewById(R.id.road_relay_checkbox);
        mSoccerCheckbox = (CheckBox) findViewById(R.id.soccer_checkbox);
        mTableTennisCheckbox = (CheckBox) findViewById(R.id.table_tennis_checkbox);
        mTchoukballCheckbox = (CheckBox) findViewById(R.id.tchoukball_checkbox);
        mTennisCheckbox = (CheckBox) findViewById(R.id.tennis_checkbox);
        mTouchFootballCheckbox = (CheckBox) findViewById(R.id.touch_football_checkbox);
        mUltimateCheckbox = (CheckBox) findViewById(R.id.ultimate_checkbox);
        mVolleyballCheckbox = (CheckBox) findViewById(R.id.volleyball_checkbox);

        mCreateTournamentButton = (Button) findViewById(R.id.create_tournament_button);

        mCreateTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTournament();
            }
        });

    }


    private void createTournament() {
        String tournamentName = mTournamentName.getText().toString().trim();

        badminton = mBadmintonCheckbox.isChecked();
        basketball = mBasketballCheckbox.isChecked();
        contractBridge = mContractBridgeCheckbox.isChecked();
        chess = mChessCheckbox.isChecked();
        dodgeball = mDodgeballCheckbox.isChecked();
        dota2 = mDota2Checkbox.isChecked();
        floorball = mFloorballCheckbox.isChecked();
        handball = mHandballCheckbox.isChecked();
        netball = mNetballCheckbox.isChecked();
        reversi = mReversiCheckbox.isChecked();
        roadRelay = mRoadRelayCheckbox.isChecked();
        soccer = mSoccerCheckbox.isChecked();
        tableTennis = mTableTennisCheckbox.isChecked();
        tchoukball = mTchoukballCheckbox.isChecked();
        tennis = mTennisCheckbox.isChecked();
        touchFootball = mTouchFootballCheckbox.isChecked();
        ultimate = mUltimateCheckbox.isChecked();
        volleyball = mVolleyballCheckbox.isChecked();

        if(!basketball && !badminton && !contractBridge && !chess && !dodgeball && !dota2 && !floorball && !handball
                && !netball && !reversi && !roadRelay && !soccer && !tableTennis && !tchoukball && !tennis
                && !touchFootball && !ultimate && !volleyball) {
            Toast.makeText(CreateTournamentActivity.this,"You must choose at least 1 sport",Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> tournamentDetails = new HashMap<>();

        tournamentDetails.put("tournamentName", tournamentName);

        tournamentDetails.put("badminton", badminton);
        tournamentDetails.put("basketball", basketball);
        tournamentDetails.put("contractBridge", contractBridge);
        tournamentDetails.put("chess", chess);
        tournamentDetails.put("dodgeball", dodgeball);
        tournamentDetails.put("dota2", dota2);
        tournamentDetails.put("floorball", floorball);
        tournamentDetails.put("handball", handball);
        tournamentDetails.put("netball", netball);
        tournamentDetails.put("reversi", reversi);
        tournamentDetails.put("roadRelay", roadRelay);
        tournamentDetails.put("soccer", soccer);
        tournamentDetails.put("tableTennis", tableTennis);
        tournamentDetails.put("tchoukball", tchoukball);
        tournamentDetails.put("tennis", tennis);
        tournamentDetails.put("touchFootball", touchFootball);
        tournamentDetails.put("ultimate", ultimate);
        tournamentDetails.put("volleyball", volleyball);

        DatabaseReference newRef = mDatabase.push();
        final String tournamentId = newRef.getKey();

        newRef.setValue(tournamentDetails);

        final HashMap<String,Object> tournamentStatus = new HashMap<>();
        tournamentStatus.put("isOrganizing",false);
        tournamentStatus.put("isParticipating",false);

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren()){
                    user.getRef().child("tournamentStatuses").child(tournamentId).updateChildren(tournamentStatus);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(CreateTournamentActivity.this, "Tournament Successfully Added", Toast.LENGTH_SHORT).show();
        Log.d("Main Activity", "Tournament Created - No glitch");

        Intent intent = new Intent(CreateTournamentActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
