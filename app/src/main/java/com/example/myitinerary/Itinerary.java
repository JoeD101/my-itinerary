package com.example.myitinerary;

import android.app.AlertDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
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
        TextView itinStartEdit = findViewById(R.id.startTimeEditText);
        TextView itinEndEdit = findViewById(R.id.endTimeEditText);

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
                    itinStartEdit.setText(itinStartEdit.getText() + " " + document.getString("timeStart"));
                    itinEndEdit.setText(itinEndEdit.getText() + " " + document.getString("timeEnd"));

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
                        Intent intent = new Intent(Itinerary.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Itinerary.this.startActivity(intent);
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        //save button
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

        //edit start time
        ImageButton editStartTime = findViewById(R.id.startTimeEdit);
        editStartTime.setOnClickListener(v -> {
            final View dialogView = View.inflate(Itinerary.this, R.layout.date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(Itinerary.this).create();

            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                    TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getHour(),
                            timePicker.getMinute());
                    itinStartEdit.setText("Start Time: " + calendar.getTime().toString());
                    itinerary.update("timeStart", calendar.getTime().toString());

                    alertDialog.dismiss();
                }});
            alertDialog.setView(dialogView);
            alertDialog.show();
        });

        //edit end time
        ImageButton editEndTime = findViewById(R.id.endTimeEdit);
        editEndTime.setOnClickListener(v -> {
            final View dialogView = View.inflate(Itinerary.this, R.layout.date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(Itinerary.this).create();

            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                    TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getHour(),
                            timePicker.getMinute());
                    itinEndEdit.setText("End Time: " + calendar.getTime().toString());
                    itinerary.update("timeEnd", calendar.getTime().toString());

                    alertDialog.dismiss();
                }});
            alertDialog.setView(dialogView);
            alertDialog.show();
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