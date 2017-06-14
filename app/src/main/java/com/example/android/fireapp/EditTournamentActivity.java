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

public class EditTournamentActivity extends AppCompatActivity {

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

    private Button mSaveChangesButton;

    private DatabaseReference mDatabase;

    private String tournamentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tournament);

        Intent intent = getIntent();
        tournamentId = intent.getStringExtra(SportsListActivity.EXTRA_MESSAGE);

        Log.d("tournamentID(edit):",tournamentId);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tournaments");

        mTournamentName = (EditText) findViewById(R.id.tournament_name_text_field_edit);

        mBadmintonCheckbox = (CheckBox) findViewById(R.id.badminton_checkbox_edit);
        mBasketballCheckbox = (CheckBox) findViewById(R.id.basketball_checkbox_edit);
        mContractBridgeCheckbox = (CheckBox) findViewById(R.id.contract_bridge_checkbox_edit);
        mDodgeballCheckbox = (CheckBox) findViewById(R.id.dodgeball_checkbox_edit);
        mDota2Checkbox = (CheckBox) findViewById(R.id.dota2_checkbox_edit);
        mFloorballCheckbox = (CheckBox) findViewById(R.id.floorball_checkbox_edit);
        mHandballCheckbox = (CheckBox) findViewById(R.id.handball_checkbox_edit);
        mChessCheckbox = (CheckBox) findViewById(R.id.chess_checkbox_edit);
        mNetballCheckbox = (CheckBox) findViewById(R.id.netball_checkbox_edit);
        mReversiCheckbox = (CheckBox) findViewById(R.id.reversi_checkbox_edit);
        mRoadRelayCheckbox = (CheckBox) findViewById(R.id.road_relay_checkbox_edit);
        mSoccerCheckbox = (CheckBox) findViewById(R.id.soccer_checkbox_edit);
        mTableTennisCheckbox = (CheckBox) findViewById(R.id.table_tennis_checkbox_edit);
        mTchoukballCheckbox = (CheckBox) findViewById(R.id.tchoukball_checkbox_edit);
        mTennisCheckbox = (CheckBox) findViewById(R.id.tennis_checkbox_edit);
        mTouchFootballCheckbox = (CheckBox) findViewById(R.id.touch_football_checkbox_edit);
        mUltimateCheckbox = (CheckBox) findViewById(R.id.ultimate_checkbox_edit);
        mVolleyballCheckbox = (CheckBox) findViewById(R.id.volleyball_checkbox_edit);

        final DatabaseReference tournamentRef = mDatabase.child(tournamentId);
        tournamentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TournamentInformation tournament = dataSnapshot.getValue(TournamentInformation.class);
                Log.d("tournamentname(edit)",tournament.getTournamentName());
                mTournamentName.setText(tournament.getTournamentName());
                if(tournament.hasBadminton()){ mBadmintonCheckbox.toggle(); }
                if(tournament.hasBasketball()){ mBasketballCheckbox.toggle(); }
                if(tournament.hasChess()){ mChessCheckbox.toggle(); }
                if(tournament.hasContractBridge()){ mContractBridgeCheckbox.toggle(); }
                if(tournament.hasDodgeball()){ mDodgeballCheckbox.toggle(); }
                if(tournament.hasDota2()){ mDota2Checkbox.toggle(); }
                if(tournament.hasFloorball()){ mFloorballCheckbox.toggle(); }
                if(tournament.hasHandball()){ mHandballCheckbox.toggle(); }
                if(tournament.hasNetball()){ mNetballCheckbox.toggle(); }
                if(tournament.hasReversi()){ mReversiCheckbox.toggle(); }
                if(tournament.hasRoadRelay()){ mRoadRelayCheckbox.toggle(); }
                if(tournament.hasSoccer()){ mSoccerCheckbox.toggle(); }
                if(tournament.hasTchoukball()){ mTchoukballCheckbox.toggle(); }
                if(tournament.hasTableTennis()){ mTableTennisCheckbox.toggle(); }
                if(tournament.hasTennis()){ mTennisCheckbox.toggle(); }
                if(tournament.hasTouchFootball()){ mTouchFootballCheckbox.toggle(); }
                if(tournament.hasUltimate()){ mUltimateCheckbox.toggle(); }
                if(tournament.hasVolleyball()){ mVolleyballCheckbox.toggle(); }


                mSaveChangesButton = (Button) findViewById(R.id.save_changes_button);

        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTournament(tournamentRef);
            }
        });
    }

    private void editTournament(DatabaseReference mRef) {

                //create the tournament
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

                mRef.updateChildren(tournamentDetails);

                Toast.makeText(EditTournamentActivity.this, "Tournament Successfully Edited", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditTournamentActivity.this, MainActivity.class);
                startActivity(intent);

                Log.d("Main Activity", "Tournament Saved - No glitch");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
