package com.example.myitinerary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Itinerary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        TextView test = findViewById(R.id.test_view);
        test.setText( getIntent().getStringExtra("itinName") );
    }
}