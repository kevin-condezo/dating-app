package com.saramambiches.datingapp.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saramambiches.datingapp.Matches.MatchesAdapter;
import com.saramambiches.datingapp.Matches.MatchesObject;
import com.saramambiches.datingapp.Messages;
import com.saramambiches.datingapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mSendEditText;
    private TextView bSendButton;

    private ImageView mBackButton, mGalleryButton, mCameraButton;

    private CircleImageView mProfileImage;
    private TextView mProfileName;

    private String currentUserId,matchId,chatId;

    DatabaseReference mDatabaseUser,mDatabaseChat;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId=getIntent().getExtras().getString("matchId");

        //Obtemos el id del match actual
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseChat= FirebaseDatabase.getInstance().getReference().child("Chat");

        mStorage=FirebaseStorage.getInstance();

        getChatId();
        getUserInfoMatch();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSms);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText= findViewById(R.id.message);
        bSendButton= findViewById(R.id.send_text);

        mBackButton= findViewById(R.id.back);
        mProfileImage= findViewById(R.id.profile_image_match);
        mProfileName= findViewById(R.id.profile_name_match);
        mGalleryButton= findViewById(R.id.send_gallery);
        mCameraButton= findViewById(R.id.send_camera);

        mSendEditText.addTextChangedListener(TextAdd);



        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        //Button Gallery
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryI = new Intent(Intent.ACTION_PICK);
                galleryI.setType("image/*");
                startActivityForResult(galleryI, 1);
            }
        });
        //Button Camera
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraI, 2);
            }
        });


        mChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        });
    }


    private TextWatcher TextAdd = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String text = mSendEditText.getText().toString().trim();
            if(!text.isEmpty()){
                bSendButton.setVisibility(View.VISIBLE);
                mGalleryButton.setVisibility(View.GONE);
            }else{
                bSendButton.setVisibility(View.GONE);
                mGalleryButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void sendMessage() {
        String sendMessageText= mSendEditText.getText().toString();

        if(! sendMessageText.isEmpty()){
            DatabaseReference newMessageDb= mDatabaseChat.push();

            Map newMessage=new HashMap();
            newMessage.put("createdByUser", currentUserId);
            newMessage.put("text", sendMessageText);

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);

    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId=snapshot.getValue().toString();
                    mDatabaseChat=mDatabaseChat.child(chatId);
                    getChatMessages();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //obtenemos datos del match
    private void getUserInfoMatch(){

        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId);
        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    String profileName = snapshot.child("name").getValue().toString();
                    if ("default".equals(profileImageUrl)) {
                        Glide.with(getApplication()).load("https://firebasestorage.googleapis.com/v0/b/date-app-e56a0.appspot.com/o/profileImages%2Fimage_profile.jpg?alt=media&token=f1c8e41a-bcf5-40de-adfd-195ce5ef7b0a").into(mProfileImage);

                    } else {
                        Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                    }

                    mProfileName.setText(profileName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //get value idChat



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Uri imageUri= data.getData();
            mStorageReference = mStorage.getReference().child("message_images").child(chatId);
            final StorageReference urlImage = mStorageReference.child(System.currentTimeMillis() + ".jpg");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] dataCamera = baos.toByteArray();
            UploadTask uploadTask = urlImage.putBytes(dataCamera);
            uploadTask.addOnFailureListener(e -> finish());
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Envia el mensaje con el url de la imagen
                    urlImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            DatabaseReference newMessageDb= mDatabaseChat.push();

                            Map newMessage=new HashMap();
                            newMessage.put("createdByUser",currentUserId);
                            newMessage.put("textImage", imageUrl);
                            newMessageDb.setValue(newMessage);

                        }
                    });
                }
            });
        }

        if(requestCode==2 && resultCode==RESULT_OK){

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataImage = baos.toByteArray();
            mStorageReference = mStorage.getReference().child("message_images").child(chatId);
            final StorageReference imageReference = mStorageReference.child(System.currentTimeMillis() + ".jpg");
            imageReference.putBytes(dataImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Envia el mensaje con el url de la imagen
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            DatabaseReference newMessageDb= mDatabaseChat.push();

                            Map newMessage=new HashMap();
                            newMessage.put("createdByUser",currentUserId);
                            newMessage.put("textImage", imageUrl);
                            newMessageDb.setValue(newMessage);

                        }
                    });
                }
            });

        }

    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot snapshot,  String s) {
                if(snapshot.exists()){
                    String message=null;
                    String messageImage=null;
                    String createdByUser= null;
                    String typeMessage= null;
                    String idChat= chatId;
                    String chatKey = snapshot.getKey();

                    if(snapshot.child("createdByUser").getValue()!=null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }


                    if(snapshot.child("createdByUser").getValue()!=null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if(snapshot.child("text").getValue()!=null){
                        message = snapshot.child("text").getValue().toString();
                        typeMessage ="1";
                    }else{
                        messageImage = snapshot.child("textImage").getValue().toString();
                        typeMessage ="2";
                    }

                    if(message!=null && createdByUser!=null || messageImage!=null && createdByUser!=null){
                        Boolean currentUserBoolean=false;
                        if(createdByUser.equals(currentUserId)){
                            currentUserBoolean=true;
                        }
                        ChatObject newMessage=new ChatObject(message,messageImage,typeMessage,currentUserBoolean,idChat,chatKey);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}