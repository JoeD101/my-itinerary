package com.example.myitinerary;

import android.app.AlertDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Itinerary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        String itinID = getIntent().getStringExtra("id");

        TextView itinNameEdit = findViewById(R.id.editItinName);
        TextView itinDescEdit = findViewById(R.id.editItinDesc);

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

        itinerary.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Toast.makeText(Itinerary.this,
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (snapshot == null) {
                Toast.makeText(Itinerary.this,
                        "Deleted itinerary", Toast.LENGTH_SHORT).show();
            }
        });

        // create new event
        Button newEventBttn = findViewById(R.id.newEventBttn);
        newEventBttn.setOnClickListener(v ->
            setFragment(new NewEventFragment(), itinID));

        //back to home
        ImageButton backItineraries = findViewById(R.id.backItineraries);
        backItineraries.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        //delete itinerary
        ImageButton delete = findViewById(R.id.deleteItin);
        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
            builder.setCancelable(true);
            builder.setTitle("Delete Itinerary");
            builder.setMessage("Are you sure you want to delete this?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        deleteItinerary(itinID, user.getUid());
                        //startActivity(new Intent(Itinerary.this, MainActivity.class));

                        Intent intent = new Intent(Itinerary.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Itinerary.this.startActivity(intent);
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        ImageButton saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(v -> {
            String itinName = itinNameEdit.getText().toString().trim();
            String itinDesc = itinDescEdit.getText().toString().trim();

            itinerary.update("name", itinName);
            itinerary.update("description", itinDesc);

            itinDescEdit.setText(itinDesc);
            itinNameEdit.setText(itinName);
            startActivity(new Intent(Itinerary.this, MainActivity.class));
        });

    }

    private void setFragment(Fragment frag, String itinID) {
        Bundle arguments = new Bundle();
        arguments.putString("itinId" , itinID);
        frag.setArguments(arguments);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
        );
        ConstraintLayout itin = findViewById(R.id.itinerary_layout);
        itin.removeAllViews();
        ft.replace(R.id.itinerary_layout, frag);
        ft.commit();
    }

    public static void deleteItinerary(String itinName, String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itineraries = db.collection("itineraries").document("users");
        CollectionReference userItineraries = itineraries.collection(uid);

        itineraries.update("numItins", FieldValue.increment(-1));
        userItineraries.document(itinName).delete();
    }

}