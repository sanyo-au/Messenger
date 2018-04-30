package com.example.sanyo.group24_inclass13;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ComposeMessageActivity extends AppCompatActivity {
    EditText eTMessage, editTextTo;
    ArrayList<User> users;
    User user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,database;
    private DatabaseReference emailRef;
    Email email;
    String sentToId;
    String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Compose Message");
        editTextTo = findViewById(R.id.editTextRecipient) ;
        editTextTo.setEnabled(false);
        editTextTo.setFocusable(false);
        eTMessage = findViewById(R.id.editTextMessage);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        mDatabase = database.child("users");
        users = new ArrayList<>();
        emailRef = database.child("emails");
        username = getIntent().getStringExtra("USERNAME");
        Log.d("demo", "onCreate: "+username);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("demo", "onClick: "+dataSnapshot.getValue());
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);

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
        sentToId = getIntent().getStringExtra("SENDERID");
        boolean fromRead = getIntent().getIntExtra("FROM",1) == 0 ? true:false;
            if(fromRead){
               String sender = getIntent().getStringExtra("SENDERNAME");
               editTextTo.setText(sender);
               sentToId = getIntent().getStringExtra("SENDERID");
           }

         
        findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String sentTo = editTextTo.getText().toString();
                if(sentTo.length() > 0) {
                    DatabaseReference addEmail = emailRef.child(sentToId);
                    Email email = new Email();
                    email.id = addEmail.push().getKey();
                    email.sender = username;
                    email.senderID = mAuth.getUid();
                    email.isRead = "false";
                    email.message = eTMessage.getText().toString();
                    DateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm a");
                    Date dateobj = new Date();
                    System.out.println(df.format(dateobj));
                    email.date = df.format(dateobj);
                    Log.d("demo","Date:"+email.date);
                    Log.d("demo", "onClick: " + email.sender);
                    addEmail.child(email.id).setValue(email);
                    Toast.makeText(ComposeMessageActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ComposeMessageActivity.this, InboxActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(ComposeMessageActivity.this, "No recipient selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.imageButtonUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "onClick: "+users.size());
                final String userArray[] = new String[users.size()];
                for (int i = 0; i <userArray.length ; i++) {
                        userArray[i] = users.get(i).name;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(ComposeMessageActivity.this);
                builder.setTitle("Contacts");
                builder.setItems(userArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           editTextTo.setText(userArray[i]);
                           sentToId = users.get(i).id;
                    }
                });
                builder.create().show();

            }
        });
        
    }
}
