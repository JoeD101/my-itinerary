package com.example.myitinerary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static androidx.core.content.ContextCompat.startActivity;

public class DeleteItinerary extends AppCompatActivity{

    public static void deleteItineraryEntry(FirebaseFirestore db, String uid, String itinName){
        //db.collection("itineraries").document(uid).collection(itinName).get();

        //List<DocumentSnapshot> list = db.collection("itineraries").document(uid).collection(itinName).get()
                /*.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Itinerary deleted.",
                        // Snackbar.LENGTH_SHORT)
                        // .show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.d(TAG, "There was a problem deleting the itinerary", e);
                    }
                });

    */
        db.collection("itineraries").document(uid).collection(itinName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                db.collection("itineraries").document(uid).collection(itinName).document(document.getId()).delete();
                            }
                        }
                    }
                });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itinerary);

        final ImageButton button = findViewById(R.id.deleteItinerary);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteItinerary.this);
                builder.setCancelable(true);
                builder.setTitle("Delete Itinerary");
                builder.setMessage("Are you sure you want to delete this?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}



