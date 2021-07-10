package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Manage_skillActivity extends AppCompatActivity {

    Spinner skill;
    Button submit;
    SharedPreferences sp;
    ArrayList<String> skillArray;
    String sSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_skill);
        getSupportActionBar().setTitle("Add Skill");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        skill = findViewById(R.id.skill_text);
        submit = findViewById(R.id.skill_submitbutton);

        if (new ConnectionDetector(Manage_skillActivity.this).isConnectingToInternet()) {
            new getSkill().execute();
        } else {
            new ConnectionDetector(Manage_skillActivity.this).connectiondetect();
        }

        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sSkill = skillArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(Manage_skillActivity.this).isConnectingToInternet()) {
                    new addSkill().execute();

                } else {
                    new ConnectionDetector(Manage_skillActivity.this).connectiondetect();
                }
            }
        });
    }

    private class getSkill extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Manage_skillActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getExpertise.php", MakeServiceCall.POST, new HashMap<String, String>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    JSONArray array = object.getJSONArray("response");
                    skillArray = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        skillArray.add(jsonObject.getString("name"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(Manage_skillActivity.this, android.R.layout.simple_list_item_1, skillArray);
                    skill.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class addSkill extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Manage_skillActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("expertId", sp.getString(ConstantSP.USERID, ""));
            hashMap.put("skill", sSkill);
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "addSkill.php", MakeServiceCall.POST, hashMap);

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    Toast.makeText(Manage_skillActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    clearData();
                    startActivity(new Intent(Manage_skillActivity.this, View_skillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Toast.makeText(Manage_skillActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void clearData() {
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

