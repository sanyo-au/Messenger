package com.example.sanyo.group24_inclass13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserActivity extends AppCompatActivity {

    EditText etFName, etLName, etPass, etEmail, etPass2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Messenger");

        etFName = findViewById(R.id.editTextFName);
        etLName = findViewById(R.id.editTextLName);
        etPass = findViewById(R.id.editTextPass);
        etPass2 = findViewById(R.id.editTextPass2);
        etEmail = findViewById(R.id.editTextEmail);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = etFName.getText().toString();
                String lName = etLName.getText().toString();
                String email = etEmail.getText().toString();
                String pass1 = etPass.getText().toString();
                String pass2 = etPass2.getText().toString();
                if (fName == null || fName.isEmpty()) {
                    etFName.setError("First Name Required");
                } else if (lName == null || lName.isEmpty()) {
                    etLName.setError("Last Name Required");
                } else if (email == null || email.toString().isEmpty()) {
                    etEmail.setError("Email Required");
                } else if (pass1 == null || pass1.isEmpty()) {
                    etPass.setError("Password Required");
                } else if (pass2 == null || pass2.isEmpty()) {
                    etPass2.setError("Password Required");
                } else if (!pass1.equals(pass2)) {
                    Toast.makeText(NewUserActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass2.getText().toString())
                            .addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(NewUserActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        task.getResult().getUser();
                                        user = createNewUser(task.getResult().getUser(), etFName.getText().toString(), etLName.getText().toString());
                                        Intent intent = new Intent(NewUserActivity.this, InboxActivity.class);
                                        intent.putExtra("USER", user);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                }
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private User createNewUser(FirebaseUser userFromRegistration, String fName, String lName) {

        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();
        String name = fName +" " +lName;
        User user = new User(email, userId, name);
        mDatabase.child("users").child(userId).setValue(user);
        return user;
    }
}
