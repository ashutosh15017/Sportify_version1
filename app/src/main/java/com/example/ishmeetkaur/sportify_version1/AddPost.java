package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPost extends AppCompatActivity {

    private EditText postText;
    private Spinner typeSpinner;
    private Button postButton;
    private String type,postString,sport,byWhom,isRegistration;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabLogout;
    private Coord mCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getCoord();


        postText = (EditText) findViewById(R.id.postText);
        postButton = (Button) findViewById(R.id.postButton);
        //typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        checkBox = (CheckBox) findViewById(R.id.checkBox);



        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

     /*   typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = typeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type = "general";
            }
        });*/


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

    }

    public void post ()
    {
        if (checkBox.isChecked())
            isRegistration = "yes";

        else
            isRegistration = "no";


        postString = postText.getText().toString();
        if (TextUtils.isEmpty(postString))
        {
            Toast.makeText(getApplicationContext(), "Please add post description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            writeToFirebase();
        }
    }

    public void writeToFirebase()
    {
        Log.d("TYPETAG", "writeToFirebase: " + type);
        String key = databaseReference.child("feed").push().getKey();
        Post post = new Post(sport,type,byWhom,postString,isRegistration);
        databaseReference.child("feed").child(key).setValue(post);
        Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),Home_Coordinator.class);
        startActivity(intent);
    }

    public void getCoord()
    {

        FirebaseDatabase.getInstance().getReference().child("coordinator").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Coord coord = snapshot.getValue(Coord.class);
                    if (coord.getCoordemail().equals(mAuth.getCurrentUser().getEmail()))
                    {
                        mCoord =  coord;
                    }
                }
                sport = mCoord.getCoordSport();
                byWhom = mCoord.getCoordemail();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }
}
