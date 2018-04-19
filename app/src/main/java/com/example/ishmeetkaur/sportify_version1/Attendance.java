package com.example.ishmeetkaur.sportify_version1;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Attendance extends AppCompatActivity
{


    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    private ArrayList<TeamMemberAttendance> thisTeam = new ArrayList<>();
    private ArrayList<DayAttendance> allDays = new ArrayList<DayAttendance>();
    private int count;
    //private ArrayList<Integer> attendanceArray = new ArrayList<Integer>();

    RecyclerView mRecyclerView;
    recyler_adapter_attendance adapter;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //clearing lists
        thisTeam.clear();
        allDays.clear();

        // finding today's date

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

                    v1 = team.attendanceArray.get(k);

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
                //getFirebaseData();

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


//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//
//                //after we finish adding all the child, this function will get called
//
//                for(int k1=0;k1<count;k1++)
//                {
//
//
//                    Log.v("new one", String.valueOf(allDays.get(k1).getPresent()));
//                    Log.v("new one", String.valueOf(allDays.get(k1).getAbsent()));
//                    Log.v("new one", String.valueOf(allDays.get(k1).getMedical()));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });

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
            holder.p.setText("PRESENT: "+String.valueOf(days.get(position).getPresent()));
            holder.a.setText("ABSENT: "+String.valueOf(days.get(position).getAbsent()));
            holder.m.setText("MEDICAL: "+String.valueOf(days.get(position).getMedical()));

            if(position == 0)
                holder.w.setText("monday");
            else if(position == 1)
                holder.w.setText("tuesday");
            else if(position == 2)
                holder.w.setText("wednesday");
            else if(position == 3)
                holder.w.setText("thursday");
            else if(position == 4)
                holder.w.setText("friday");
        }

        @Override
        public int getItemCount()
        {
            return days.size();
        }


        public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            TextView p;
            TextView a;
            TextView m;
            TextView w;

//            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//            String weekday = new DateFormatSymbols().getWeekdays()[dayOfWeek];


            public ViewHolder1(View itemView)
            {
                super(itemView);
                itemView.setOnClickListener(this);
                p  = (TextView) itemView.findViewById(R.id.present);
                a = (TextView) itemView.findViewById(R.id.absent);
                m = (TextView) itemView.findViewById(R.id.medical);
                w = (TextView) itemView.findViewById(R.id.whichDay);
            }

            @Override
            public void onClick(View view)
            {
                Log.v("onClick " , String.valueOf(getLayoutPosition()));
                Intent i11 = new Intent(Attendance.this,Attendance_Clicked.class);
                int sendThis = getLayoutPosition() +1;
                i11.putExtra("intVariableName", sendThis);
                startActivity(i11);
            }
        }
    }


//    public void onPause()
//    {
//        super.onPause();
//        //clearing lists
//        //Toast.makeText(getApplicationContext(),"Attendance Marked",Toast.LENGTH_SHORT).show();
//        thisTeam.clear();
//        allDays.clear();
//        // finding today's date
//        Calendar calendar = Calendar.getInstance();
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//
//
//        count = dayOfWeek - 1;
//        if(count == 0 || count == 6)
//            count = 5;
//
//
//
//        // adding days in the array
//
//        DayAttendance tempDay;
//
//        for(int z=0;z<count;z++)
//        {
//            tempDay = new DayAttendance(0,0,0);
//            allDays.add(tempDay);
//        }
//
//        getFirebaseData();
//    }



//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        //Toast.makeText(getApplicationContext(),"Attendance Marked",Toast.LENGTH_SHORT).show();
//
//        //clearing lists
//        thisTeam.clear();
//        allDays.clear();
//
//        // finding today's date
//        Calendar calendar = Calendar.getInstance();
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//
//
//        count = dayOfWeek - 1;
//        if(count == 0 || count == 6)
//            count = 5;
//
//
//
//        // adding days in the array
//
//        DayAttendance tempDay;
//
//        for(int z=0;z<count;z++)
//        {
//            tempDay = new DayAttendance(0,0,0);
//            allDays.add(tempDay);
//        }
//        // put your code here...
//        getFirebaseData();
//    }

}
