package com.example.ishmeetkaur.sportify_version1;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

        count = 2;

        // adding days in the array

        DayAttendance tempDay;

        for(int z=0;z<count;z++)
        {
            tempDay = new DayAttendance(0,0,0);
            allDays.add(tempDay);
        }




        getFirebaseData();








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


}
