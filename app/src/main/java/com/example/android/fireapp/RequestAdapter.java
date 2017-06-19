package com.example.android.fireapp;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yash Chowdhary on 19-06-2017.
 */

public class RequestAdapter extends ArrayAdapter<RequestDetails> {

    private DatabaseReference tournamentRef;
    private DatabaseReference userRef;
    ArrayList<RequestDetails> mRequestsList;

    private HashMap<String,Object> tournamentStatus = new HashMap<>();

    public RequestAdapter(Activity context, ArrayList<RequestDetails> requests){
        super(context,0,requests);
        this.mRequestsList = requests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("Adapter getView()","Invoked");

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.request_list_item, parent, false);
        }

        final RequestDetails retrievedRequest = getItem(position);

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
                    userName.setText("User Name: "+user.getName());
                    userFaculty.setText("Faculty: "+user.getFaculty());
                    userYear.setText("Year: "+Integer.toString(user.getYear()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button mAcceptButton = (Button) listItemView.findViewById(R.id.accept_request_button);
        Button mDenyButton = (Button) listItemView.findViewById(R.id.deny_request_button);

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(retrievedRequest.isParticipantRequest()){
                    tournamentStatus.put("isOrganizing",false);
                    tournamentStatus.put("isParticipating",true);
                }
                else if(retrievedRequest.isOCRequest()){
                    tournamentStatus.put("isOrganizing",true);
                    tournamentStatus.put("isParticipating",false);
                }


                Toast.makeText(getContext(),"Requested Accepted",Toast.LENGTH_SHORT).show();
                userRef.child("tournamentStatuses").child(retrievedRequest.getTournamentId()).updateChildren(tournamentStatus);

                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                requestsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot requestLooper : dataSnapshot.getChildren()){
                                RequestDetails loopedRequest = requestLooper.getValue(RequestDetails.class);
                                if(loopedRequest.equals(retrievedRequest)){
                                    requestLooper.getRef().removeValue();

                                    Log.d("Removing at",requestLooper.getRef().toString());
                                    Log.d("requestList",mRequestsList.toString());

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mRequestsList.remove(retrievedRequest);
                notifyDataSetChanged();
            }
        });

        mDenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                requestsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot requestLooper : dataSnapshot.getChildren()){
                                RequestDetails requestDetails = requestLooper.getValue(RequestDetails.class);

                                if(requestDetails.equals(retrievedRequest)){
                                    requestLooper.getRef().removeValue();

                                    Log.d("Removing at",requestLooper.getRef().toString());
                                    Log.d("requestList",mRequestsList.toString());

                                    Toast.makeText(getContext(),"Requested Denied",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mRequestsList.remove(retrievedRequest);
                notifyDataSetChanged();
            }
        });

        return listItemView;
    }
}
