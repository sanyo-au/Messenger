package com.example.sanyo.group24_inclass13;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadMessageActivity extends AppCompatActivity {
    
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private  Email email;
    TextView tVFrom, tVMessage;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Read Message");
        email = (Email)getIntent().getSerializableExtra("EMAIL");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("emails")
                .child(mAuth.getUid()).child(email.id).child("isRead");
        mDatabase.setValue("true");
        userName = getIntent().getStringExtra("USERNAME");
        Log.d("demo", "Inside ReadActivity"+userName);
        tVFrom = findViewById(R.id.textViewFrom);
        tVMessage = findViewById(R.id.textViewMessage);
        tVMessage.setMovementMethod(new ScrollingMovementMethod());
        tVFrom.setText("From: "+email.sender);
        tVMessage.setText(email.message);




        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, InboxActivity.class );
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                mDatabase = FirebaseDatabase.getInstance().getReference().child("emails")
                        .child(mAuth.getUid()).child(email.id);
                mDatabase.removeValue();
                Intent intent = new Intent(this, InboxActivity.class );
                startActivity(intent);
                finish();
                return true;

            case R.id.action_reply:

                intent = new Intent(this, ComposeMessageActivity.class);
                intent.putExtra("SENDERID", email.senderID);
                intent.putExtra("SENDERNAME", email.sender);
                intent.putExtra("FROM", 0);
                intent.putExtra("USERNAME",userName);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
