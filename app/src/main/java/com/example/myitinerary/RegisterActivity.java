package com.example.myitinerary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerSubmitBtn;
    private TextView goToLoginBtn;
    private EditText editTextTextPersonName, editTextEmail, editTextPassword, editTextPasswordConfirm;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        goToLoginBtn = (TextView) findViewById(R.id.goToLoginBtn);
        goToLoginBtn.setOnClickListener(this);

        registerSubmitBtn = (Button) findViewById(R.id.registerSubmitBtn);
        registerSubmitBtn.setOnClickListener(this);

        editTextTextPersonName = (EditText) findViewById(R.id.editTextPersonName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = (EditText) findViewById(R.id.editTextPasswordConfirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.goToLoginBtn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerSubmitBtn:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String fullName = editTextTextPersonName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password_c = editTextPasswordConfirm.getText().toString().trim();

        // form validation
        if(fullName.isEmpty()) {
            editTextTextPersonName.setError("Please enter a name");
            editTextTextPersonName.requestFocus();
            return;
        }
        if(!fullName.contains(" "))
        {
            editTextTextPersonName.setError("Please enter a first and last name");
            editTextTextPersonName.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTextEmail.setError("Please enter an email");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty() || password_c.isEmpty()) {
            editTextPassword.setError("Please enter a password and confirm");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 8)
        {
            editTextPassword.setError("Password must be at least 8 characters");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.matches(password_c)) {
            editTextPasswordConfirm.setError("Passwords do not match");
            editTextPasswordConfirm.requestFocus();
            return;
        }

        //create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //location and bio can be set later in settings
                    User user = new User(fullName, email, "", "");
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                Toast.makeText(RegisterActivity.this,
                                        "Email verification sent", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("itineraries")
                            .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "Registration successful, you can log in now", Toast.LENGTH_LONG).show();
                               MainActivity.createItineraryEntry(0, 0, "example", "Pittsburgh", 0, 0, FirebaseAuth.getInstance().getCurrentUser().getUid(), db);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}