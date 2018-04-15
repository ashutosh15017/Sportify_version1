package com.example.ishmeetkaur.sportify_version1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Fragment_feeds extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    RecyclerView mRecyclerView;
    ArrayList<Post> postList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    recyler_adapter_feed adapter;
    ArrayList<String> sportsList;
    FirebaseAuth mAuth;

    public Fragment_feeds() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_feeds, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        postList = new ArrayList<>();
        sportsList = new ArrayList<>();
        adapter = new recyler_adapter_feed(postList);
        firebaseDatabase = FirebaseDatabase.getInstance();
        getFirebaseSports();
        getFirebaseData();


        return rootView;
    }

    void getFirebaseSports()
    {
        databaseReference = firebaseDatabase.getReference("student").child(FirebaseAuth.getInstance().getUid()).child("sports");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String obj = dataSnapshot.getValue(String.class);
                sportsList.add(obj);
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

    void getFirebaseData()
    {
        databaseReference = firebaseDatabase.getReference("feed");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                post.setPostKey(dataSnapshot.getKey());
                if (sportsList.contains(post.getSport()))
                    postList.add(post);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                postList.remove(post);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class recyler_adapter_feed extends RecyclerView.Adapter<recyler_adapter_feed.MyViewHolder>
    {
        List<String> registerdEmails = new ArrayList<>();
        ArrayList<Post> postList;

        public recyler_adapter_feed(ArrayList<Post> postList) {
            this.postList = postList;
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_content,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Fragment_feeds.recyler_adapter_feed.MyViewHolder holder, final int position) {


            holder.postText.setText(postList.get(position).getInfo());

            if (postList.get(position).getIsRegistrationAllowed().equals("yes"))
            {
                // One click registration
                holder.oneclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // write to firebase


                        FirebaseDatabase.getInstance().getReference().child("feed").child(postList.get(position).getPostKey()).child("registrations").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    String email = snapshot.getValue(String.class);
                                    registerdEmails.add(email.trim());

                                }
                                if (!registerdEmails.contains(mAuth.getCurrentUser().getEmail().trim()))
                                {
                                    databaseReference.child(postList.get(position).getPostKey()).child("registrations").push().setValue(mAuth.getCurrentUser().getEmail());
                                    Toast.makeText(getContext(), "Registered", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(getContext(), "Already Registered", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });



                    }
                });
            }
            else
            {
                holder.oneclick.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView postText;
            Button oneclick;

            public MyViewHolder(View itemView) {
                super(itemView);
                postText  = (TextView) itemView.findViewById(R.id.post_text);
                oneclick = (Button) itemView.findViewById(R.id.oneclick);

            }
        }
    }


}
