package com.example.stevetran.pantryraider.Setting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stevetran.pantryraider.Login.LoginActivity;
import com.example.stevetran.pantryraider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // get current logged in user's info
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String email = mAuth.getCurrentUser().getEmail();

        // text initialize
        final TextView oldpassword = findViewById(R.id.CPoldpassword);
        final TextView newpassword = findViewById(R.id.CPnewpassword);
        final TextView retypepassword = findViewById(R.id.CPretypepassword);

        // button initialize
        final Button btnChangePassword = (Button) findViewById(R.id.BtnChangePassword);
        final Button btnClear = (Button) findViewById(R.id.Clear);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                ///TextView email = (TextView) findViewById(R.id.CPemail);

                if (oldpassword.getText().toString().isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter the Old Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newpassword.getText().toString().isEmpty() || retypepassword.getText().toString().isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter and Verify the New Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newpassword.getText().toString().equals(retypepassword.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Password Doesn't Match",
                            Toast.LENGTH_SHORT).show();
                    newpassword.setText("");
                    retypepassword.setText("");
                    return;
                }

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldpassword.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(retypepassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("CP", "Password updated");
                                                Toast.makeText(ChangePasswordActivity.this, "Password Updated",
                                                        Toast.LENGTH_SHORT).show();
                                                // log out current user
                                                mAuth.signOut();
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            } else {
                                                Log.d("CP", "Error password not updated");
                                                Toast.makeText(ChangePasswordActivity.this, "Error password not updated",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Log.d("CP", "Error auth failed");
                                    Toast.makeText(ChangePasswordActivity.this, "Incorrect Old Password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oldpassword.setText("");
                newpassword.setText("");
                retypepassword.setText("");
            }
        });
    }
}
