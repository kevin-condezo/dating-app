package com.saramambiches.datingapp;

import static www.sanju.motiontoast.MotionToast.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.text.Editable;
import android.text.TextWatcher;
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

import www.sanju.motiontoast.MotionToast;

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
                final String email = r_email.getText().toString();
                final String password = r_password.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            MotionToast.Companion.createColorToast(LoginPage.this,"Error al loguearse",
                                    TOAST_ERROR,
                                    GRAVITY_BOTTOM,
                                    SHORT_DURATION,
                                    ResourcesCompat.getFont(LoginPage.this,R.font.quicksand_bold));

                        }
                        MotionToast.Companion.createColorToast(LoginPage.this,"Logueado Correctamente",
                                TOAST_SUCCESS,
                                GRAVITY_BOTTOM,
                                SHORT_DURATION,
                                ResourcesCompat.getFont(LoginPage.this,R.font.quicksand_bold));
                    }
                });
            }
        });



        r_email.addTextChangedListener(loginTextWatcher);
        r_password.addTextChangedListener(loginTextWatcher);

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

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = r_email.getText().toString().trim();
            String pass = r_password.getText().toString().trim();

            btloginf.setEnabled(!email.isEmpty() && !pass.isEmpty());
            if(validar()){
                btloginf.setBackground(getResources().getDrawable(R.drawable.button_color));
                btloginf.setTextColor(Color.WHITE);
            }else{
                btloginf.setBackground(getResources().getDrawable(R.drawable.button_color_inactive));
                btloginf.setTextColor(getResources().getColor(R.color.inactive));
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    //Validez de campos
    public boolean validar() {
        boolean retorno = true;
        String email = r_email.getText().toString();
        String pass = r_password.getText().toString();
        if (email.isEmpty()) {
            retorno = false;
        }
        if (pass.isEmpty()) {
            retorno = false;
        }
        return retorno;
    }

    /*Redireccionamiento de pagina
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginPage.this, RegisterAndLogin.class);
        startActivity(i);
        finish();
    }*/
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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}