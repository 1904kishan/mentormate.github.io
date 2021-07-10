package com.example.mentormate.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;

public class SplashActivity extends AppCompatActivity {
    ImageView logo;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        logo = findViewById(R.id.main_splash);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(2500);
        logo.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(sp.getString(ConstantSP.USERTYPE,"").equals("Admin")){
                    Intent intent = new Intent(SplashActivity.this, Admin_profileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(sp.getString(ConstantSP.USERTYPE,"").equals("User")){
                    Intent intent = new Intent(SplashActivity.this, User_profileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(sp.getString(ConstantSP.USERTYPE,"").equals("Expert")){
                    Intent intent = new Intent(SplashActivity.this, Expert_profileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}