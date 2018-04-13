package com.example.ishmeetkaur.sportify_version1;

import android.app.ProgressDialog;
import android.os.Handler;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Attendance extends AppCompatActivity
{


    private ProgressDialog working_dialog;

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    private ArrayList<TeamMemberAttendance> thisTeam = new ArrayList<>();
    private ArrayList<DayAttendance> allDays = new ArrayList<DayAttendance>();
    private int count;
    //private ArrayList<Integer> attendanceArray = new ArrayList<Integer>();

    RecyclerView mRecyclerView;
    recyler_adapter_attendance adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //clearing lists
        thisTeam.clear();
        allDays.clear();

        // finding today's date
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        count = dayOfWeek - 1;
        if(count == 0 || count == 6)
            count = 5;



        // adding days in the array

        DayAttendance tempDay;

        for(int z=0;z<count;z++)
        {
            tempDay = new DayAttendance(0,0,0);
            allDays.add(tempDay);
        }




        getFirebaseData();


        mRecyclerView = (RecyclerView) findViewById(R.id.attendance_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        adapter = new recyler_adapter_attendance(allDays);




    }

    void getFirebaseData()
    {
        // check the auth of the user, which coordinator he is
        // store his name and email(from child info)
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String CoordinatorId = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team");


        final int finalCount = count;
        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                TeamMemberAttendance team = dataSnapshot.getValue(TeamMemberAttendance.class);
                thisTeam.add(team);
                team.makeArray();

                //now i have array of attendance in attendanceArray

                //team.attendanceArray.get(1);

                int c = finalCount;

                int v1;

                for(int k=0;k<c;k++)
                {
                    Log.v("lolva", String.valueOf(100));
                    v1 = team.attendanceArray.get(k);
                    Log.v("lolva", String.valueOf(v1));
                    allDays.get(k).setValue(v1);
                    Log.v("lolva", String.valueOf(allDays.get(k).getPresent()));
                }




//                Log.v("new one", team.getName());
//                Log.v("new one", CoordinatorId);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                // i think empty the list here and refill it i.e. copy the above code
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                //after we finish adding all the child, this function will get called

                for(int k1=0;k1<count;k1++)
                {


                    Log.v("new one", String.valueOf(allDays.get(k1).getPresent()));
                    Log.v("new one", String.valueOf(allDays.get(k1).getAbsent()));
                    Log.v("new one", String.valueOf(allDays.get(k1).getMedical()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public class recyler_adapter_attendance extends RecyclerView.Adapter<recyler_adapter_attendance.ViewHolder1>
    {
        ArrayList <DayAttendance> days;

        public recyler_adapter_attendance(ArrayList<DayAttendance> x)
        {
            days = x;
        }

        @Override
        public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_day_recylcer_content,parent,false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder1 holder, int position)
        {
            holder.p.setText(String.valueOf(days.get(position).getPresent()));
            holder.a.setText(String.valueOf(days.get(position).getAbsent()));
            holder.m.setText(String.valueOf(days.get(position).getMedical()));
        }

        @Override
        public int getItemCount()
        {
            return days.size();
        }


        public class ViewHolder1 extends RecyclerView.ViewHolder
        {
            TextView p;
            TextView a;
            TextView m;


            public ViewHolder1(View itemView)
            {
                super(itemView);
                p  = (TextView) itemView.findViewById(R.id.present);
                a = (TextView) itemView.findViewById(R.id.absent);
                m = (TextView) itemView.findViewById(R.id.medical);
            }
        }
    }




}
