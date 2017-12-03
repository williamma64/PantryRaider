package com.example.stevetran.pantryraider.Login;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacterEnums;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stevetran.pantryraider.Home.HomeActivity;
import com.example.stevetran.pantryraider.R;
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


/**
 * Created by rongfalu on 11/21/17.
 */

class User {
    public String uid;
    public int preference;
    public int allergy;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, int preference, int allergy) {
        this.uid = uid;
        this.preference = preference;
        this.allergy = allergy;
    }

}


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

    }



    public void createPressed(View view) {
        final Intent goToHome = new Intent(this, HomeActivity.class);
        EditText emailView = (EditText) findViewById(R.id.input_email);
        String email = emailView.getText().toString();
        EditText passwordView = (EditText) findViewById(R.id.input_password);
        String password = passwordView.getText().toString();
        EditText usernameView = (EditText) findViewById(R.id.input_name);
        String useruname = usernameView.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        final DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();

                        // Attach a listener to read the data at our posts reference
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                System.out.println(user.uid);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });

                        // store info to fire base
                        User user = new User(mAuth.getCurrentUser().getUid(), 0, 0);

                        mDatabase.child("Account").child(mAuth.getCurrentUser().getUid()).setValue(user);

                        // ...
                    }
                });
    }



    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}
