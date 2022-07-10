package com.saramambiches.datingapp;

import static www.sanju.motiontoast.MotionToast.GRAVITY_BOTTOM;
import static www.sanju.motiontoast.MotionToast.SHORT_DURATION;
import static www.sanju.motiontoast.MotionToast.TOAST_ERROR;
import static www.sanju.motiontoast.MotionToast.TOAST_SUCCESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import www.sanju.motiontoast.MotionToast;

public class RegisterPage extends AppCompatActivity {
    Button btregisterf;
    //FloatingActionButton btFB, btGOOGLE;
    TextView btredirectl;

    //TextInputLayout menusex;
    //AutoCompleteTextView dropitemsex;


    private TextInputEditText r_email, r_password, r_name;
    private TextInputLayout layout_name, layout_email, layout_pass;
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
        //menusex= (TextInputLayout) findViewById(R.id.menu_sex);
        //dropitemsex = (AutoCompleteTextView) findViewById(R.id.drop_item_sex);
        //Recolectando informacion
        r_email = (TextInputEditText) findViewById(R.id.email);
        r_password = (TextInputEditText) findViewById(R.id.password);
        r_name = (TextInputEditText)  findViewById(R.id.name);
        r_RadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        layout_name=(TextInputLayout) findViewById(R.id.nameLayout);
        layout_email=(TextInputLayout) findViewById(R.id.emailLayout);
        layout_pass=(TextInputLayout) findViewById(R.id.passwordLayout);


        //String [] itemsSex={"Hombre", "Mujer"};
        //ArrayAdapter<String> itemsAdapter= new ArrayAdapter<>(RegisterPage.this , R.layout.sex_items, itemsSex);
        //dropitemsex.setAdapter(itemsAdapter);


        btregisterf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(validar()){
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
                                    MotionToast.Companion.createColorToast(RegisterPage.this,"Error al registrarte",
                                            TOAST_ERROR,
                                            GRAVITY_BOTTOM,
                                            SHORT_DURATION,
                                            ResourcesCompat.getFont(RegisterPage.this,R.font.quicksand_bold));
                                } else {
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("name");
                                    currentUserDb.setValue(name);
                                }
                                MotionToast.Companion.createColorToast(RegisterPage.this,"Registrado Correctamente",
                                        TOAST_SUCCESS,
                                        GRAVITY_BOTTOM,
                                        SHORT_DURATION,
                                        ResourcesCompat.getFont(RegisterPage.this,R.font.quicksand_bold));
                            }
                        });
                    }
                }catch (Exception e){
                    MotionToast.Companion.createColorToast(RegisterPage.this,"Error al registrarte",
                            TOAST_ERROR,
                            GRAVITY_BOTTOM,
                            SHORT_DURATION,
                            ResourcesCompat.getFont(RegisterPage.this,R.font.quicksand_bold));
                }


            }
        });

        r_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_name.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        r_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_email.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        r_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_pass.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    //Validez de campos
    public boolean validar() {
        boolean retorno = true;
        String name = r_name.getText().toString();
        String email = r_email.getText().toString();
        String pass = r_password.getText().toString();
        if (name.isEmpty()) {
            layout_name.setError("Complete el campo");
            retorno = false;
        }
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