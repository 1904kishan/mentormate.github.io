package com.example.mentormate.Activitys;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mentormate.R;

public class Admin_profileActivity extends AppCompatActivity {
    LinearLayout expert, customer, feedback, payment, complaint, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        getSupportActionBar().setTitle("Admin Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expert = findViewById(R.id.admin_manageexpert);
        customer = findViewById(R.id.admin_managecustomer);
        feedback = findViewById(R.id.admin_managefeedback);
        payment = findViewById(R.id.admin_managepayment);
        complaint = findViewById(R.id.admin_managecomplaints);
        detail = findViewById(R.id.admin_details);

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_profileActivity.this, Manage_expertActivity.class));
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_profileActivity.this, ManageCustomerActivity.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_profileActivity.this, Manage_feedbackActivity.class));
            }
        });

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_profileActivity.this, Manage_complaintActivity.class));
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_profileActivity.this, Manage_paymentActivity.class));
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_profileActivity.this, Admin_detailActivity.class));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
