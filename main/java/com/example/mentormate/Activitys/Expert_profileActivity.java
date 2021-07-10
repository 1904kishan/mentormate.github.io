package com.example.mentormate.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mentormate.Chat.ChatListActivity;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;

public class Expert_profileActivity extends AppCompatActivity {
LinearLayout skill,reply,request,query,membership,details;
SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_profile);
        getSupportActionBar().setTitle("Expert Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        skill=findViewById(R.id.expert_manageskill);
        reply=findViewById(R.id.expert_reply);
        request=findViewById(R.id.expert_viewrequest);
        query=findViewById(R.id.expert_viewqueries);
        //membership=findViewById(R.id.expert_membership);
        details=findViewById(R.id.expert_details);

        skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Expert_profileActivity.this,View_skillActivity.class));
            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Expert_profileActivity.this,Reply_customerActivty.class));
                startActivity(new Intent(Expert_profileActivity.this, ChatListActivity.class));
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Expert_profileActivity.this,Manage_requestActivity.class));
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSP.QUERYUSER,"expert").commit();
                startActivity(new Intent(Expert_profileActivity.this,My_queriesActivity.class));
            }
        });

      /*  membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Expert_profileActivity.this,MembershipActivity.class));
            }
        });*/

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Expert_profileActivity.this,Expert_detailsActivity.class));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            else{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        else{
            finish();
        }
    }
}
