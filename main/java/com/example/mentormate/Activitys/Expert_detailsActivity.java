package com.example.mentormate.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Expert_detailsActivity extends AppCompatActivity {
    Spinner city,state;
    EditText name,email,contact,password,experience,skill;
    RadioGroup rg;
    Button update,logout;

    SharedPreferences sp;

    String[] cityArray = {"Ahmedabad"};
    String[] stateArray = {"Gujarat"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_details);
        getSupportActionBar().setTitle("Expert Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        name = findViewById(R.id.expertdetail_name);
        email = findViewById(R.id.expertdetail_email);
        contact = findViewById(R.id.expertdetail_cno);
        password = findViewById(R.id.expertdetail_password);
        city = findViewById(R.id.expertdetail_city);
        state = findViewById(R.id.expertdetail_state);
        rg = findViewById(R.id.expertdetail_rg);
        update = findViewById(R.id.expertdetail_button);
        experience=findViewById(R.id.expertdetail_experience);
        skill=findViewById(R.id.expertdetail_skills);
        logout=findViewById(R.id.expertdetail_logout_button);

        ArrayAdapter cityAdapter = new ArrayAdapter(Expert_detailsActivity.this, android.R.layout.simple_list_item_checked, cityArray);
        city.setAdapter(cityAdapter);

        ArrayAdapter stateAdapter = new ArrayAdapter(Expert_detailsActivity.this, android.R.layout.simple_list_item_checked, stateArray);
        state.setAdapter(stateAdapter);

        name.setText(sp.getString(ConstantSP.NAME,""));
        email.setText(sp.getString(ConstantSP.EMAIL,""));
        contact.setText(sp.getString(ConstantSP.CONTACT_NO,""));
        password.setText(sp.getString(ConstantSP.PASSWORD,""));
        experience.setText(sp.getString(ConstantSP.EXPERIENCE,""));
        skill.setText(sp.getString(ConstantSP.SKILL,""));

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
                    Toast.makeText(Expert_detailsActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }else if (experience.getText().toString().equals("")) {
                    email.setError("Experience Required");
                }else if (skill.getText().toString().equals("")) {
                    email.setError("Skill Required");
                }
                else {
                    Toast.makeText(Expert_detailsActivity.this, "updated Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                startActivity(new Intent(Expert_detailsActivity.this, LoginActivity.class));
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
