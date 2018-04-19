package com.example.ishmeetkaur.sportify_version1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    static ArrayList<Student> validStudents;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    static ArrayList<Student> students;
    private FirebaseAuth mAuth;
    private Coord mCoord;
    private String Coordsport;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);

        mRecyclerView = (RecyclerView) findViewById(R.id.add_team_member_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        students= new ArrayList<>();
        validStudents = new ArrayList<>();
        adapter = new AddTeamMemberActivity.recycler_adapter_add_member(validStudents);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRecyclerView.setAdapter(adapter);



        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText t = (EditText) findViewById(R.id.searchText);
                getFirebaseData(String.valueOf(t.getText()));
            }
        });
    }
    
    public void getFirebaseData(final String userInput)
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
                Log.v("sport",Coordsport);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        students.clear();

        //get all students info
        databaseReference = firebaseDatabase.getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                students.clear();
                validStudents.clear();
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

                        //fill the validStudents Array
                        validStudents.clear();
                        Log.v("SIZE", String.valueOf(validStudents.size()));
                        for(int i =0 ; i< students.size();i++) {
                            if (students.get(i).getname().toLowerCase().startsWith(userInput.toLowerCase()))
                            {
                                Log.v("True?", String.valueOf(students.get(i).getteams().contains(Coordsport)));
                                if(String.valueOf(students.get(i).getteams().contains(Coordsport)).equals("false"))
                                {

                                    validStudents.add(students.get(i));
                                    //again setting teh adapter
                                    mRecyclerView.setAdapter(adapter);
                                }
                            }
                        }
                        Log.v("SIZE", String.valueOf(validStudents.size()));
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("The read failed: ", databaseError.getMessage());
                    }


                });


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

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView memberName;
            TextView memberEmail;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                memberName = (TextView) itemView.findViewById(R.id.student_name);
                memberEmail = (TextView) itemView.findViewById(R.id.student_email);

            }

            @Override
            public void onClick(View view) {
                int pos= getAdapterPosition();
                int correctpos = 0;
                String name="";
                ArrayList<String> team = new ArrayList<>();
                //find position in students array
                for (int i=0;i<AddTeamMemberActivity.students.size();i++)
                {
                    if((validStudents.get(pos).getemail()).equals(AddTeamMemberActivity.students.get(i).getemail())) {
                        correctpos = i;
                        name = AddTeamMemberActivity.students.get(i).getname();
                    }
                }
                team.addAll(AddTeamMemberActivity.students.get(correctpos).getteams());
                Log.v("teams", team.get(0));
                Log.v("coord sport",Coordsport);
                team.add(Coordsport);
                Log.v("teams", team.get(0));

                //make background color lighter.



                //write to Firebase and make toast that member added
                writeTofirebase(correctpos, team,name);
                Log.v("Pos", String.valueOf(correctpos));

            }
        }


        public void writeTofirebase(final int pos, final ArrayList<String> team, String name)
        {
            Log.v("here", String.valueOf(team.size()));
            Log.v("sport",Coordsport);
            //Logv()


            //final int finalCorrectpos = correctpos;

            //do the writing.


            //identify student
            databaseReference = FirebaseDatabase.getInstance().getReference().child("student");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int index =0; String key;
                    for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                        if(index==pos)
                        {
                            key = uniqueKeySnapshot.getKey();
                            FirebaseDatabase.getInstance().getReference().child("student").child(key).child("team").setValue(team);
                        Toast.makeText(getApplicationContext(), "Added to team!",
                                Toast.LENGTH_LONG).show();

                        }
                        index ++;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v("The read failed: ", databaseError.getMessage());
                }
            });

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            final String CoordinatorId = firebaseUser.getUid();

            TeamMemberAttendance team_class = new TeamMemberAttendance(0,name,10,10,10,10,10);
            databaseReference = FirebaseDatabase.getInstance().getReference();

            String key = databaseReference.child("Coordinator").child(CoordinatorId).child("Team").push().getKey();

            databaseReference.child("Coordinator").child(CoordinatorId).child("Team").child(key).setValue(team_class);


        }
    }

}
