package com.example.sanyo.group24_inclass13;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    ArrayList<Email> emails;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Inbox");


        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String userId = mAuth.getUid();
        emails = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user1 = dataSnapshot.getValue(User.class);
                    if(user1.id.equals(mAuth.getUid()))    {

                        username = user1.name;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new InboxAdapter(emails,username);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    });
               

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
        }) ;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("emails").child(userId);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Email email = dataSnapshot.getValue(Email.class);
                email.id = dataSnapshot.getKey();
                emails.add(0,email);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("demo","Email :" +emails.toString());

                        mAdapter.notifyItemRangeInserted(0,emails.size());
                        mAdapter.notifyItemChanged(emails.size());
                    }
                });

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_compose:
                Intent intent = new Intent(this, ComposeMessageActivity.class );
                intent.putExtra("FROM",1);
                Log.d("demo", "onOptionsItemSelected: "+username);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_logout:
                mAuth.signOut();
               Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}