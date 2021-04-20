package com.example.myitinerary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = this.getArguments();
        //TextView test = view.findViewById(R.id.testItinId);
        assert bundle != null;
        //test.setText(bundle.getString("itinId"));

        // back button
        view.findViewById(R.id.cancelActivities).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Itinerary.class);
            assert bundle != null;
            intent.putExtra("id", bundle.getString("itinId"));
            intent.putExtra("collection", user.getUid());
            //intent.putExtra("itinName", name[0]);
            startActivity(intent);
        });

        return view;
    }
}