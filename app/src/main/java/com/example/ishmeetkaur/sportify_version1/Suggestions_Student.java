package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Suggestions_Student extends AppCompatActivity implements OnItemSelectedListener
{

    private int sports = 1;
    private int all = 6;
    private int type;
    private String complaint;
    private HashMap<Integer,String> myHash;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions__student);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        myHash = new HashMap<>();
        myHash.put(1,"bLHibQ4JNabIwpAbTsyABJFXrdx1");//badi-chauhan
        myHash.put(2,"43qnQL9GKKNr50SCkOiulh89wDC2");//basketball
        myHash.put(3,"srqK5H531FNAHRxlN3Upo3HTYAf2"); //cricket
        myHash.put(4,"uYuzfzB9dxRJ4gLSoM4dFwOyiSY2"); //football
        myHash.put(5,"A4M01Tjk56egRBgE6rLFEqkDRqy1"); // tt
        myHash.put(6,"bLHibQ4JNabIwpAbTsyABJFXrdx1"); //volley
        final EditText text1 = (EditText) findViewById(R.id.edit_suggestion);


        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.suggestion_spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Equipments/Court");
        categories.add("Trials");
        categories.add("Intramurals");
        categories.add("Others");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        Button button= (Button) findViewById(R.id.submit_suggestion);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!ifAnyChecked())
                {
                    Toast.makeText(getBaseContext(),"No Sports Selected",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getBaseContext(),"Selected sport is: "+sports,Toast.LENGTH_LONG).show();

                    // i am getting sport number and type number

                    complaint = text1.getText().toString().trim();
                    Suggestion_class postThis = new Suggestion_class(type, complaint);

                    String key = databaseReference.child("Suggestions").child(myHash.get(sports)).push().getKey();

                    databaseReference.child("Suggestions").child(myHash.get(sports)).child(key).setValue(postThis);

                    Toast.makeText(getBaseContext(),"SUGGESTION SENT",Toast.LENGTH_SHORT).show();
                    text1.getText().clear();
                    CheckBox badi = findViewById(R.id.check_badi);
                    CheckBox bb = findViewById(R.id.check_bb);
                    CheckBox cricket = findViewById(R.id.check_cricket);
                    CheckBox football = findViewById(R.id.check_football);
                    CheckBox tt = findViewById(R.id.check_tt);
                    CheckBox volley = findViewById(R.id.check_volley);

                    badi.setChecked(false);
                    bb.setChecked(false);
                    cricket.setChecked(false);
                    football.setChecked(false);
                    tt.setChecked(false);
                    volley.setChecked(false);

                    Intent i = new Intent(Suggestions_Student.this,Home.class);
                    startActivity(i);

                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_SHORT).show();
        type = position+1;

    }

    public void onNothingSelected(AdapterView<?> arg0)
    {
        // TODO Auto-generated method stub
    }


    public boolean ifAnyChecked()
    {
        CheckBox badi = findViewById(R.id.check_badi);
        CheckBox bb = findViewById(R.id.check_bb);
        CheckBox cricket = findViewById(R.id.check_cricket);
        CheckBox football = findViewById(R.id.check_football);
        CheckBox tt = findViewById(R.id.check_tt);
        CheckBox volley = findViewById(R.id.check_volley);

        if (badi.isChecked() || bb.isChecked() || cricket.isChecked() || football.isChecked()
                || tt.isChecked() || volley.isChecked())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void makeOtherUnchecked(int k)
    {
        CheckBox badi = findViewById(R.id.check_badi);
        CheckBox bb = findViewById(R.id.check_bb);
        CheckBox cricket = findViewById(R.id.check_cricket);
        CheckBox football = findViewById(R.id.check_football);
        CheckBox tt = findViewById(R.id.check_tt);
        CheckBox volley = findViewById(R.id.check_volley);

        if(k == 1)
        {
            //badi.setChecked(false);
            bb.setChecked(false);
            cricket.setChecked(false);
            football.setChecked(false);
            tt.setChecked(false);
            volley.setChecked(false);
        }
        else if(k == 2)
        {
            badi.setChecked(false);
            //bb.setChecked(false);
            cricket.setChecked(false);
            football.setChecked(false);
            tt.setChecked(false);
            volley.setChecked(false);

        }
        else if(k == 3)
        {
            badi.setChecked(false);
            bb.setChecked(false);
            //cricket.setChecked(false);
            football.setChecked(false);
            tt.setChecked(false);
            volley.setChecked(false);

        }
        else if(k == 4)
        {
            badi.setChecked(false);
            bb.setChecked(false);
            cricket.setChecked(false);
            //football.setChecked(false);
            tt.setChecked(false);
            volley.setChecked(false);

        }
        else if(k == 5)
        {
            badi.setChecked(false);
            bb.setChecked(false);
            cricket.setChecked(false);
            football.setChecked(false);
            //tt.setChecked(false);
            volley.setChecked(false);

        }
        else if(k == 6)
        {
            badi.setChecked(false);
            bb.setChecked(false);
            cricket.setChecked(false);
            football.setChecked(false);
            tt.setChecked(false);
            //volley.setChecked(false);

        }


    }

    public void onCheckboxClicked(View view)
    {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId())
        {
            case R.id.check_badi:
                if (checked)
                {
                    sports = 1;
                    //all--;
                    makeOtherUnchecked(1);
                }

                // Put some meat on the sandwich

                // Remove the meat
                break;

            case R.id.check_bb:
                if (checked)
                {
                    sports = 2;
                    //all--;
                    makeOtherUnchecked(2);
                }// Cheese me

                // I'm lactose intolerant
                break;


            case R.id.check_cricket:
                if (checked)
                {
                    sports = 3;
                    //all--;
                    makeOtherUnchecked(3);
                }// Cheese me

                // I'm lactose intolerant
                break;

            case R.id.check_football:
                if (checked)
                {
                    sports = 4;
                    //all--;
                    makeOtherUnchecked(4);
                }// Cheese me

                // I'm lactose intolerant
                break;


            case R.id.check_tt:
                if (checked)
                {
                    sports = 5;
                    //all--;
                    makeOtherUnchecked(5);
                }// Cheese me


                // I'm lactose intolerant
                break;

            case R.id.check_volley:
                if (checked)
                {
                    sports = 6;
                    //all--;
                    makeOtherUnchecked(6);
                }
                // Cheese me

                // I'm lactose intolerant
                break;

        }
    }

}
