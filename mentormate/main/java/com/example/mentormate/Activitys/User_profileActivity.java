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

public class User_profileActivity extends AppCompatActivity {

   LinearLayout query,reply,meeting,membership,payment,bookexpert,detail;
   SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        query=findViewById(R.id.userquery);
        reply=findViewById(R.id.userreply);
        meeting=findViewById(R.id.usermeeting);
       // membership=findViewById(R.id.usermembership);
        payment=findViewById(R.id.userpayment);
        bookexpert=findViewById(R.id.user_bookexpert);
        detail=findViewById(R.id.userdetail);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSP.QUERYUSER,"user").commit();
                startActivity(new Intent(User_profileActivity.this,My_queriesActivity.class));
            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(User_profileActivity.this,ReplyActivity.class));
                startActivity(new Intent(User_profileActivity.this, ChatListActivity.class));
            }
        });

        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_profileActivity.this,Request_meetingActivity.class));
            }
        });

       /* membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_profileActivity.this,MembershipActivity.class));
            }
        });*/

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_profileActivity.this,PaymentActivity.class));
            }
        });

        bookexpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_profileActivity.this,Book_ExpertActivity.class));
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_profileActivity.this, UserDetailsActivity.class));
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



