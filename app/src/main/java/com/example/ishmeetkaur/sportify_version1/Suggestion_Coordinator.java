package com.example.ishmeetkaur.sportify_version1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Suggestion_Coordinator extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    Query firebase_query;

    private ArrayList<String> query_type1 = new ArrayList<>();
    private ArrayList<String> query_type2 = new ArrayList<>();
    private ArrayList<String> query_type3 = new ArrayList<>();
    private ArrayList<String> query_type4 = new ArrayList<>();

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    private String CoordinatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion__coordinator);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        CoordinatorId = firebaseUser.getUid();

        getFirebaseData();


    }

    void getFirebaseData()
    {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Suggestions").child(CoordinatorId);

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Suggestion_class s_class = dataSnapshot.getValue(Suggestion_class.class);

                if(s_class.getType() == 1)
                {
                    query_type1.add(s_class.getText());
                }
                else if(s_class.getType() == 2)
                {
                    query_type2.add(s_class.getText());
                }
                else if(s_class.getType() == 3)
                {
                    query_type3.add(s_class.getText());
                }
                else if(s_class.getType() == 4)
                {
                    query_type4.add(s_class.getText());
                }

//                if (!myHash.containsKey(s_class.getText())) {
//                    myHash.put(s_class.getText(),s_class.getType());
//                } else {
//                    // the key already exists in the map. It might be a bug, or it might be
//                    // a valid situation that you have to decide how to handle
//                }



                listView = (ExpandableListView)findViewById(R.id.lvExp);
                initData();
                listAdapter = new ExpandableListAdapter(getBaseContext(),listDataHeader,listHash);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                // i think empty the list here and refill it i.e. copy the above code
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                // clear the item from the list
                Suggestion_class s_class2 = dataSnapshot.getValue(Suggestion_class.class);
                int to_delete_from = s_class2.getType();

                if(to_delete_from == 1)
                {
                    query_type1.remove(s_class2.getText());
                }
                else if(to_delete_from == 2)
                {
                    query_type2.remove(s_class2.getText());
                }
                else if(to_delete_from == 3)
                {
                    query_type3.remove(s_class2.getText());
                }
                else if(to_delete_from == 4)
                {
                    query_type4.remove(s_class2.getText());
                }

                listView = (ExpandableListView)findViewById(R.id.lvExp);
                initData();
                listAdapter = new ExpandableListAdapter(getBaseContext(),listDataHeader,listHash);
                listView.setAdapter(listAdapter);
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





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    private void initData()
    {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Equipment/Court");
        listDataHeader.add("Trials");
        listDataHeader.add("Intramural");
        listDataHeader.add("Other");


        listHash.put(listDataHeader.get(0),query_type1);
        listHash.put(listDataHeader.get(1),query_type2);
        listHash.put(listDataHeader.get(2),query_type3);
        listHash.put(listDataHeader.get(3),query_type4);
    }

    public void myClickHandler(View v)
    {
        Log.v("going","good bro");
        LinearLayout vwParentRow = (LinearLayout)v.getParent();

        TextView child = (TextView)vwParentRow.getChildAt(0);
        String input = child.getText().toString();
        Log.v("going",input);
        deleteFromDatabase(input);
    }

    public void deleteFromDatabase(String input)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebase_query = firebaseDatabase.getReference("Suggestions").child(CoordinatorId).orderByChild("text").equalTo(input);

        firebase_query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
                {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
