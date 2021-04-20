package com.example.myitinerary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
public class ActivityEvent extends AppCompatActivity{

    public static void createActivityEntry(String itinName, String itinDescription, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_event);
        String itinID = getIntent().getStringExtra("id");

        EditText itinNameEditText = findViewById(R.id.editNameActivity);
        EditText itinDescEditText = findViewById(R.id.editDescriptionActivity);

        ImageButton backItinerary = findViewById(R.id.cancelActivities);
        backItinerary.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
        //Firebase user = FirebaseAuth.getInstance().getCurrentUser();

        Button createActivity = findViewById(R.id.createActivityBtn);
        createActivity.setOnClickListener(v -> {
            String itinName = itinNameEditText.getText().toString().trim();
            if (itinName.isEmpty()) {
                itinNameEditText.setError("Enter Activity name");
                itinNameEditText.requestFocus();
                return;
            }

            String itinDescription = itinDescEditText.getText().toString().trim();
            if(itinDescription.isEmpty()) {
                itinDescription = "";
            }
            //assert user != null;
            //ActivityEVent.a
        });


    }
}
