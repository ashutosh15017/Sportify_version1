package com.example.ishmeetkaur.sportify_version1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;


public class C_feeds_fragment extends Fragment {

    RecyclerView mRecyclerView;
    ArrayList<Post> postList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    recyler_adapter_feed_coord adapter;
    FirebaseAuth mAuth;
    FloatingActionButton fabAddPost;

    public C_feeds_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_c_feeds_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler_view_coord);
        fabAddPost = (FloatingActionButton) rootView.findViewById(R.id.fabAddPost);
        fabAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add post
                Intent intent = new Intent(getActivity(),AddPost.class);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        postList = new ArrayList<>();

        adapter = new recyler_adapter_feed_coord(postList);
        firebaseDatabase = FirebaseDatabase.getInstance();
        getFirebaseData();
        return rootView;
    }

    void getFirebaseData()
    {

        databaseReference = firebaseDatabase.getReference("feed");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                post.setPostKey(dataSnapshot.getKey());
                if (post.getByWhom().equals(mAuth.getCurrentUser().getEmail()))
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

    public class recyler_adapter_feed_coord extends RecyclerView.Adapter<C_feeds_fragment.recyler_adapter_feed_coord.MyViewHolder>
    {

        ArrayList<Post> postList;
        List<String> registerdEmails = new ArrayList<>();
        List<User> registeredUsers = new ArrayList<>();
        ArrayList<String> reminderEmails = new ArrayList<>();

        public recyler_adapter_feed_coord(ArrayList<Post> postList) {
            this.postList = postList;
        }



        @Override
        public C_feeds_fragment.recyler_adapter_feed_coord.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.coord_feed_recycler_content,parent,false);
            return new C_feeds_fragment.recyler_adapter_feed_coord.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final C_feeds_fragment.recyler_adapter_feed_coord.MyViewHolder holder, final int position) {


            holder.postText.setText(postList.get(position).getInfo());
            holder.removePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // remove post
                    removePost(position);
                }
            });

            holder.send_reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendReminder(position);
                }
            });


            if (postList.get(position).getIsRegistrationAllowed().equals("yes"))
            {
                // check the registrations from firebase, grab user info and make a sheet
                holder.registrations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerdEmails.clear();
                        registeredUsers.clear();
                        grabRegistrations(position);
                    }
                });


            }
            else
            {
                holder.registrations.setVisibility(View.GONE);
            }


        }

        public void sendReminder (final int position)
        {
            // send reminder to everyone subscribed to the sport

            final String sport = postList.get(position).getSport();
            reminderEmails.clear();
            FirebaseDatabase.getInstance().getReference().child("student").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        User user = snapshot.child("information").getValue(User.class);
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> sportList = snapshot.child("sports").getValue(t);
                        if (sportList.contains(sport))
                            reminderEmails.add(user.getUserEmail());
                    }


                    Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    email.putExtra(Intent.EXTRA_SUBJECT,"Reminder");
                    email.setType("text/plain");
                    email.putExtra(Intent.EXTRA_TEXT, "Reminder for: \n" + postList.get(position).getInfo());
                    String [] emailList = reminderEmails.toArray(new String[0]);
                    email.putExtra(Intent.EXTRA_EMAIL, emailList);
                    startActivity(Intent.createChooser(email, "Send email..."));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }

        public void removePost(int position)
        {
            databaseReference = firebaseDatabase.getReference("feed").child(postList.get(position).getPostKey());
            databaseReference.removeValue();
            postList.remove(position);
            Toast.makeText(getContext(), "Removed post", Toast.LENGTH_SHORT).show();
        }

        public void grabRegistrations(int position)
        {

            FirebaseDatabase.getInstance().getReference().child("feed").child(postList.get(position).getPostKey()).child("registrations").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String email = snapshot.getValue(String.class);
                        registerdEmails.add(email.trim());
                    }
                    grabUsers();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }

        public void grabUsers()
        {
            FirebaseDatabase.getInstance().getReference().child("student").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        User user = snapshot.child("information").getValue(User.class);
                        if (registerdEmails.contains(user.getUserEmail()))
                            registeredUsers.add(user);
                    }

                    makeAndOpenSheet();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }

        public void makeAndOpenSheet()
        {
            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "registrations.csv";
            String filePath = baseDir + File.separator + fileName;
            File f = new File(filePath);
            CSVWriter writer = null;

            if (f.exists() && !f.isDirectory())
            {
                try {
                    FileWriter mFileWriter = new FileWriter(filePath , false);
                    writer = new CSVWriter(mFileWriter);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                try {
                    writer = new CSVWriter(new FileWriter(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            String registrationsInfo;
            registrationsInfo = "Name"+","+"Email"+","+"Number"+","+"Gender"+"\n";
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Name","Email","Number","Gender"});
            for (User user : registeredUsers)
            {
               data.add(new String[]{user.getUserName(),user.getUserEmail(),user.getUserNumber(),user.getUserGender()});
            }
            try {
                Log.d("REGISTAG", "user: "+registeredUsers.toString());
                Log.d("REGISTAG", "email: "+registerdEmails.toString());
                writer.writeAll(data);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent i = new Intent();
            i.setAction(android.content.Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(f), "text/csv");
            startActivity(i);

        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView postText;
            Button registrations;
            Button removePost;
            Button send_reminder;

            public MyViewHolder(View itemView) {
                super(itemView);
                postText  = (TextView) itemView.findViewById(R.id.post_text);
                registrations = (Button) itemView.findViewById(R.id.registrations);
                removePost = (Button) itemView.findViewById(R.id.removePost);
                send_reminder = (Button) itemView.findViewById(R.id.send_reminder);
            }
        }
    }


}
