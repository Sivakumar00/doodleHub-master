package com.doodleHub.io;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEventActivity extends AppCompatActivity {

    static String TAG = "helo";
    private EditText eventName, eventDate, eventWebsite, eventCoordinator, eventCoordinatorNo, eventDesc;
    PlaceAutocompleteFragment autocompleteFragment;
    String eventInstitution;
    LatLng location;
    double lat, lon;
    Menu menu;
    DatabaseReference mDatabase;
    private Pattern pattern;
    private Matcher matcher;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //init
        Toolbar mToolbar = (Toolbar) findViewById(R.id.add_event_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Event");
        eventName = (EditText) findViewById(R.id.add_event_name);
        eventCoordinator = (EditText) findViewById(R.id.add_event_coordinator_name);
        eventCoordinatorNo = (EditText) findViewById(R.id.add_event_coordinator_number);
        eventDate = (EditText) findViewById(R.id.add_event_date);
        eventDesc = (EditText) findViewById(R.id.add_event_desc);
        eventWebsite = (EditText) findViewById(R.id.add_event_website);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");
        //places
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("IN").setTypeFilter(5).build();
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter).build(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                eventInstitution = place.getName().toString();
                location = place.getLatLng();
                lat = location.latitude;
                lon = location.longitude;


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        
        //date
         myCalendar = Calendar.getInstance();

        
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        eventDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.add_event_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_next) {

            if (!TextUtils.isEmpty( eventName.getText().toString())
                    || !TextUtils.isEmpty(eventInstitution)
                    || !TextUtils.isEmpty(eventCoordinator.getText().toString())
                    || !TextUtils.isEmpty(eventCoordinatorNo.getText().toString())
                    || !TextUtils.isEmpty(eventDate.getText().toString())
                    || !TextUtils.isEmpty(eventDesc.getText().toString())

                    ) {



                    HashMap<String, String> map = new HashMap<>();
                    map.put("title", eventName.getText().toString());
                    map.put("college", eventInstitution.toString());
                    map.put("coordinator", eventCoordinator.getText().toString());
                    map.put("coordinator_no", eventCoordinatorNo.getText().toString());
                    map.put("date", eventDate.getText().toString());
                    map.put("desc", eventDesc.getText().toString());

                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(), "Successfully registered... Kindly fill Event details also", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), AddEventActivity_2.class));
                        }
                    });
                }

            
        }
        return true;
    }
}
