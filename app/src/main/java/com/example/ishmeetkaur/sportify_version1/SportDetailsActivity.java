package com.example.ishmeetkaur.sportify_version1;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
    Handler mHandler;
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Toast.makeText(getApplicationContext(),"in runnable",Toast.LENGTH_SHORT).show();

            mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable
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

        Button button= (Button) findViewById(R.id.suggestionPlease);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SportDetailsActivity.this,Suggestions_Student.class);
                startActivity(intent);
            }
        });

        TextView cordemail = findViewById(R.id.cord_emailfield);
        TextView cordphone = findViewById(R.id.cord_phonefield);
        cordphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("yes","call clicked");
                TextView phoneno = (TextView) findViewById(R.id.cord_phonefield);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                Log.v("yes",String.valueOf(phoneno.getText()));
                callIntent.setData(Uri.parse("tel:"+String.valueOf(phoneno.getText())));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);

            }
        });
        cordemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView email = (TextView) findViewById(R.id.cord_emailfield);
                String[] to={String.valueOf(email.getText())};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }

            }
        });



        String newString= "";
        newString=getIntent().getStringExtra("SPORT").toString();
        Log.v("sport",newString);
        TextView sportname = (TextView) findViewById(R.id.sportname);
        sportname.setText(newString);
        getFirebaseData(newString);


    }


    @Override
    protected void onResume()
    {
        super.onResume();






    }

    public void Call(View v)
    {
        Log.v("yes","call clicked");
        TextView phoneno = (TextView) findViewById(R.id.cord_phonefield);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        Log.v("yes",String.valueOf(phoneno.getText()));
        callIntent.setData(Uri.parse("tel:"+String.valueOf(phoneno.getText())));
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    public void Email(View v)
    {
        TextView email = (TextView) findViewById(R.id.cord_emailfield);
        String[] to={String.valueOf(email.getText())};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }



    void getFirebaseData(final String newString) {



        //find coord stuff.
        final ArrayList<Coord> Coordinators = new ArrayList<>();

        databaseReference = firebaseDatabase.getReference("coordinator");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                    Coord s = new Coord("","","","","");
                    for (DataSnapshot teamSnapshot : uniqueKeySnapshot.getChildren()) {
                        String key = (String) teamSnapshot.getKey();
                        String value = (String) teamSnapshot.getValue();
                        String name="",email="",no="";
                        if (key.equals("coordname"))
                        {
                            s.setCoordname(value);
                            name =value;

                        }
                        if (key.equals("coordemail"))
                        {
                            s.setCoordemail(value);
                            email = value;
                        }
                        if (key.equals("coordNumber"))
                        {
                            s.setCoordNumber(value);
                            no = value;
                        }
                        if (key.equals("coordSport"))
                        {
                            s.setCoordSport(value);
                        }

                    }
                    Coordinators.add(s);
                    //Log.v("students", String.valueOf(Coordinators.size()));
                }
                TextView cordname = findViewById(R.id.cord_namefield);
                TextView cordemail = findViewById(R.id.cord_emailfield);
                TextView cordphone = findViewById(R.id.cord_phonefield);

                for(int i=0;i<Coordinators.size();i++)
                {
                    Log.v("here", Coordinators.get(i).getCoordname());
                    if(Coordinators.get(i).getCoordSport().equals(newString))
                    {
                        Log.v("here", Coordinators.get(i).getCoordname());
                        cordemail.setText(Coordinators.get(i).getCoordemail());
                        cordname.setText(Coordinators.get(i).getCoordname());
                        cordphone.setText(Coordinators.get(i).getCoordNumber());
                        Log.v("PHONE",Coordinators.get(i).getCoordNumber());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("The read failed: ", databaseError.getMessage());
            }
        });



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
                    if (students.get(i).getteams().contains(newString))
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
                memberPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("yes","call clicked");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        Log.v("yes",String.valueOf(memberPhone.getText()));
                        callIntent.setData(Uri.parse("tel:"+String.valueOf(memberPhone.getText())));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);
                    }
                });

                memberEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] to={String.valueOf(memberEmail.getText())};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                        if (emailIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(emailIntent);
                        }
                    }
                });
            }
        }
    }
}
