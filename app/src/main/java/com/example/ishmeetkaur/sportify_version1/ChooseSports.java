package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChooseSports extends AppCompatActivity {

    private ImageView imageFootball;
    private ImageView imageVolleyball;
    private ImageView imageBasketball;
    private ImageView imageCricket;
    private ImageView imageBadminton;
    private ImageView imageTT;
    private FloatingActionButton fab;
    //private HashMap<String,String> selectedSports = new HashMap<>();
    private ArrayList<String> selectedSports = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<String> sportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sports);
        imageFootball = (ImageView) findViewById(R.id.imageFootball);
        imageVolleyball = (ImageView) findViewById(R.id.imageVolleyball);
        imageBasketball = (ImageView) findViewById(R.id.imageBasketball);
        imageCricket = (ImageView) findViewById(R.id.imageCricket);
        imageBadminton = (ImageView) findViewById(R.id.imageBadminton);
        imageTT = (ImageView) findViewById(R.id.imageTT);
        fab = (FloatingActionButton) findViewById(R.id.fabOK);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sportsList = (ArrayList<String>) getIntent().getSerializableExtra("sportsList");




        imageFootball.setOnClickListener(new View.OnClickListener() {
            int buttonClick = 0;
            public void onClick(View view) {
                if (buttonClick==0)
                {
                    // selected
                    imageFootball.setAlpha(0.5f);
                    buttonClick = 1;
                    selectedSports.add("football");
                }
                else
                {
                    imageFootball.setAlpha(1f);
                    buttonClick = 0;
                    selectedSports.remove("football");
                }
            }
        });
        imageVolleyball.setOnClickListener(new View.OnClickListener() {
            int buttonClick = 0;
            public void onClick(View view) {
                if (buttonClick==0)
                {
                    // selected
                    imageVolleyball.setAlpha(0.5f);
                    buttonClick = 1;
                    selectedSports.add("volleyball");
                }
                else
                {
                    imageVolleyball.setAlpha(1f);
                    buttonClick = 0;
                    selectedSports.remove("volleyball");
                }
            }
        });
        imageBasketball.setOnClickListener(new View.OnClickListener() {
            int buttonClick = 0;
            public void onClick(View view) {
                if (buttonClick==0)
                {
                    // selected
                    imageBasketball.setAlpha(0.5f);
                    buttonClick = 1;
                    selectedSports.add("basketball");
                }
                else
                {
                    imageBasketball.setAlpha(1f);
                    buttonClick = 0;
                    selectedSports.remove("basketball");
                }
            }
        });
        imageCricket.setOnClickListener(new View.OnClickListener() {
            int buttonClick = 0;
            public void onClick(View view) {
                if (buttonClick==0)
                {
                    // selected
                    imageCricket.setAlpha(0.5f);
                    buttonClick = 1;
                    selectedSports.add("cricket");
                }
                else
                {
                    imageCricket.setAlpha(1f);
                    buttonClick = 0;
                    selectedSports.remove("cricket");
                }
            }
        });
        imageBadminton.setOnClickListener(new View.OnClickListener() {
            int buttonClick = 0;
            public void onClick(View view) {
                if (buttonClick==0)
                {
                    // selected
                    imageBadminton.setAlpha(0.5f);
                    buttonClick = 1;
                    selectedSports.add("badminton");
                }
                else
                {
                    imageBadminton.setAlpha(1f);
                    buttonClick = 0;
                    selectedSports.remove("badminton");
                }
            }
        });
        imageTT.setOnClickListener(new View.OnClickListener() {
            int buttonClick = 0;
            public void onClick(View view) {
                if (buttonClick==0)
                {
                    // selected
                    imageTT.setAlpha(0.5f);
                    buttonClick = 1;
                    selectedSports.add("table tennis");
                }
                else
                {
                    imageTT.setAlpha(1f);
                    buttonClick = 0;
                    selectedSports.remove("table tennis ");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                databaseReference.child("student").child(firebaseUser.getUid()).child("sports").setValue(selectedSports);
                Toast.makeText(getApplication(), "Selected Sports", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseSports.this,Home.class);
                startActivity(intent);
            }
        });
    }
}
