package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddTeamMemberActivity extends AppCompatActivity {
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView mRecyclerView;
    AddTeamMemberActivity.recycler_adapter_add_member adapter;
    ArrayList<Student> validStudents;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FloatingActionButton fabDone;
    ArrayList<Student> students;
    private FirebaseAuth mAuth;
    private Coord mCoord;
    private String Coordsport;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);
        fabDone = (FloatingActionButton) findViewById(R.id.fabDone);

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TeamDetailsActivity.class);
                startActivity(i);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        students= new ArrayList<>();
        validStudents = new ArrayList<>();
        adapter = new AddTeamMemberActivity.recycler_adapter_add_member(validStudents);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRecyclerView.setAdapter(adapter);



        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText t = (TextInputEditText) findViewById(R.id.searchText);
                getFirebaseData(String.valueOf(t.getText()));
            }
        });
    }
    
    public void getFirebaseData(final String userInput)
    {

        //get all students info
        databaseReference = firebaseDatabase.getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
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
                //fill the validStudents Array
                for(int i =0 ; i< students.size();i++) {
                    if (students.get(i).getname().startsWith(userInput))
                    {
                        validStudents.add(students.get(i));
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

    public class recycler_adapter_add_member extends RecyclerView.Adapter<AddTeamMemberActivity.recycler_adapter_add_member.MyViewHolder> {
        ArrayList<Student> students;

        public recycler_adapter_add_member(ArrayList<Student> students) {
            this.students = students;
        }


        @Override
        public AddTeamMemberActivity.recycler_adapter_add_member.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_team_member_recycler_content, parent, false);
            return new AddTeamMemberActivity.recycler_adapter_add_member.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AddTeamMemberActivity.recycler_adapter_add_member.MyViewHolder holder, int position) {
            holder.memberEmail.setText(students.get(position).getemail());
            holder.memberName.setText(students.get(position).getname());
        }

        @Override
        public int getItemCount() {
            return students.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView memberName;
            TextView memberEmail;

            public MyViewHolder(View itemView) {
                super(itemView);
                memberName = (TextView) itemView.findViewById(R.id.student_name);
                memberEmail = (TextView) itemView.findViewById(R.id.student_email);
                LinearLayout ll = (LinearLayout) itemView.findViewById(R.id.ll_layout);

                ll.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        int pos= getAdapterPosition();
                        //make background color lighter.

                        //write to Firebase and make toast that member added
                        writeTofirebase(pos);

                    }
                });
            }
        }


        public void writeTofirebase(final int pos)
        {
            //get sport.
            mAuth = FirebaseAuth.getInstance();
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

            //identify student
            databaseReference = firebaseDatabase.getReference("student");
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int index =0;
                    for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                        Student s = new Student("","","","",new ArrayList<String> ());
                        for (DataSnapshot teamSnapshot : uniqueKeySnapshot.child("team").getChildren()) {
                            if(index==pos)
                            {
                                //write.
                                databaseReference.child(String.valueOf(pos)).setValue(Coordsport);

                                //make Toast
                                Toast.makeText(AddTeamMemberActivity.this,
                                        students.get(pos).getname()+ " Added!", Toast.LENGTH_SHORT).show();

                            }
                        }
                        index ++;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v("The read failed: ", databaseError.getMessage());
                }
            });

        }
    }

}
