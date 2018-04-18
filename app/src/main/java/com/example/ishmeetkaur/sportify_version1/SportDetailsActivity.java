package com.example.ishmeetkaur.sportify_version1;

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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SportDetailsActivity extends AppCompatActivity {


    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView mRecyclerView;
    recyler_adapter_sportDetails adapter;
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
        setContentView(R.layout.activity_sport_details);

        mRecyclerView = (RecyclerView) findViewById(R.id.sport_details_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        students= new ArrayList<>();
        teamStudents = new ArrayList<>();
        adapter = new SportDetailsActivity.recyler_adapter_sportDetails(teamStudents);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRecyclerView.setAdapter(adapter);



        getFirebaseData(savedInstanceState);


    }



    void getFirebaseData(Bundle savedInstanceState) {


        //find the sport of coordinator
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase.getInstance().getReference().child("coordinator").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Coord coord = snapshot.getValue(Coord.class);
//                    if (coord.getCoordemail().equals(mAuth.getCurrentUser().getEmail())) {
//                        mCoord = coord;
//                    }
//                }
//                Coordsport = mCoord.getCoordSport();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("SPORT");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("SPORT");
        }

        Coordsport= newString;

        Log.v("sport", Coordsport);

        //get all students info
        databaseReference = firebaseDatabase.getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                students.clear();
                teamStudents.clear();
                for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                    Student s = new Student("","","","",new ArrayList<String> ());
                    for (DataSnapshot teamSnapshot : uniqueKeySnapshot.child("information").getChildren()) {
                        String key = (String) teamSnapshot.getKey();
                        String value = (String) teamSnapshot.getValue();
                        if (key.equals("userName")) s.setname(value);
                        if (key.equals("userEmail")) s.setemail(value);
                        if (key.equals("userGender")) s.setgender(value);
                        if (key.equals("userNumber")) s.setphoneno(value);

                    }
                    students.add(s);
                    //Log.v("students", String.valueOf(students.size()));
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

                //fill the teamStudents Array
                for(int i =0 ; i< students.size();i++) {
                    if (students.get(i).getteams().contains(Coordsport))
                    {
                        teamStudents.add(students.get(i));
                        //again setting teh adapter
                        mRecyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("The read failed: ", databaseError.getMessage());
            }


        });




    }


    public class recyler_adapter_sportDetails extends RecyclerView.Adapter<SportDetailsActivity.recyler_adapter_sportDetails.MyViewHolder> {
        ArrayList<Student> students;

        public recyler_adapter_sportDetails(ArrayList<Student> students) {
            this.students = students;
        }


        @Override
        public SportDetailsActivity.recyler_adapter_sportDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sport_details_recycler_content, parent, false);
            return new SportDetailsActivity.recyler_adapter_sportDetails.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SportDetailsActivity.recyler_adapter_sportDetails.MyViewHolder holder, int position) {
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
