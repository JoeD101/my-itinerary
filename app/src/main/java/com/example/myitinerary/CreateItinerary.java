package com.example.myitinerary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateItinerary extends AppCompatActivity {

    // used in registration and createItinerary
    public static void createItineraryEntry(String itinName, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference itinerary = db.collection(uid);
        Map<String, Object> itin1 = new HashMap<>();

        itin1.put("endTime", 0);
        itin1.put("startTime", 0);
        itin1.put("name", "Example");
        itin1.put("events", uid.concat(itinName));
        itinerary.document(itinName).set(itin1);
        CollectionReference events = db.collection(uid.concat(itinName));

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
        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        // go back to main activity
        ImageButton backItinerary = findViewById(R.id.backItineraries);
        backItinerary.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        Button createItinerary = findViewById(R.id.createItineraryBtn);
        createItinerary.setOnClickListener(v -> {

            assert user != null;
            CollectionReference userItineraries = fs.collection(user.getUid());
                });
    }
}