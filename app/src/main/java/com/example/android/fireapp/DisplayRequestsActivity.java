package com.example.android.fireapp;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        final ListView requestsListView = (ListView) findViewById(R.id.requests_list);
        requestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");


//        View emptyView = findViewById(R.id.no_requests_textView);


        Log.d("requestDetailsListsize",Integer.toString(requestDetailsArrayList.size()) + "happening first!!");

        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot requests : dataSnapshot.getChildren()){
                        RequestDetails retrievedRequest = requests.getValue(RequestDetails.class);
                        requestDetailsArrayList.add(retrievedRequest);
                        Log.d("requestID",requests.getKey());
                        Log.d("requestListsize(inside)",Integer.toString(requestDetailsArrayList.size()));
                    }
                }

                RequestAdapter requestAdapter = new RequestAdapter(DisplayRequestsActivity.this, requestDetailsArrayList);
                Log.d("checking size again",Integer.toString(requestDetailsArrayList.size()));


                TextView emptyView = (TextView) findViewById(R.id.no_requests_textView);

                requestsListView.setAdapter(requestAdapter);

                if(requestDetailsArrayList.size()==0)

                {
                    emptyView.setVisibility(View.VISIBLE);
                    requestsListView.setVisibility(View.GONE);
                }

                else

                {
                    emptyView.setVisibility(View.GONE);
                    requestsListView.setVisibility(View.VISIBLE);
                }
                //      requestsListView.setEmptyView(emptyView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
