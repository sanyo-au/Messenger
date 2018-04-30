package com.example.sanyo.group24_inclass13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
     private FirebaseAuth mAuth;
     EditText email, password;
     User user;
     private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Messenger");

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "OnClick");
                if(email.getText().length() > 0 && password.getText().length() > 0)     {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        Log.d("demo", "Else");
                                        ValueEventListener postListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                    if(childSnapshot.getKey().equals(firebaseUser.getUid()))
                                                        user = childSnapshot.getValue(User.class);
                                                }
                                                Log.d("demo", user.toString());
                                                Intent intent = new Intent(LoginActivity.this, InboxActivity.class);
                                                intent.putExtra("USER",user);
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        };

                                        mDatabase.addListenerForSingleValueEvent(postListener);

                                    }
                                }
                            });
                }
                else{
                    if(email.getText().length() < 1){
                        email.setError("Enter email");
                    }
                    if(password.getText().length() < 1){
                        password.setError("Enter password");
                    }
                }
            }
        });

        findViewById(R.id.buttonNewUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NewUserActivity.class );
                startActivity(intent);
                finish();
            }
        });
    }
}
