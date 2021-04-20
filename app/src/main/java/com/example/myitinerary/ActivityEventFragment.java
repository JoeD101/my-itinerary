package com.example.myitinerary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityEventFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityEventFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_event, container, false);

        Bundle bundle = this.getArguments();
        //TextView test = view.findViewById(R.id.testItinId);
        assert bundle != null;
        //test.setText(bundle.getString("itinId"));

        return view;
    }
}