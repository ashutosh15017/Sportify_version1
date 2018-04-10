package com.example.ishmeetkaur.sportify_version1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUp_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUp_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private Button buttonSignup;
    private EditText emailInput;
    private EditText nameInput;
    private EditText numInput;
    private EditText passInput;
    private EditText confirmPasssInput;
    private CheckBox checkMale;
    private CheckBox checkFemale;

    private String name;
    private String email;
    private String number;
    private String password;
    private String confirmPassword;
    private String gender;


    private DatabaseReference databaseReference;



    public SignUp_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_sign_up_, container, false);
        buttonSignup = (Button) mView.findViewById(R.id.buttonSignup);
        nameInput = (EditText) mView.findViewById(R.id.nameInput);
        emailInput = (EditText) mView.findViewById(R.id.emailInput);
        numInput = (EditText) mView.findViewById(R.id.numInput);
        passInput = (EditText) mView.findViewById(R.id.passInput);
        confirmPasssInput = (EditText) mView.findViewById(R.id.passInput2);
        checkFemale = (CheckBox) mView.findViewById(R.id.checkFemale);
        checkMale = (CheckBox) mView.findViewById(R.id.checkMale);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
        return mView;
    }

    public void startRegister()
    {


        name = nameInput.getText().toString().trim();
        email = emailInput.getText().toString().trim();
        number = numInput.getText().toString().trim();
        password = passInput.getText().toString().trim();
        confirmPassword = passInput.getText().toString().trim();

        if (checkMale.isChecked())
            gender = "male";
        else if (checkFemale.isChecked())
            gender = "female";
        else if (!checkMale.isChecked() && !checkFemale.isChecked())
            Toast.makeText(getActivity(),"select gender",Toast.LENGTH_SHORT).show();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(confirmPassword) )
        {
            Toast.makeText(getActivity(), "Field empty", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword))
        {
            Toast.makeText(getActivity(),"Passwords don't match",Toast.LENGTH_LONG).show();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Signing Up");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        User userStudent = new User(name,email,number,gender);
                        try {
                            databaseReference.child("student").child(firebaseUser.getUid()).child("information").setValue(userStudent);
                        }catch (NullPointerException e){

                        }

                        Intent i = new Intent(getActivity(),ChooseSports.class);
                        startActivity(i);
                        Toast.makeText(getActivity(), "Registered", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }


}
