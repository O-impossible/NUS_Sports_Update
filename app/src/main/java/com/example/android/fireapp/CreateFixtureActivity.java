package com.example.android.fireapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class CreateFixtureActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_FROM_CREATE_FIXTURES = "Tournie & Sport Details";

    private Spinner mTeam1Spinner;
    private Spinner mTeam2Spinner;
    private EditText mDateEditText;
    private EditText mTimeEditText;
    private EditText mVenueEditText;
    private Button createFixtureButton;
    private Button cancelButton;

    private String tournamentId;
    private String sportName;

    private String team1;
    private String team2;
    private String venue;

    Calendar myCalendar = Calendar.getInstance();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fixture);
        Intent intent = getIntent();
        String[] extras = intent.getStringArrayExtra(DisplayFixturesActivity.EXTRA_MESSAGE_TO_ADD_FIXTURES);
        tournamentId = extras[0];
        sportName = extras[1];

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTeam1Spinner = (Spinner) findViewById(R.id.team1_spinner);
        mTeam2Spinner = (Spinner) findViewById(R.id.team2_spinner);
        mDateEditText = (EditText) findViewById(R.id.add_date_text_field);
        mTimeEditText = (EditText) findViewById(R.id.add_time_text_field);
        mVenueEditText = (EditText) findViewById(R.id.add_venue_text_field);
        createFixtureButton = (Button) findViewById(R.id.create_fixture_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        setupTeam1Spinner();
        setupTeam2Spinner();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            @TargetApi(24)
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            @TargetApi(24)
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateFixtureActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mTimeEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateFixtureActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        mTimeEditText.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        createFixtureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFixture();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToDisplayFixtures2 = new Intent(CreateFixtureActivity.this,DisplayFixturesActivity.class);
                String[] extras = {tournamentId,sportName};
                intentToDisplayFixtures2.putExtra(EXTRA_MESSAGE_FROM_CREATE_FIXTURES,extras);
                finish();
                startActivity(intentToDisplayFixtures2);
            }
        });
    }

    private void createFixture() {
        if(team1.equals(team2)) {
            Toast.makeText(this, "Contesting teams cannot be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        venue = mVenueEditText.getText().toString().trim();
        HashMap<String,Object> fixtureDetails = new HashMap<>();
        fixtureDetails.put("team1",team1);
        fixtureDetails.put("team2",team2);
        fixtureDetails.put("date",mDateEditText.getText().toString().trim());
        fixtureDetails.put("time",mTimeEditText.getText().toString().trim());
        fixtureDetails.put("venue",venue);
        fixtureDetails.put("team1score","0");
        fixtureDetails.put("team2score","0");
        fixtureDetails.put("ongoing",false);

        DatabaseReference pushId = mDatabase.child("Fixtures").child(tournamentId).child(sportName).push();
        fixtureDetails.put("fixtureId",pushId.getKey());

        pushId.setValue(fixtureDetails);
        Toast.makeText(this, "Fixture Created Successfully!", Toast.LENGTH_SHORT).show();
        Intent intentToDisplayFixtures = new Intent(CreateFixtureActivity.this,DisplayFixturesActivity.class);
        String[] extras = {tournamentId,sportName};
        intentToDisplayFixtures.putExtra(EXTRA_MESSAGE_FROM_CREATE_FIXTURES,extras);
        finish();
        startActivity(intentToDisplayFixtures);
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        mDateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void setupTeam1Spinner() {
        // Create adapter for spinner. The tournament_list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter facultySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_faculty_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple tournament_list view with 1 item per line
        facultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTeam1Spinner.setAdapter(facultySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTeam1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.faculty_biz))) {
                        team1 = "Business";
                    } else if (selection.equals(getString(R.string.faculty_computing))) {
                        team1 = "Computing";
                    } else if (selection.equals(getString(R.string.faculty_dentistry))) {
                        team1 = "Dentistry";
                    } else if (selection.equals(getString(R.string.faculty_fass))) {
                        team1 = "FASS";
                    } else if (selection.equals(getString(R.string.faculty_engin))) {
                        team1 = "Engin";
                    } else if (selection.equals(getString(R.string.faculty_law))) {
                        team1 = "Law";
                    } else if (selection.equals(getString(R.string.faculty_medicine))) {
                        team1 = "Medicine";
                    } else if (selection.equals(getString(R.string.faculty_science))) {
                        team1 = "Science";
                    } else if (selection.equals(getString(R.string.faculty_usp))) {
                        team1 = "USP";
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                team1 = "N/A";
            }
        });
    }

    private void setupTeam2Spinner() {
        // Create adapter for spinner. The tournament_list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter facultySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_faculty_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple tournament_list view with 1 item per line
        facultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTeam2Spinner.setAdapter(facultySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTeam2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.faculty_biz))) {
                        team2 = "Business";
                    } else if (selection.equals(getString(R.string.faculty_computing))) {
                        team2= "Computing";
                    } else if (selection.equals(getString(R.string.faculty_dentistry))) {
                        team2 = "Dentistry";
                    } else if (selection.equals(getString(R.string.faculty_fass))) {
                        team2 = "FASS";
                    } else if (selection.equals(getString(R.string.faculty_engin))) {
                        team2 = "Engin";
                    } else if (selection.equals(getString(R.string.faculty_law))) {
                        team2 = "Law";
                    } else if (selection.equals(getString(R.string.faculty_medicine))) {
                        team2 = "Medicine";
                    } else if (selection.equals(getString(R.string.faculty_science))) {
                        team2 = "Science";
                    } else if (selection.equals(getString(R.string.faculty_usp))) {
                        team2 = "USP";
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                team1 = "N/A";
            }
        });
    }
}
