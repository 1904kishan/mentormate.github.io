package com.example.mentormate.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;

public class Admin_detailActivity extends AppCompatActivity {
Button logout;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);
        getSupportActionBar().setTitle("Admin Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logout=findViewById(R.id.Admin_logout_button);
        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                startActivity(new Intent(Admin_detailActivity.this, LoginActivity.class));
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
