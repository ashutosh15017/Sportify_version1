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

import java.util.ArrayList;

public class Suggestion_Coordinator extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    private ArrayList<String> query_type1 = new ArrayList<>();
    private ArrayList<String> query_type2 = new ArrayList<>();
    private ArrayList<String> query_type3 = new ArrayList<>();
    private ArrayList<String> query_type4 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion__coordinator);

        getFirebaseData();

    }

    void getFirebaseData()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String CoordinatorId = firebaseUser.getUid();
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



    }
}
