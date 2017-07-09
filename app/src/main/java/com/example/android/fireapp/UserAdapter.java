package com.example.android.fireapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Yash Chowdhary on 08-07-2017.
 */

public class UserAdapter extends ArrayAdapter<UserInformation> {

    private ArrayList<UserInformation> mUserList;
    private String tournamentId,sportName;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    public UserAdapter(Activity context, ArrayList<UserInformation> users, String tournamentId, String sportName){
        super(context,0,users);
        mUserList = users;
        this.tournamentId = tournamentId;
        this.sportName = sportName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.participant_list_item, parent, false);
        }

        final UserInformation retrievedUser = getItem(position);

        TextView participantView = (TextView) listItemView.findViewById(R.id.participant_name_text_view);
        participantView.setText(retrievedUser.getName());

        ImageView deleteButton = (ImageView) listItemView.findViewById(R.id.delete_participant_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Remove Participant");
                builder.setMessage("Are you sure you want to remove this participant?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // deletes the fixture and all its contents from the realtime database
                        Toast.makeText(getContext(),"Participant Removed",Toast.LENGTH_SHORT).show();

                        mUserList.remove(retrievedUser);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                                    UserInformation userInformation = userSnapshot.getValue(UserInformation.class);
                                    if(userInformation.getEmail().equals(retrievedUser.getEmail())){
                                        userSnapshot.getRef().child("sports").child(tournamentId).child(sportName).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        updateAdapter(mUserList);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return listItemView;
    }

    public synchronized void updateAdapter(ArrayList<UserInformation> users) {
        mUserList.clear();
        mUserList.addAll(users);

        //and call notifyDataSetChanged
        notifyDataSetChanged();
    }
}
