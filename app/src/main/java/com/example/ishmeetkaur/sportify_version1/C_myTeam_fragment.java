package com.example.ishmeetkaur.sportify_version1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.ui.phone.VerifyPhoneNumberFragment;


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

        Button teamdetailsbutton = (Button) v.findViewById(R.id.buttonTeamDetails);
        teamdetailsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),TeamDetailsActivity.class);
                startActivity(i);
            }
        });

        //work here for attendance and suggestions.

        return v;
    }


}
