package com.example.myitinerary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateItinerary extends AppCompatActivity {


    // used in registration and createItinerary
    public static void createItineraryEntry(String itinName, String itinDescription, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itineraries = db.collection("itineraries").document("users");

        //update number of itineraries
        itineraries.update("numItins", FieldValue.increment(1));

        CollectionReference userItineraries = itineraries.collection(uid);

        Map<String, Object> itin1 = new HashMap<>();
        //needs automatically calculated and put into INFO document of itinerary event collection
        itin1.put("timeStart", 0);
        itin1.put("timeEnd", 0);
        itin1.put("name", itinName);
        itin1.put("description", itinDescription);
        // set itinerary document id as random, this is identifier always in db, but field is edited
        userItineraries.document().set(itin1);

        // create collection for events in that itinerary
        //userItineraries.document(itinName).collection("events");

        // first event
        /* itin1 = new HashMap<>();
        itin1.put("location", location);
        itin1.put("endTime", endTimeEvent);
        itin1.put("startTime", startTimeEvent);
        events.document(location).set(itin1); */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        EditText itinNameEditText = findViewById(R.id.editNameItinerary);
        EditText itinDescEditText = findViewById(R.id.editDescription);

        // go back to main activity
        ImageButton backItinerary = findViewById(R.id.backItineraries);
        backItinerary.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        Button createItinerary = findViewById(R.id.createItineraryBtn);
        createItinerary.setOnClickListener(v -> {
            // validate input
            String itinName = itinNameEditText.getText().toString().trim();
            if(itinName.isEmpty())
            {
                itinNameEditText.setError("Please enter an itinerary name");
                itinNameEditText.requestFocus();
                return;
            }
            // optional entry
            String itinDescription = itinDescEditText.getText().toString().trim();
            if(itinDescription.isEmpty())
            {
                itinDescription = "";
            }
            assert user != null;
            CreateItinerary.createItineraryEntry(itinName, itinDescription, user.getUid());

            Toast.makeText(CreateItinerary.this,
                    "Itinerary created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateItinerary.this, MainActivity.class));
        });
    }
}