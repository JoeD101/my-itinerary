package com.example.myitinerary;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Itinerary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        String itinID = getIntent().getStringExtra("id");
        String itineraryName = getIntent().getStringExtra("itinName");

        TextView itinNameEdit = findViewById(R.id.editItinName);
        TextView itinDescEdit = findViewById(R.id.editItinDesc);
        String currentItinNameField;
        String currentItinDesc;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert user != null;
        DocumentReference itinerary =
                db.collection("itineraries").document("users")
                        .collection(user.getUid())
                        .document(itinID);

        itinerary.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> itineraryData = document.getData();
                    assert itineraryData != null;
                    //itinNameEdit.setText((String) itineraryData.get("description"));

                } else {
                    Toast.makeText(this, "Error: no such document", LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this, "Error", LENGTH_LONG).show();
            }
        });

        itinerary.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Itinerary.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (snapshot != null && snapshot.exists()) {



                } else {
                    Toast.makeText(Itinerary.this,
                            "data is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}