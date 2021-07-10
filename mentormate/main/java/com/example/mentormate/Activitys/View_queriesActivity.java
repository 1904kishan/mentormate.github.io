package com.example.mentormate.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mentormate.R;

public class View_queriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_queries);
        getSupportActionBar().setTitle("View Queries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startActivity(new Intent(View_queriesActivity.this,My_queriesActivity.class));

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
