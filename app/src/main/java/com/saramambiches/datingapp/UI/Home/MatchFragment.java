package com.saramambiches.datingapp.UI.Home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saramambiches.datingapp.Cards;
import com.saramambiches.datingapp.DirectionCustom;
import com.saramambiches.datingapp.R;
import com.saramambiches.datingapp.UI.Messages.MessagesFragment;
import com.saramambiches.datingapp.UI.Profile.ProfileFragment;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchFragment extends Fragment {

    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private FloatingActionButton bt_back, bt_skip, bt_like, bt_super_like;
    private ImageView bt_sms;
    private CircleImageView mProfileImageTop;
    private String userSex;
    private String oppositeUserSex;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;
    List<ItemModel> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_match, container, false);

        mAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        items = new ArrayList<>();
        currentUId= mAuth.getCurrentUser().getUid();
        checkUserSex();

        init(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.toolbar_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                MessagesFragment fragment = new MessagesFragment();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        getView().findViewById(R.id.toolbar_profiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ProfileFragment fragment = new ProfileFragment();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

    }

    private void init(@NonNull View root) {

        bt_skip = root.findViewById(R.id.bt_skip);
        bt_like = root.findViewById(R.id.bt_like);
        bt_super_like = root.findViewById(R.id.bt_star);
        bt_back = root.findViewById(R.id.bt_back);
        CardStackView cardStackView = root.findViewById(R.id.card_stack_view);

        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    //Toast.makeText(getContext(), "Direction Right", Toast.LENGTH_SHORT).show();
                    ItemModel item = items.get(manager.getTopPosition()-1);
                    String userId = item.getUserId();
                    usersDb.child(userId).child("connections").child("like").child(currentUId).setValue(true);
                }
                if (direction == Direction.Left){
                    //Toast.makeText(getContext(), "Direction Left", Toast.LENGTH_SHORT).show();
                    ItemModel item = items.get(manager.getTopPosition()-1);
                    String userId = item.getUserId();
                    usersDb.child(userId).child("connections").child("skip").child(currentUId).setValue(true);

                }
                if (direction == Direction.Top){
                    //Toast.makeText(getContext(), "Direction Top", Toast.LENGTH_SHORT).show();
                    ItemModel item = items.get(manager.getTopPosition()-1);
                    String userId = item.getUserId();
                    usersDb.child(userId).child("connections").child("superlike").child(currentUId).setValue(true);
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                //Toast.makeText(getContext(), "onCardRewound: " + manager.getTopPosition(), Toast.LENGTH_SHORT).show();
                ItemModel item = items.get(manager.getTopPosition());
                String userId = item.getUserId();
                usersDb.child(userId).child("connections").child("skip").child(currentUId).removeValue();
                usersDb.child(userId).child("connections").child("like").child(currentUId).removeValue();
                usersDb.child(userId).child("connections").child("superlike").child(currentUId).removeValue();

            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());
            }
        });


        manager.setStackFrom(StackFrom.Bottom);  //Estilo de cartas
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(DirectionCustom.CUSTOM);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimarFab(bt_skip);
                manager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build());
                cardStackView.swipe();
            }
        });

        bt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimarFab(bt_like);
                manager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build());
                cardStackView.swipe();
            }
        });

        bt_super_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimarFab(bt_super_like);
                manager.setSwipeAnimationSetting(new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Top)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build());
                cardStackView.swipe();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimarFab(bt_back);
                manager.setRewindAnimationSetting(new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build());
                cardStackView.rewind();

            }
        });

    }

    private void AnimarFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                fab.animate().scaleX(1f).scaleY(1f);
            }
        });
    }

    private void paginate() {
        List<ItemModel> oldItem = adapter.getItems();
        List<ItemModel> newItem = new ArrayList<>(addList());

        CardStackCallback callback = new CardStackCallback(oldItem, newItem);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(newItem);
        hasil.dispatchUpdatesTo(adapter);
    }


    public void checkUserSex(){ //Método que comprueba el sexo del usuario en la base de datos
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("sex").getValue() != null) {
                        userSex = snapshot.child("sex").getValue().toString();
                        switch (userSex) {
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        //getOppositeUserSex();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private List<ItemModel> addList() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.child("sex").getValue() !=null) {
                    if (snapshot.exists() && !snapshot.child("connections").child("skip").hasChild(currentUId) && !snapshot.child("connections").child("superlike").hasChild(currentUId) && !snapshot.child("connections").child("like").hasChild(currentUId) && snapshot.child("sex").getValue().toString().equals(oppositeUserSex) && !snapshot.getKey().equals(currentUId)) {
                        String image = "default";
                        String age = "";
                        String university="";
                        if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                            image = snapshot.child("profileImageUrl").getValue().toString();

                        }
                        if (!(snapshot.child("age").getValue() == null)) {
                            age = snapshot.child("age").getValue().toString();
                        }
                        if (!(snapshot.child("university").getValue() == null)) {
                            university = snapshot.child("university").getValue().toString();
                        }

                        ItemModel itemModel = new ItemModel(snapshot.getKey(),image, snapshot.child("name").getValue().toString(), age,university);
                        items.add(itemModel);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return items;
    }

}