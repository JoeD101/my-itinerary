package com.example.myitinerary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Itinerary extends AppCompatActivity {

    public static void deleteItinerary(String itinName, String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itineraries = db.collection("itineraries").document("users");
        CollectionReference userItineraries = itineraries.collection(uid);

        userItineraries.document(itinName).delete();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView title = findViewById(R.id.itinTitle);
        title.setText( getIntent().getStringExtra("itinName") );

        TextView description = findViewById(R.id.itinDescription);
        description.setText(getIntent().getStringExtra("itinName"));



        //delete itinerary
        ImageButton delete = findViewById(R.id.imageButton);

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete Itinerary");
            builder.setMessage("Are you sure you want to delete this?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItinerary(title.getText().toString(), user.getUid());
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
    }
}