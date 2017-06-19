package com.example.android.fireapp;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayRequestsActivity extends AppCompatActivity {

    private DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_requests);

        final ArrayList<RequestDetails> requestDetailsArrayList = new ArrayList<>();
        requestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");

        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot requests : dataSnapshot.getChildren()){
                        RequestDetails retrievedRequest = requests.getValue(RequestDetails.class);
                        requestDetailsArrayList.add(retrievedRequest);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        View emptyView = findViewById(R.id.no_requests_textView);
        RequestAdapter requestAdapter = new RequestAdapter(this, requestDetailsArrayList);

        ListView requestsListView = (ListView) findViewById(R.id.requests_list);
        requestsListView.setAdapter(requestAdapter);
        requestsListView.setEmptyView(emptyView);

    }
}
