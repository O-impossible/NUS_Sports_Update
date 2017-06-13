package com.example.android.fireapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mNameField;
    private EditText mStudentNumberField;
    private EditText mPasswordField;
    private EditText mEmailIdField;

    private Spinner mFacultySpinner;
    private String mFaculty;

    private Button mRegisterButton;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mNameField = (EditText) findViewById(R.id.signup_name_text_field);
        mStudentNumberField = (EditText) findViewById(R.id.signup_student_number_text_field);
        mFacultySpinner = (Spinner) findViewById(R.id.faculty_spinner);
        mEmailIdField = (EditText) findViewById(R.id.signup_email_text_field);
        mPasswordField = (EditText) findViewById(R.id.signup_password_text_field);

        mRegisterButton = (Button) findViewById(R.id.sign_up_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        setupSpinner();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The tournament_list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_faculty_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple tournament_list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFacultySpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mFacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.faculty_biz))) {
                        mFaculty = "Business";
                    } else if (selection.equals(getString(R.string.faculty_computing))) {
                        mFaculty = "Computing";
                    } else if (selection.equals(getString(R.string.faculty_dentistry))) {
                        mFaculty = "Dentistry";
                    } else if (selection.equals(getString(R.string.faculty_fass))) {
                        mFaculty = "FASS";
                    } else if (selection.equals(getString(R.string.faculty_engin))) {
                        mFaculty = "Engin";
                    } else if (selection.equals(getString(R.string.faculty_law))) {
                        mFaculty = "Law";
                    } else if (selection.equals(getString(R.string.faculty_medicine))) {
                        mFaculty = "Medicine";
                    } else if (selection.equals(getString(R.string.faculty_science))) {
                        mFaculty = "Science";
                    } else if (selection.equals(getString(R.string.faculty_usp))) {
                        mFaculty = "USP";
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFaculty = "N/A";
            }
        });
    }

    private void startRegister() {
        final String name = mNameField.getText().toString().trim();
        final String studentNumber = mStudentNumberField.getText().toString().trim();
        final String emailId = mEmailIdField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        mProgressDialog.setMessage("Signing Up ...");
        mProgressDialog.show();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(studentNumber) || TextUtils.isEmpty(emailId) ||
                TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
        else {
            mAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        ArrayList<String> participatingIn = new ArrayList<String>();
                        ArrayList<String> isOrganizing = new ArrayList<String>();
                        DatabaseReference currentUser = mDatabase.child(userId);
                        currentUser.child("name").setValue(name);
                        currentUser.child("studentNumber").setValue(studentNumber);
                        currentUser.child("faculty").setValue(mFaculty);
                        currentUser.child("email").setValue(emailId);
                        currentUser.child("isGod").setValue(false);
                        //currentUser.child("participatingIn").setValue(participatingIn);
                        //currentUser.child("isOrganizing").setValue(isOrganizing);

                        Toast.makeText(RegisterActivity.this,"User successfully registered\nLogged in as "+name,Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                    else{

                        Toast.makeText(RegisterActivity.this,"Registration failed. Please try again",Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        return;
                    }


                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            });
        }
    }

}
