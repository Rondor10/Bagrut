package com.example.a1stapp;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.a1stapp.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_Splash extends AppCompatActivity {

    int RC_SIGN_IN = 1234;
    private FirebaseUser user_Fb;
    User newUser;
    private DatabaseReference mDatabase;
    private ImageView splash_IMG_logo;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To remove status bar - put before setContentView()!
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        relativeLayout = findViewById(R.id.relativeLayout);
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        splash_IMG_logo.setAlpha(0f);
        splash_IMG_logo.animate()
                .setDuration(1000)
                .translationY(0)
                .alpha(1.0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        StartProcess();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });

        addDemoUsersToDB();
    }

    private void addDemoUsersToDB() {

        final ArrayList<User> users = new ArrayList<User>();
        User u1 = new User();
        u1.setKey("AvivLevyKey");
        u1.setImageURL("default");
        u1.setBirthday("03/15/2002");
        u1.setFullName("aviv levy");
        u1.setMinAge(19);
        u1.setMaxAge(30);
        u1.setFirstName("Aviv");
        u1.setLastName("Levy");
        u1.setGender("Male");
        u1.setGenderWanted("Flexible");
        u1.setTel("+972557654321");
        u1.setDistance(10.0);
        u1.setLatitude(32.057667);
        u1.setLongitude(34.856575);
        u1.getCategories().put(CATEGORY.FOOTBALL.name(), true);
        u1.getCategories().put(CATEGORY.GYM.name(), false);
        u1.getCategories().put(CATEGORY.MOVIE.name(), true);
        u1.getCategories().put(CATEGORY.SWIMMING.name(), true);
        u1.getCategories().put(CATEGORY.SHOPPING.name(), true);
        u1.getCategories().put(CATEGORY.BASKETBALL.name(), false);
        u1.getCategories().put(CATEGORY.SNOOKER.name(), false);
        u1.getCategories().put(CATEGORY.CONCERT.name(), true);
        users.add(u1);

        User u2 = new User();
        u2.setKey("LiaMamanKey");
        u2.setBirthday("05/27/1997");
        u2.setImageURL("default");
        u2.setFullName("lia maman");
        u2.setMinAge(18);
        u2.setMaxAge(34);
        u2.setFirstName("Lia");
        u2.setLastName("Maman");
        u2.setGender("Female");
        u2.setGenderWanted("Flexible");
        u2.setTel("+972551111111");
        u2.setDistance(8.0);
        u2.setLatitude(32.058667);
        u2.setLongitude(34.854575);
        u2.getCategories().put(CATEGORY.FOOTBALL.name(), true);
        u2.getCategories().put(CATEGORY.GYM.name(), true);
        u2.getCategories().put(CATEGORY.MOVIE.name(), true);
        u2.getCategories().put(CATEGORY.SWIMMING.name(), false);
        u2.getCategories().put(CATEGORY.SHOPPING.name(), true);
        u2.getCategories().put(CATEGORY.BASKETBALL.name(), true);
        u2.getCategories().put(CATEGORY.SNOOKER.name(), true);
        u2.getCategories().put(CATEGORY.CONCERT.name(), false);
        users.add(u2);

        User u3 = new User();
        u3.setKey("MeitalAdivKey");
        u3.setBirthday("06/30/1972");
        u3.setImageURL("default");
        u3.setFullName("meital adiv");
        u3.setMinAge(35);
        u3.setMaxAge(55);
        u3.setFirstName("Meital");
        u3.setLastName("Adiv");
        u3.setGender("Female");
        u3.setGenderWanted("Other");
        u3.setTel("+972552222222");
        u3.setDistance(9.0);
        u3.setLatitude(32.094467);
        u3.setLongitude(34.854275);
        u3.getCategories().put(CATEGORY.FOOTBALL.name(), true);
        u3.getCategories().put(CATEGORY.GYM.name(), true);
        u3.getCategories().put(CATEGORY.MOVIE.name(), true);
        u3.getCategories().put(CATEGORY.SWIMMING.name(), true);
        u3.getCategories().put(CATEGORY.SHOPPING.name(), false);
        u3.getCategories().put(CATEGORY.BASKETBALL.name(), false);
        u3.getCategories().put(CATEGORY.SNOOKER.name(), true);
        u3.getCategories().put(CATEGORY.CONCERT.name(), true);
        users.add(u3);

        User u4 = new User();
        u4.setKey("RivkaRozenKey");
        u4.setBirthday("09/10/1973");
        u4.setFullName("rivka rozen");
        u4.setImageURL("default");
        u4.setMinAge(43);
        u4.setMaxAge(59);
        u4.setFirstName("Rivka");
        u4.setLastName("Rozen");
        u4.setGender("Female");
        u4.setGenderWanted("Male");
        u4.setTel("+97255333333");
        u4.setDistance(10.0);
        u4.setLatitude(32.058437);
        u4.setLongitude(34.854225);
        u4.getCategories().put(CATEGORY.FOOTBALL.name(), true);
        u4.getCategories().put(CATEGORY.GYM.name(), false);
        u4.getCategories().put(CATEGORY.MOVIE.name(), true);
        u4.getCategories().put(CATEGORY.SWIMMING.name(), true);
        u4.getCategories().put(CATEGORY.SHOPPING.name(), true);
        u4.getCategories().put(CATEGORY.BASKETBALL.name(), true);
        u4.getCategories().put(CATEGORY.SNOOKER.name(), true);
        u4.getCategories().put(CATEGORY.CONCERT.name(), false);
        users.add(u4);

        User u5 = new User();
        u5.setKey("EranZahaviKey");
        u5.setBirthday("05/24/2001");
        u5.setImageURL("default");
        u5.setFullName("eran zahavi");
        u5.setMinAge(18);
        u5.setMaxAge(29);
        u5.setFirstName("Eran");
        u5.setLastName("Zahavi");
        u5.setGender("Male");
        u5.setGenderWanted("Other");
        u5.setTel("+97255444444");
        u5.setDistance(5.0);
        u5.setLatitude(32.058467);
        u5.setLongitude(34.854275);
        u5.getCategories().put(CATEGORY.FOOTBALL.name(), true);
        u5.getCategories().put(CATEGORY.GYM.name(), true);
        u5.getCategories().put(CATEGORY.MOVIE.name(), true);
        u5.getCategories().put(CATEGORY.SWIMMING.name(), true);
        u5.getCategories().put(CATEGORY.SHOPPING.name(), false);
        u5.getCategories().put(CATEGORY.BASKETBALL.name(), true);
        u5.getCategories().put(CATEGORY.SNOOKER.name(), false);
        u5.getCategories().put(CATEGORY.CONCERT.name(), true);
        users.add(u5);

        for (User u : users ) {
            FirebaseDatabase.getInstance().getReference().child("users").child(u.getKey()).setValue(u);
        }
    }

    private void StartProcess() {
        //Starting process
        user_Fb = FirebaseAuth.getInstance().getCurrentUser();
        if (user_Fb != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(user_Fb.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("firstName").getValue().toString().equals("")) {
                        Intent intent = new Intent(Activity_Splash.this, ConstantSettings.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        FirebaseDatabase.getInstance().getReference().child("users").child(user_Fb.getUid()).child("online").setValue(true);
                        Intent intent = new Intent(Activity_Splash.this, Settings.class);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            login();
        }
    }

    private void login() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {   // Successfully signed in
                user_Fb = FirebaseAuth.getInstance().getCurrentUser();
                String phoneNumber = user_Fb.getPhoneNumber();
                addNewUser(phoneNumber);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
                splash_IMG_logo.setVisibility(View.GONE);
                Intent intent = new Intent(this, ConstantSettings.class);
                startActivity(intent);
                finish();
            }
            else
            {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
            }
        }
    }

    private void addNewUser(String phoneNumber) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        newUser = new User(phoneNumber, user_Fb.getUid());
        mDatabase.child("users").child(user_Fb.getUid()).setValue(newUser);

    }
}

