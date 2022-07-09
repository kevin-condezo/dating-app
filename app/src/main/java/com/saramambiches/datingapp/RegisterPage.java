package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {
    Button btregisterf;
    //FloatingActionButton btFB, btGOOGLE;
    TextView btredirectl;

    private TextInputEditText r_email, r_password, r_name;
    private RadioGroup r_RadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!= null) {
                    Intent i = new Intent(RegisterPage.this, PrincipalPage.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        };

        setContentView(R.layout.activity_register_page);
        btregisterf = (Button) findViewById(R.id.bt_register_f);
        btredirectl =(TextView) findViewById(R.id.text_redirect_l);
        //btFB = (FloatingActionButton) findViewById(R.id.bt_fb);
        //btGOOGLE = (FloatingActionButton) findViewById(R.id.bt_google);

        //Recolectando informacion
        r_email = (TextInputEditText) findViewById(R.id.email);
        r_password = (TextInputEditText) findViewById(R.id.password);
        r_name = (TextInputEditText)  findViewById(R.id.name);
        r_RadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        btregisterf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = r_RadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectId);
                if (radioButton.getText() == null) {
                    return;
                }

                final String email = r_email.getText().toString();
                final String password = r_password.getText().toString();
                final String name = r_name.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegisterPage.this, "Sign up Error", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("name");
                            currentUserDb.setValue(name);
                        }
                    }
                });
            }
        });
        btredirectl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent i = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        });
        /*
        btFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterPage.this,"Aqui redirecciona a Facebook",Toast.LENGTH_LONG).show();
            }
        });
        btGOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterPage.this,"Aqui redirecciona a google",Toast.LENGTH_LONG).show();
            }
        });
        */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterPage.this, RegisterAndLogin.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}