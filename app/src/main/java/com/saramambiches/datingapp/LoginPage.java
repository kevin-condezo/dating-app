package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {

    Button btloginf;
    //FloatingActionButton btFB, btGOOGLE;
    TextView btredirectR;

    private TextInputEditText r_email, r_password;
    private TextInputLayout layout_email, layout_pass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent i = new Intent(LoginPage.this, PrincipalPage.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        };

        btloginf = (Button) findViewById(R.id.bt_login_f);
        btredirectR = (TextView) findViewById(R.id.text_redirect_r);
        //btFB = (FloatingActionButton) findViewById(R.id.bt_fb);
        //btGOOGLE = (FloatingActionButton) findViewById(R.id.bt_google);

        //Verificando Informacion
        r_email=(TextInputEditText) findViewById(R.id.email);
        r_password=(TextInputEditText) findViewById(R.id.password);
        layout_email=(TextInputLayout) findViewById(R.id.emailLayout);
        layout_pass=(TextInputLayout) findViewById(R.id.passwordLayout);


        //Boton Login
        btloginf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (validar()) {
                        final String email = r_email.getText().toString();
                        final String password = r_password.getText().toString();
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginPage.this, "Sign in Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }catch (Exception e){
                    
                }
            }
        });
        //Boton redirect Register
        btredirectR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent i = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(i);
                finish();
            }
        });
        /* Botones social red
        btFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this,"Aqui redirecciona a Facebook",Toast.LENGTH_LONG).show();
            }
        });
        btGOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this,"Aqui redirecciona a google",Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    //Validez de campos
    public boolean validar() {
        boolean retorno = true;
        String email = r_email.getText().toString();
        String pass = r_password.getText().toString();
        if (email.isEmpty()) {
            layout_email.setError("Complete el campo");
            retorno = false;
        }
        if (pass.isEmpty()) {
            layout_pass.setError("Complete el campo");
            retorno = false;
        }
        return retorno;
    }

    public void clickEmail(View v){
        layout_email.setError(null);

    }
    public void clickPass(View v){
        layout_pass.setError(null);
    }

    //Redireccionamiento de pagina
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginPage.this, RegisterAndLogin.class);
        startActivity(i);
        finish();
    }
    //Nose
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