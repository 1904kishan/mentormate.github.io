package com.example.mentormate.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;

public class UserDetailsActivity extends AppCompatActivity {

    Spinner city,state;
    EditText name,email,contact,password;
    RadioGroup rg;
    Button feedback,complain,update,logout;

    SharedPreferences sp;

    String[] cityArray = {"Ahmedabad"};
    String[] stateArray = {"Gujarat"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getSupportActionBar().setTitle("User Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        name = findViewById(R.id.detail_name);
        email = findViewById(R.id.detail_email);
        contact = findViewById(R.id.detail_cno);
        password = findViewById(R.id.detail_password);
        city = findViewById(R.id.detail_city);
        state = findViewById(R.id.detail_state);
        rg = findViewById(R.id.detail_rg);
        update = findViewById(R.id.detail_button);
        logout=findViewById(R.id.detail_logout_button);

        complain=findViewById(R.id.detail_complain_button);
        feedback=findViewById(R.id.detail_feedback_button);

        ArrayAdapter cityAdapter = new ArrayAdapter(UserDetailsActivity.this, android.R.layout.simple_list_item_checked, cityArray);
        city.setAdapter(cityAdapter);

        ArrayAdapter stateAdapter = new ArrayAdapter(UserDetailsActivity.this, android.R.layout.simple_list_item_checked, stateArray);
        state.setAdapter(stateAdapter);

        name.setText(sp.getString(ConstantSP.NAME,""));
        email.setText(sp.getString(ConstantSP.EMAIL,""));
        contact.setText(sp.getString(ConstantSP.CONTACT_NO,""));
        password.setText(sp.getString(ConstantSP.PASSWORD,""));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().length() < 10 || contact.getText().toString().length() > 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UserDetailsActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDetailsActivity.this, "updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(UserDetailsActivity.this,Manage_complaintActivity.class));
                sp.edit().putString(ConstantSP.FEEDBACKExpId,"1").commit();
                startActivity(new Intent(UserDetailsActivity.this,Manage_feedbackActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
