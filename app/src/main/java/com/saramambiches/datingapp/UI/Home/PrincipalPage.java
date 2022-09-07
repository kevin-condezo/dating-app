package com.saramambiches.datingapp.UI.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.saramambiches.datingapp.Cards;
import com.saramambiches.datingapp.CardsAdapter;
import com.saramambiches.datingapp.UI.Messages.Messages;
import com.saramambiches.datingapp.R;
import com.saramambiches.datingapp.UI.Messages.MessagesFragment;
import com.saramambiches.datingapp.UI.Profile.ProfileFragment;
import com.saramambiches.datingapp.VPadapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalPage extends AppCompatActivity {
    private CardsAdapter adaptadorItems;

    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;

    private FloatingActionButton bt_back, bt_skip, bt_like, bt_super_like, bt_skip_info, bt_like_info;
    BottomNavigationView bottomNavigationView;

    private Button layoutHide;
    private Button bt_smsRedirect;
    private LinearLayout layoutMatch;

    private CircleImageView myImage, matchImage;
    private String myImageUrl, matchImageUrl;

    String profileImageUrl;
    private CircleImageView mProfileImageTop;
    private String userId;

    int surfaceColors;

    private Cards cards_data[];

    RelativeLayout screenInfo;
    FloatingActionButton infoHide;
    ImageView infoImage;
    TextView infoName, infoAge, infoBio;

    List<Cards> colaItems;

    Animation expandAnimation,contractAnimation;
    //Card
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    //ViewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SplashScreen splashScreen= SplashScreen.installSplashScreen(this); //init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);

        //surfaceColors = SurfaceColors.SURFACE_2.getColor(this);


        //binding satusbar color

        //Fragment inflater
        MatchFragment matchFragment = new MatchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, matchFragment).commit();




        expandAnimation = AnimationUtils.loadAnimation(this, R.anim.expand);
        contractAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        init();

        mAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //hay otro currentUId

        infoHide = findViewById(R.id.infoHide);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId= mAuth.getCurrentUser().getUid();

        checkUserSex();

        colaItems = new ArrayList<>(); //Cola de items para el SwipeFlingView (Estructura de datos)

        //Manera en la que todos los datos se van a mostrar en el SwipeFlingView
        adaptadorItems = new CardsAdapter(this, R.layout.item, colaItems);

        //Contenedor de los datos de las cards
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(adaptadorItems);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() { //Se llama a este método cuando se desliza y se elimina el primer item de la cola
                Log.d("colaItems", "item eliminado");
                // se elimina (desencola) el primer elemento de la cola de items
                colaItems.remove(0);
                adaptadorItems.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) { //Se llama a este método cuando se desliza hacia la izquierda y guarda la actividad en la base de datos
                Cards object = (Cards) dataObject;
                String userId = object.getUserId();
                usersDb.child(userId).child("connections").child("skip").child(currentUId).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) { //Se llama a este método cuando se desliza hacia la derecha y guarda la actividad en la base de datos
                Cards object = (Cards) dataObject;
                String userId = object.getUserId();
                usersDb.child(userId).child("connections").child("like").child(currentUId).setValue(true);
                isConnectionMatch(userId);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //mensaje al entrar a la pantalla
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Informacion de la card
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                    screenInfo.setVisibility(View.VISIBLE);
                    screenInfo.startAnimation(expandAnimation);

                    Cards object = (Cards) dataObject;
                    String imageUrl = object.getProfileImageUrl();
                    String name = object.getName();
                    infoName.setText(name);

                    Glide.with(infoImage);
                    if (imageUrl.equals("default")) {
                        infoImage.setImageResource(R.drawable.image_profile);
                    } else {
                        Glide.with(infoImage.getContext()).load(imageUrl).into(infoImage);
                    }



            }
        });

        //Botones flotantes



        bt_smsRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redireccionar a la pagina de mensajes
                startActivity(new Intent(getApplicationContext(), Messages.class));
                finish();
            }
        });

        bt_skip_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_skip);
                try {
                    flingContainer.getTopCardListener().selectLeft();
                } catch (Exception e) {
                    Toast.makeText(PrincipalPage.this, "No hay más usuarios", Toast.LENGTH_SHORT).show();
                }
                screenInfo.setVisibility(View.GONE);
            }
        });

        bt_like_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimarFab(bt_like);
                try {
                    flingContainer.getTopCardListener().selectRight();
                } catch (Exception e) {
                    Toast.makeText(PrincipalPage.this, "No hay más usuarios", Toast.LENGTH_SHORT).show();
                }
                screenInfo.setVisibility(View.GONE);
            }
        });

        infoHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenInfo.startAnimation(contractAnimation);
                screenInfo.setVisibility(View.GONE);
            }
        });

        layoutHide.setOnClickListener(new View.OnClickListener() { //Layout invisible que se hace visible cuando hay match
            @Override
            public void onClick(View view) {
                layoutMatch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void isConnectionMatch(String userId) { //Método que comprueba si hay match
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("like").child(userId); //revisa si hay match en el nodo actual
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() { //Listener para el nodo actual de la base de datos
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){ //si existe un match

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);

                    // Se cargan las imagenes de perfil de ambos usuarios
                    usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            Glide.with(myImage);
                            myImageUrl = datasnapshot.child(currentUId).child("profileImageUrl").getValue().toString();
                            if ("default".equals(myImageUrl)) {
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(myImage);
                            } else {
                                Glide.with(getApplication()).load(myImageUrl).into(myImage);
                            }

                            Glide.with(matchImage);
                            matchImageUrl = datasnapshot.child(snapshot.getKey()).child("profileImageUrl").getValue().toString();
                            if ("default".equals(matchImageUrl)) {
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(matchImage);
                            } else {
                                Glide.with(getApplication()).load(matchImageUrl).into(matchImage);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    //Show layout of Match
                    layoutMatch.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void init() {
        //this.bt_back = findViewById(R.id.bt_back);

        this.bt_skip_info = findViewById(R.id.bt_skip_info);
        this.bt_like_info = findViewById(R.id.bt_like_info);
        this.layoutHide= findViewById(R.id.hideLayout);
        this.layoutMatch= findViewById(R.id.matchLayout);
        this.bt_smsRedirect= findViewById(R.id.sms_redirect);
        this.screenInfo= findViewById(R.id.screen_info);
        this.myImage = findViewById(R.id.profile_image_you);
        this.matchImage = findViewById(R.id.profile_image_match);
        this.infoImage = findViewById(R.id.profile_image_info);
        this.infoName = findViewById(R.id.name_info);
    }
    private void AnimarFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                fab.animate().scaleX(1f).scaleY(1f);
            }
        });
    }

    private String userSex;
    private String oppositeUserSex;
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
                        getOppositeUserSex();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    public void getOppositeUserSex() { //Método que obtiene el sexo del usuario opuesto a la persona actual
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.child("sex").getValue() !=null) {
                    if (snapshot.exists() && !snapshot.child("connections").child("skip").hasChild(currentUId) && !snapshot.child("connections").child("like").hasChild(currentUId) && snapshot.child("sex").getValue().toString().equals(oppositeUserSex) && !snapshot.getKey().equals(currentUId)) {
                        String imageUrl = "default";
                        if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                            imageUrl = snapshot.child("profileImageUrl").getValue().toString();

                        }
                        Cards Item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), imageUrl);
                        colaItems.add(Item); // se agrega el item a la cola
                        adaptadorItems.notifyDataSetChanged(); // se actualiza la vista
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
    }

}