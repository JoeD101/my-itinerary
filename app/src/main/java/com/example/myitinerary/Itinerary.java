package com.example.myitinerary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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

        //String itineraryDesc = getIntent().getStringExtra("itinDescription");



        TextView itinNameEdit = findViewById(R.id.editItinName);
        TextView itinDescEdit = findViewById(R.id.editItinDesc);
        //itinNameEdit.setText(itineraryName);
        //itinDescEdit.setText(itineraryDesc);

        String currentItinNameField;
        String currentItinDesc;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert user != null;
        DocumentReference itinerary =
                db.collection("itineraries").document("users")
                        .collection(user.getUid())
                        .document(itinID);



        //itinerary.get()

        itinerary.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> itineraryData = document.getData();
                    assert itineraryData != null;
                    String itineraryName = document.getString("name");
                    String itineraryDesc = document.getString("description");
                    itinNameEdit.setText(itineraryName);
                    itinDescEdit.setText(itineraryDesc);

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
                            "Deleted: ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton delete = findViewById(R.id.deleteItin);

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete Itinerary");
            builder.setMessage("Are you sure you want to delete this?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItinerary(itinID, user.getUid());
                            //startActivity(new Intent(Itinerary.this, MainActivity.class));

                            Intent intent = new Intent(Itinerary.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Itinerary.this.startActivity(intent);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });



        ImageButton saveBtn = findViewById(R.id.save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itinName = itinNameEdit.getText().toString().trim();
                String itinDesc = itinDescEdit.getText().toString().trim();

                itinerary.update("name", itinName);
                itinerary.update("description", itinDesc);

                itinDescEdit.setText(itinDesc);
                itinNameEdit.setText(itinName);
                startActivity(new Intent(Itinerary.this, MainActivity.class));


                // Do something in response to button click
            }
        });







    }

    public static void deleteItinerary(String itinName, String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itineraries = db.collection("itineraries").document("users");
        CollectionReference userItineraries = itineraries.collection(uid);

        userItineraries.document(itinName).delete();
    }

}