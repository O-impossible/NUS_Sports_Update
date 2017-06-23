package com.example.android.fireapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Yash Chowdhary on 23-06-2017.
 */

public class FixtureAdapter extends ArrayAdapter<FixtureDetails> {

    private DatabaseReference fixturesRef;

    private TextView team1TextView;
    private TextView team2TextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView venueTextView;
    private TextView team1ScoreTextView;
    private TextView team2ScoreTextView;
    private LinearLayout scoreLayout;

    public FixtureAdapter(Activity context, ArrayList<FixtureDetails> fixtures) {
        super(context, 0, fixtures);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fixture_list_item, parent, false);
        }

        final FixtureDetails retrievedFixture = getItem(position);

        team1TextView = (TextView) listItemView.findViewById(R.id.team1_text_view);
        team2TextView = (TextView) listItemView.findViewById(R.id.team2_text_view);
        dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        venueTextView = (TextView) listItemView.findViewById(R.id.venue_text_view);
        team1ScoreTextView = (TextView) listItemView.findViewById(R.id.team1_score_text_view);
        team2ScoreTextView = (TextView) listItemView.findViewById(R.id.team2_score_text_view);
        scoreLayout = (LinearLayout) listItemView.findViewById(R.id.score_layout);

        team1TextView.setText(retrievedFixture.getTeam1());
        team2TextView.setText(retrievedFixture.getTeam2());
        dateTextView.setText(retrievedFixture.getDate());
        timeTextView.setText(retrievedFixture.getTime());
        venueTextView.setText(retrievedFixture.getVenue());
        if(!retrievedFixture.isOngoing()){
            scoreLayout.setVisibility(View.GONE);
        }
        else{
            team1ScoreTextView.setText(retrievedFixture.getTeam1score());
            team2ScoreTextView.setText(retrievedFixture.getTeam2score());
        }

        return listItemView;
    }
}
