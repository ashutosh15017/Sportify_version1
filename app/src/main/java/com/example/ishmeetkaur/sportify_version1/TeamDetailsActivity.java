package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamDetailsActivity extends AppCompatActivity {


    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView mRecyclerView;
    recyler_adapter_teamDetails adapter;
    ArrayList<Student> teamStudents;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FloatingActionButton fabAdd;
    ArrayList<Student> students;
    private FirebaseAuth mAuth;
    private Coord mCoord;
    private String Coordsport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        mRecyclerView = (RecyclerView) findViewById(R.id.team_details_recycler_view);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        teamStudents = new ArrayList<>();
        adapter = new TeamDetailsActivity.recyler_adapter_teamDetails(teamStudents);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRecyclerView.setAdapter(adapter);

        getFirebaseData();

    }

    void getFirebaseData() {
        //get all students info
        databaseReference = firebaseDatabase.getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                    Student s = new Student();
                    for (DataSnapshot teamSnapshot : uniqueKeySnapshot.child("information").getChildren()) {
                        String key = (String) teamSnapshot.getKey();
                        String value = (String) teamSnapshot.getValue();
                        if (key.equals("userName")) s.setname(value);
                        if (key.equals("userEmail")) s.setemail(value);
                        if (key.equals("userGender")) s.setgender(value);
                        if (key.equals("userNumber")) s.setphoneno(value);

                    }
                    students.add(s);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("The read failed: ", databaseError.getMessage());
            }
        });


        //get All Students teams
        databaseReference = firebaseDatabase.getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int index = 0;
                for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                    ArrayList<String> teams = new ArrayList<>();
                    for (DataSnapshot teamSnapshot : uniqueKeySnapshot.child("team").getChildren()) {
                        String team = (String) teamSnapshot.getValue();
                        teams.add(team);
                        Log.v("TAG", team);
                        mRecyclerView.setAdapter(adapter);
                    }
                    students.get(index).setteam(teams);
                    index++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("The read failed: ", databaseError.getMessage());
            }


        });


        //find the sport of coordinator
        FirebaseDatabase.getInstance().getReference().child("coordinator").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coord coord = snapshot.getValue(Coord.class);
                    if (coord.getCoordemail().equals(mAuth.getCurrentUser().getEmail())) {
                        mCoord = coord;
                    }
                }
                Coordsport = mCoord.getCoordSport();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        //fill the validStudents Array


        //again setting teh adapter

    }


    public class recyler_adapter_teamDetails extends RecyclerView.Adapter<TeamDetailsActivity.recyler_adapter_teamDetails.MyViewHolder> {
        ArrayList<Student> students;

        public recyler_adapter_teamDetails(ArrayList<Student> students) {
            this.students = students;
        }


        @Override
        public TeamDetailsActivity.recyler_adapter_teamDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_details_recycler_content, parent, false);
            return new TeamDetailsActivity.recyler_adapter_teamDetails.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TeamDetailsActivity.recyler_adapter_teamDetails.MyViewHolder holder, int position) {
            holder.memberEmail.setText(students.get(position).getemail());
            holder.memberName.setText(students.get(position).getname());
            holder.memberPhone.setText(students.get(position).getphoneno());
        }

        @Override
        public int getItemCount() {
            return students.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView memberName;
            TextView memberPhone;
            TextView memberEmail;

            public MyViewHolder(View itemView) {
                super(itemView);
                memberName = (TextView) itemView.findViewById(R.id.member_name);
                memberPhone = (TextView) itemView.findViewById(R.id.member_phone);
                memberEmail = (TextView) itemView.findViewById(R.id.member_email);
            }
        }
    }
}
