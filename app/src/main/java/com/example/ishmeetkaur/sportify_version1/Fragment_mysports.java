package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Fragment_mysports extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView mRecyclerView;
    recyler_adapter_mySports adapter;
    ArrayList<String> sportsList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FloatingActionButton fabAdd;


    public Fragment_mysports() {
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

        View rootView =  inflater.inflate(R.layout.fragment_fragment_my_sports, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_sports_recycler_view);
        fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ChooseSports.class);
                intent.putExtra("sportsList",sportsList);
                startActivity(intent);
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        sportsList = new ArrayList<>();
        adapter = new recyler_adapter_mySports(sportsList);
        firebaseDatabase = FirebaseDatabase.getInstance();
        getFirebaseData();

        return rootView;
    }

    void getFirebaseData()
    {
        databaseReference = firebaseDatabase.getReference("student").child(FirebaseAuth.getInstance().getUid()).child("sports");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String obj = dataSnapshot.getValue(String.class);
                sportsList.add(obj);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    public class recyler_adapter_mySports extends RecyclerView.Adapter<recyler_adapter_mySports.MyViewHolder>
    {
        ArrayList<String> sportslist;

        public recyler_adapter_mySports(ArrayList<String> sportslist) {
            this.sportslist = sportslist;
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_sport_recycler_content,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.sportName.setText(sportslist.get(position));
            if (sportslist.get(position).equals("football"))
            {
                holder.sportImage.setImageResource(R.drawable.football);
            }
            else if (sportslist.get(position).equals("volleyball"))
            {
                holder.sportImage.setImageResource(R.drawable.vollyball);
            }
            else if (sportslist.get(position).equals("basketball"))
            {
                holder.sportImage.setImageResource(R.drawable.basketball);
            }
            else if (sportslist.get(position).equals("cricket"))
            {
                holder.sportImage.setImageResource(R.drawable.cricket);
            }
            else if (sportslist.get(position).equals("badminton"))
            {
                holder.sportImage.setImageResource(R.drawable.badminton_player);
            }
            else if (sportslist.get(position).equals("table tennis"))
            {
                holder.sportImage.setImageResource(R.drawable.pin_pong);
            }
        }

        @Override
        public int getItemCount() {
            return sportslist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView sportName;
            ImageView sportImage;
            public MyViewHolder(View itemView) {
                super(itemView);
                 sportName  = (TextView) itemView.findViewById(R.id.sport_name);
                 sportImage = (ImageView) itemView.findViewById(R.id.sport_image);
            }
        }
    }


}
