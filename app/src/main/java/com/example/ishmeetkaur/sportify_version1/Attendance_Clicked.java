package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;

public class Attendance_Clicked extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    private ArrayList<TeamMemberAttendance> thisTeam = new ArrayList<>();
    private ArrayList<DayAttendance> allDays = new ArrayList<DayAttendance>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<Integer> day1 = new ArrayList<>();
    private ArrayList<Integer> day2 = new ArrayList<>();
    private ArrayList<Integer> day3 = new ArrayList<>();
    private ArrayList<Integer> day4 = new ArrayList<>();
    private ArrayList<Integer> day5 = new ArrayList<>();
    private ArrayList<String> pushId = new ArrayList<>();
    private int chosen;
    String CoordinatorId = "";
    private int count;

    RecyclerView mRecyclerView;
    recyler_adapter_attendance_clicked adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__clicked);

        Intent mIntent = getIntent();
        chosen = mIntent.getIntExtra("intVariableName", 1);

        //clearing lists
        thisTeam.clear();
        allDays.clear();

        getFirebaseData();

        mRecyclerView = (RecyclerView) findViewById(R.id.attendance_recycler_view_clicked);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        adapter = new recyler_adapter_attendance_clicked(name,day1,day2,day3,day4,day5);

        TextView dayN = (TextView) findViewById(R.id.day_name);

        if(chosen == 1)
            dayN.setText("Monday");
        else if(chosen == 2)
            dayN.setText("Tuesday");
        else if(chosen == 3)
            dayN.setText("Wednesday");
        else if(chosen == 4)
            dayN.setText("Thursday");
        else if(chosen == 5)
            dayN.setText("Friday");

        Button button= (Button) findViewById(R.id.button_done);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"Attendance Marked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Attendance_Clicked.this,Attendance.class);
                startActivity(intent);
            }
        });

    }

    void getFirebaseData()
    {
        // check the auth of the user, which coordinator he is
        // store his name and email(from child info)
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        CoordinatorId = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team");
        //databaseReference2 = FirebaseDatabase.getInstance().getReference("Coordinator");

        final int finalCount = count;
        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                TeamMemberAttendance team = dataSnapshot.getValue(TeamMemberAttendance.class);
                thisTeam.add(team);
                team.makeArray();

//                String mGroupId = databaseReference.push().getKey();
//                databaseReference2.child(CoordinatorId).child("Team")
//                        .child(mGroupId).child("count").setValue(50);


                name.add(team.getName());
                day1.add(team.getMonday());
                day2.add(team.getTuesday());
                day3.add(team.getWednesday());
                day4.add(team.getThursday());
                day5.add(team.getFriday());




//                Log.v("new one", team.getName());
//                Log.v("new one", CoordinatorId);
                mRecyclerView.setAdapter(adapter);

                pushId.add(dataSnapshot.getKey());

                Log.v("ishii",dataSnapshot.getKey());
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


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public class recyler_adapter_attendance_clicked extends RecyclerView.Adapter<recyler_adapter_attendance_clicked.ViewHolder2>
    {
        ArrayList <String> names;
        private ArrayList<Integer> Day1 = new ArrayList<>();
        private ArrayList<Integer> Day2 = new ArrayList<>();
        private ArrayList<Integer> Day3 = new ArrayList<>();
        private ArrayList<Integer> Day4 = new ArrayList<>();
        private ArrayList<Integer> Day5 = new ArrayList<>();
        private int myPos = 0; // to save the position

        public recyler_adapter_attendance_clicked(ArrayList<String> n,ArrayList<Integer> d1,ArrayList<Integer>d2,
                                                  ArrayList<Integer>d3,ArrayList<Integer>d4,ArrayList<Integer>d5)
        {
            names = n;
            Day1 = d1;
            Day2 = d2;
            Day3 = d3;
            Day4 = d4;
            Day5 = d5;

        }

        @Override
        public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_clicked_recycler_content,parent,false);
            return new ViewHolder2(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder2 holder, int position)
        {
            int i22 = position;

            holder.name_text.setText(names.get(position));

            if(chosen == 1)
            {
                //for(int i22=0;i22<Day1.size();i22++)
                {
                    if(Day1.get(i22) == 1)
                    {
                        holder.selectionState.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        //holder.selectionState.setChecked(false);
                    }
                    else if(Day1.get(i22) == 2)
                    {
                        holder.selectionState3.setChecked(true);
                        //holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
                    else if(Day1.get(i22) == 0)
                    {
                        holder.selectionState2.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        //holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
//                    else if(Day1.get(i22) == 10)
//                    {
//                        holder.selectionState3.setChecked(false);
//                        holder.selectionState2.setChecked(false);
//                        holder.selectionState.setChecked(false);
//                    }
                }
            }
                //holder.status_text.setText(String.valueOf(Day1.get(position)));
            else if(chosen == 2)
            {
                //for(int i22=0;i22<Day2.size();i22++)
                {
                    if(Day2.get(i22) == 1)
                    {
                        holder.selectionState.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        //holder.selectionState.setChecked(false);
                    }
                    else if(Day2.get(i22) == 2)
                    {
                        holder.selectionState3.setChecked(true);
                        //holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
                    else if(Day2.get(i22) == 0)
                    {
                        holder.selectionState2.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        //holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
//                    else if(Day2.get(i22) == 10)
//                    {
//                        holder.selectionState3.setChecked(false);
//                        holder.selectionState2.setChecked(false);
//                        holder.selectionState.setChecked(false);
//                    }
                }
            }
                //holder.status_text.setText(String.valueOf(Day2.get(position)));
            else if(chosen == 3)
            {
                //for(int i22=0;i22<Day3.size();i22++)
                {
                    if(Day3.get(i22) == 1)
                    {
                        holder.selectionState.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        //holder.selectionState.setChecked(false);
                    }
                    else if(Day3.get(i22) == 2)
                    {
                        holder.selectionState3.setChecked(true);
                        //holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
                    else if(Day3.get(i22) == 0)
                    {
                        holder.selectionState2.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        //holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
//                    else if(Day3.get(i22) == 10)
//                    {
//                        holder.selectionState3.setChecked(false);
//                        holder.selectionState2.setChecked(false);
//                        holder.selectionState.setChecked(false);
//                    }
                }
            }
                //holder.status_text.setText(String.valueOf(Day3.get(position)));
            else if(chosen == 4)
            {
                //for(int i22=0;i22<Day4.size();i22++)
                {
                    if(Day4.get(i22) == 1)
                    {
                        holder.selectionState.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        //holder.selectionState.setChecked(false);
                    }
                    else if(Day4.get(i22) == 2)
                    {
                        holder.selectionState3.setChecked(true);
                        //holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
                    else if(Day4.get(i22) == 0)
                    {
                        holder.selectionState2.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        //holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
//                    else if(Day4.get(i22) == 10)
//                    {
//                        holder.selectionState3.setChecked(false);
//                        holder.selectionState2.setChecked(false);
//                        holder.selectionState.setChecked(false);
//                    }
                }
            }
                //holder.status_text.setText(String.valueOf(Day4.get(position)));
            else if(chosen == 5)
            {
                //for(int i22=0;i22<Day5.size();i22++)
                {
                    if(Day5.get(i22) == 1)
                    {
                        holder.selectionState.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        //holder.selectionState.setChecked(false);
                    }
                    else if(Day5.get(i22) == 2)
                    {
                        holder.selectionState3.setChecked(true);
                        //holder.selectionState3.setChecked(false);
                        holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
                    else if(Day5.get(i22) == 0)
                    {
                        holder.selectionState2.setChecked(true);
                        holder.selectionState3.setChecked(false);
                        //holder.selectionState2.setChecked(false);
                        holder.selectionState.setChecked(false);
                    }
//                    else if(Day5.get(i22) == 10)
//                    {
//                        holder.selectionState3.setChecked(false);
//                        holder.selectionState2.setChecked(false);
//                        holder.selectionState.setChecked(false);
//                    }
                }
            }
                //holder.status_text.setText(String.valueOf(Day5.get(position)));

            myPos = position;
        }

        @Override
        public int getItemCount()
        {
            return names.size();
        }

        public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            TextView name_text;
            TextView status_text;
            public CheckBox selectionState;
            public CheckBox selectionState2;
            public CheckBox selectionState3;
            public int k1=100;
            public int k2=100;
            public int k3=100;
            public ViewHolder2(View itemView)
            {
                super(itemView);
                name_text  = (TextView) itemView.findViewById(R.id.name);
                selectionState = (CheckBox) itemView.findViewById(R.id.present_check);
                selectionState2 = (CheckBox) itemView.findViewById(R.id.absent_check);
                selectionState3 = (CheckBox) itemView.findViewById(R.id.medical_check);
                //status_text = (TextView) itemView.findViewById(R.id.status);

                String dd = "";

                if(chosen == 1)
                    dd = "monday";
                else if(chosen == 2)
                    dd = "tuesday";
                else if(chosen == 3)
                    dd = "wednesday";
                else if(chosen == 4)
                    dd = "thursday";
                else if(chosen == 5)
                    dd = "friday";

                //item click event listener
                itemView.setOnClickListener(this);

                //checkbox click event handling
                final String finalDd = dd;
                selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked)
                    {
                        if (isChecked)
                        {
                            //Toast.makeText(ProductFilterRecyclerViewAdapter.this.context,
                              //      "selected brand is " + brandName.getText(),
                                //    Toast.LENGTH_LONG).show();
                            selectionState2.setChecked(false);
                            selectionState3.setChecked(false);
                            k1 = 1;

                           //firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference2 = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team").child(pushId.get(getAdapterPosition()));
                            // now i am at the child
                            databaseReference2.child(finalDd).setValue(1);
                            Log.v("ishii2", String.valueOf(getAdapterPosition()));
                        }
                        else
                        {
                            //firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference2 = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team").child(pushId.get(getAdapterPosition()));
                            // now i am at the child
                            databaseReference2.child(finalDd).setValue(10);
                            k1 = 100;
                            Log.v("kkk1","100");
                        }
                    }
                });


                //checkbox click event handling
                selectionState2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked)
                    {
                        if (isChecked)
                        {
                            //Toast.makeText(ProductFilterRecyclerViewAdapter.this.context,
                            //      "selected brand is " + brandName.getText(),
                            //    Toast.LENGTH_LONG).show();
                            selectionState.setChecked(false);
                            selectionState3.setChecked(false);
                            k2 = 1;

                            databaseReference2 = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team").child(pushId.get(getAdapterPosition()));
                            // now i am at the child
                            databaseReference2.child(finalDd).setValue(0);
                        }
                        else
                        {
                            k2 = 100;
                            Log.v("kkk2","100");
                            databaseReference2 = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team").child(pushId.get(getAdapterPosition()));
                            // now i am at the child
                            databaseReference2.child(finalDd).setValue(10);
                        }
                    }
                });



                //checkbox click event handling
                selectionState3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked)
                    {
                        if (isChecked)
                        {
                            //Toast.makeText(ProductFilterRecyclerViewAdapter.this.context,
                            //      "selected brand is " + brandName.getText(),
                            //    Toast.LENGTH_LONG).show();
                            selectionState2.setChecked(false);
                            selectionState.setChecked(false);
                            k3 = 1;

                            databaseReference2 = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team").child(pushId.get(getAdapterPosition()));
                            // now i am at the child
                            databaseReference2.child(finalDd).setValue(2);
                        }
                        else
                        {
                            k3 = 100;
                            Log.v("kkk3","100");
                            databaseReference2 = firebaseDatabase.getReference("Coordinator").child(CoordinatorId).child("Team").child(pushId.get(getAdapterPosition()));
                            // now i am at the child
                            databaseReference2.child(finalDd).setValue(10);
                        }
                    }
                });
            }


            @Override
            public void onClick(View view)
            {

            }
        }

        public void makeOtherUnchecked(int k)
        {
            CheckBox badi = findViewById(R.id.present_check);
            CheckBox bb = findViewById(R.id.absent_check);
            CheckBox cricket = findViewById(R.id.medical_check);


            if(k == 1)
            {
                //badi.setChecked(false);
                bb.setChecked(false);
                cricket.setChecked(false);

            }
            else if(k == 2)
            {
                badi.setChecked(false);
                //bb.setChecked(false);
                cricket.setChecked(false);


            }
            else if(k == 3)
            {
                badi.setChecked(false);
                bb.setChecked(false);
                //cricket.setChecked(false);


            }



        }

//        public void onCheckboxClicked2(View view)
//        {
//            // Is the view now checked?
//            boolean checked = ((CheckBox) view).isChecked();
//
//            // Check which checkbox was clicked
//            switch(view.getId())
//            {
//                case R.id.present_check:
//                    if (checked)
//                    {
//                        makeOtherUnchecked(1);
//                    }
//
//                    // Put some meat on the sandwich
//
//                    // Remove the meat
//                    break;
//
//                case R.id.absent_check:
//                    if (checked)
//                    {
//
//                        makeOtherUnchecked(2);
//                    }// Cheese me
//
//                    // I'm lactose intolerant
//                    break;
//
//
//                case R.id.medical_check:
//                    if (checked)
//                    {
//
//                        makeOtherUnchecked(3);
//                    }// Cheese me
//
//                    // I'm lactose intolerant
//                    break;
//
//
//            }
//        }

    }

//    public void onPause()
//    {
//        super.onPause();
//        getFirebaseData();
//    }
//
//
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        // put your code here...
//        getFirebaseData();
//    }


}
