package com.saramambiches.datingapp.Chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saramambiches.datingapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {
    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

//    public MatchesAdapter(List<MatchesObject> dataSetMatches, Messages messages) {
//    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View LayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null  , false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(LayoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, @SuppressLint("RecyclerView") int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        holder.oppoMessage.setText(chatList.get(position).getMessage());
        if(chatList.get(position).getCurrentUser()){
            holder.oppoContainer.setVisibility(View.GONE);
            holder.mContainer.setVisibility(View.VISIBLE);
        }else{
            holder.oppoContainer.setVisibility(View.VISIBLE);
            holder.mContainer.setVisibility(View.GONE);
        }

        if(chatList.get(position).getTypeMessage().equals("2")){
            holder.mMessageImage.setVisibility(View.VISIBLE);
            holder.oppoMessageImage.setVisibility(View.VISIBLE);
            holder.mMessage.setVisibility(View.GONE);
            holder.oppoMessage.setVisibility(View.GONE);
            Glide.with(context).load(chatList.get(position).getMessageImage()).into(holder.mMessageImage);
            Glide.with(context).load(chatList.get(position).getMessageImage()).into(holder.oppoMessageImage);
        }else if(chatList.get(position).getTypeMessage().equals("1")){
            holder.mMessageImage.setVisibility(View.GONE);
            holder.oppoMessageImage.setVisibility(View.GONE);
            holder.mMessage.setVisibility(View.VISIBLE);
            holder.oppoMessage.setVisibility(View.VISIBLE);
        }


        holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog builder = new AlertDialog.Builder(context)
                        .setTitle("¿Deseas eliminar este mensaje?")
                        .setPositiveButton("SI", (dialog, which) -> {
                            //Eliminamos de la base de datos
                            DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatList.get(position).getIdChat()).child(chatList.get(position).getChatKey());

                            Map<String, Object> map = new HashMap<>();
                            map.put("text", "Este mensaje ha sido eliminado");


                            chatReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        chatReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context, "Mensaje eliminado", Toast.LENGTH_SHORT).show();
                                                    chatReference.keepSynced(true);
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {

                                }
                            });

                            chatReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Mensaje eliminado", Toast.LENGTH_SHORT).show();
                                    holder.mContainer.setBackgroundColor(Color.parseColor("#00000000"));
                                }
                            });
                            /*
                            chatReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Mensaje eliminado", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "Error al eliminar el mensaje", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                             */


                            /*
                            chatReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    chatList.remove(position);
                                    notifyDataSetChanged();
                                    holder.mContainer.setBackgroundColor(Color.parseColor("#00000000"));
                                }
                            });

                             */
                }).setNegativeButton("NO", (dialog, which) -> {
                    holder.mContainer.setBackgroundColor(Color.parseColor("#00000000"));
                    dialog.dismiss();
                }).create();
                builder.show();
                if(builder.getCurrentFocus() == null){
                    holder.mContainer.setBackgroundColor(Color.parseColor("#332575e7"));
                }else {
                    holder.mContainer.setBackgroundColor(Color.parseColor("#00000000"));
                }

                return true;
            }
        });



    }


    @Override
    public int getItemCount() {
        return this.chatList.size();
    }

}
