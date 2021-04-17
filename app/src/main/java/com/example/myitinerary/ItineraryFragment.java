package com.example.myitinerary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

// Fragment shows all itinerary listings, when one clicked, go to Itinerary class activity w/
// add event and display event functions as fragments

public class ItineraryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraries, container, false);

        // add create itinerary on fab
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                CreateItinerary.class)));

        //display itineraries
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert user != null;
        CollectionReference userItineraries =
                db.collection("itineraries").document("users")
                        .collection(user.getUid());

        userItineraries.get().addOnSuccessListener(documents -> {
            if(!documents.isEmpty())
            {
                List<DocumentSnapshot> firebaseItineraries = documents.getDocuments();
                for (DocumentSnapshot d : firebaseItineraries) {
                    // create layouts for each itinerary
                    /*LinearLayout itinBlock = new LinearLayout(getContext());
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    float df =  requireContext().getResources().getDisplayMetrics().density;
                    lp.width = (int) (350 * df);
                    lp.height = (int) (100 * df);
                    itinBlock.setBackground(ContextCompat.getDrawable(requireActivity(),
                            R.drawable.border_rectangle));

                    ConstraintLayout cl = view.findViewById(R.id.layout);
                    cl.addView(itinBlock);*/

                    TextView itinDescription = new TextView(getActivity());
                    itinDescription.setText((String)d.get("description"));

                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll);

                    linearLayout.addView(itinDescription);

                    String description = (String) d.get("description");
                    Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}