package com.example.ishmeetkaur.sportify_version1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.phone.VerifyPhoneNumberFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class C_myTeam_fragment extends Fragment {



    public C_myTeam_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_c_my_team_fragment, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String CoordinatorId = firebaseUser.getUid();
        Log.v("lolva",CoordinatorId);

        ImageView i1 = (ImageView) v.findViewById(R.id.attnIntent);
        i1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getContext(),Attendance.class);
                startActivity(i);
            }
        });

        ImageView i2 = (ImageView) v.findViewById(R.id.sugg_intent);
        i2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // intent to team detail
                Intent i = new Intent(getContext(),TeamDetailsActivity.class);
                startActivity(i);
            }
        });

        ImageView i3 = (ImageView) v.findViewById(R.id.imageView8);
        i3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //logout
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent i = new Intent(getActivity(),Login.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView i4 = (ImageView) v.findViewById(R.id.imageView9);
        i4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //intent to suggestions
                Intent i = new Intent(getContext(),Suggestion_Coordinator.class);
                startActivity(i);
            }
        });

//        Button teamdetailsbutton = (Button) v.findViewById(R.id.buttonTeamDetails);
//        teamdetailsbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(),TeamDetailsActivity.class);
//                startActivity(i);
//            }
//        });
//
//        Button att = (Button) v.findViewById(R.id.buttonAttendance);
//        att.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(),Attendance.class);
//                startActivity(i);
//            }
//        });
//
//
//        Button bun2 = (Button) v.findViewById(R.id.buttonSuggestions);
//        bun2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(),Suggestion_Coordinator.class);
//                startActivity(i);
//            }
//        });
        //work here for attendance and suggestions.

        return v;
    }


}
