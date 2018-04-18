package com.example.ishmeetkaur.sportify_version1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class Login_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private Button buttonLogin;
        private FirebaseAuth mAuth;
        private EditText emailInput;
        private EditText passInput;

        private FirebaseAuth.AuthStateListener mAuthListener;
        private final List<Coord> coordList = new ArrayList<>();


    public Login_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Coord coord1 = new Coord("Abhishek Chauhan","abhishek15005@iiitd.ac.in","08284844744","volleyball","male");
        Coord coord2 = new Coord("Shivin Das","shivin16091@iiitd.ac.in","9871512830","football","male");
        Coord coord3 = new Coord("Gunkirat Kaur","gunkirat15032@iiitd.ac.in","9871130053","table tennis","female");
        Coord coord4 = new Coord("Hemant Rattey","hemant15040@iiitd.ac.in","+91 88005 48443","basketball","male");
        Coord coord5 = new Coord("Vanshit gupta","vanshit15186@iiitd.ac.in","9990436841","cricket","male");

        coordList.add(coord1);
        coordList.add(coord2);
        coordList.add(coord3);
        coordList.add(coord4);
        coordList.add(coord5);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login_, container, false);
        buttonLogin = (Button) rootView.findViewById(R.id.buttonLogin);
        emailInput = (EditText) rootView.findViewById(R.id.emailInput);
        passInput = (EditText) rootView.findViewById(R.id.passInput);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null)
                {
                    boolean isCord = false;
                    Coord mCord = null;
                    for (Coord coord : coordList)
                    {
                        if (firebaseAuth.getCurrentUser().getEmail().equals(coord.getCoordemail()))
                        {
                            isCord = true;
                        }
                    }

                    if (isCord)
                    {
                        Intent intent = new Intent(getActivity(),Home_Coordinator.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(),Home.class);
                        if(firebaseAuth.getCurrentUser().isEmailVerified())
                            startActivity(intent);
                    }

                }

            }
        };


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });

        TextView forgotPass = (TextView) rootView.findViewById(R.id.forgotpass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseAuth.getInstance().sendPasswordResetEmail(String.valueOf(emailInput.getText()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("email", "Email sent.");
                                }
                            }
                        });
            }
        });

        return rootView;
    }

    public void startLogin()
    {
        final String email = emailInput.getText().toString().trim();
        String pass = passInput.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(getActivity(),"Field empty",Toast.LENGTH_LONG).show();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Signing In");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Signed In",Toast.LENGTH_LONG).show();
                        boolean isCord = false;
                        Coord mCord = null;
                        for (Coord coord : coordList)
                        {
                            Log.d("CORDTAG",coord.getCoordemail());
                            if (email.trim().equals(coord.getCoordemail()))
                            {
                                isCord = true;
                                mCord = coord;
                            }
                        }
                        if (isCord)
                        {
                            Log.d("CORDTAG","YES");
                            Intent i = new Intent(getActivity(),Home_Coordinator.class);
                            i.putExtra("Coordinator",mCord);
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(getActivity(),Home.class);
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                                startActivity(i);
                            else
                            {
                                Toast.makeText(getContext(), "Please verify email first!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }


                    }
                }
            });

        }
    }


}
