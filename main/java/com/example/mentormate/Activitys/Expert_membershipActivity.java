package com.example.mentormate.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mentormate.R;

public class Expert_membershipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_membership);
        getSupportActionBar().setTitle("Membership");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startActivity(new Intent(Expert_membershipActivity.this,MembershipActivity.class));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
