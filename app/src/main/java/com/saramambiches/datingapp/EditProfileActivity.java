package com.saramambiches.datingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText mNameField;
    private Button mConfirm;
    private CircleImageView mProfileImage;
    private DatabaseReference mUserDatabase;
    private String userId, name, profileImageUrl, userSex;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userSex = getIntent().getExtras().getString("sex"); // Se obtiene el dato de la instancia anterior

        mNameField = findViewById(R.id.name);
        mNameField.addTextChangedListener(editTextWatcher);
        mProfileImage = findViewById(R.id.circle_profile_image);
        mConfirm = findViewById(R.id.btn_guardar);
        TextView mBack = findViewById(R.id.regresar);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(userId);

        getUserInfo();

        mProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), Profile.class));
                Intent i = new Intent(EditProfileActivity.this, Profile.class);
                i.putExtra("sex", userSex);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    // Para activar boton Guardar al hacer cambios en el nombre
    private final TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String changedName = mNameField.getText().toString().trim();
            mConfirm.setEnabled(!changedName.equals(name));
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    // Se obtienen los datos del usuario: nombre e imagen de perfil
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = Objects.requireNonNull(map.get("name")).toString();
                        mNameField.setText(name);
                    }
                    Glide.with(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = Objects.requireNonNull(map.get("profileImageUrl")).toString();
                        //Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        if ("default".equals(profileImageUrl)) {
                            Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                        } else {
                            Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    // Se guardan los cambios hechos
    private void saveUserInformation() {
        name = mNameField.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("name", name);
        mUserDatabase.updateChildren(userInfo);
        if(resultUri != null){ // Si se agrega una imagen de perfil
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(e -> finish());
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            Map userInfo1 = new HashMap();
                            userInfo1.put("profileImageUrl", downloadUrl.toString());
                            mUserDatabase.updateChildren(userInfo1);
                        }
                    });
                }
            });
        }
        /*else{
            finish();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
            mConfirm.setEnabled(true);
        }
    }
}