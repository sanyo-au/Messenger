package com.example.sanyo.group24_inclass13;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Intent intent;
    private DatabaseReference mDatabase;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        if(mAuth.getCurrentUser() == null){
            intent = new Intent(this, LoginActivity.class) ;
            startActivity(intent);
            finish();
        }else{
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if(childSnapshot.getKey().equals(mAuth.getUid()))
                            user = childSnapshot.getValue(User.class);
                    }
                    Intent intent = new Intent(MainActivity.this, InboxActivity.class);
                    intent.putExtra("USER",user);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mDatabase.addListenerForSingleValueEvent(postListener);
        }

    }
}
