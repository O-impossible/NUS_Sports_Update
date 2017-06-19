package com.example.android.fireapp;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Yash Chowdhary on 19-06-2017.
 */

public class RequestAdapter extends ArrayAdapter<RequestDetails> {

    private DatabaseReference tournamentRef;
    private DatabaseReference userRef;
    ArrayList<RequestDetails> mRequestsList;

    public RequestAdapter(Activity context, ArrayList<RequestDetails> requests){
        super(context,0,requests);
        this.mRequestsList = requests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.request_list_item, parent, false);
        }

        RequestDetails retrievedRequest = getItem(position);

        final boolean isOCRequest;
        final boolean isParticipantRequest;

        final TextView tournamentName = (TextView) listItemView.findViewById(R.id.request_textview_tournament_name);
        final TextView userName = (TextView) listItemView.findViewById(R.id.request_textview_user_name);
        final TextView requestType = (TextView) listItemView.findViewById(R.id.request_textview_request_type);
        final TextView userFaculty = (TextView) listItemView.findViewById(R.id.request_textview_user_faculty);
        final TextView userYear = (TextView) listItemView.findViewById(R.id.request_textview_user_year);

        if(retrievedRequest.isParticipantRequest())
            requestType.setText("Request Type: Participant");
        else
            requestType.setText("Request Type: Organizing Committee");

        tournamentRef = FirebaseDatabase.getInstance().getReference().child("Tournaments").child(retrievedRequest.getTournamentId());
        tournamentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    TournamentInformation tournamentInformation = dataSnapshot.getValue(TournamentInformation.class);
                    tournamentName.setText(tournamentInformation.getTournamentName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(retrievedRequest.getUserId());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserInformation user = dataSnapshot.getValue(UserInformation.class);
                    userName.setText(user.getName());
                    userFaculty.setText(user.getFaculty());
                    userYear.setText(Integer.toString(user.getYear()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listItemView;
    }
}
