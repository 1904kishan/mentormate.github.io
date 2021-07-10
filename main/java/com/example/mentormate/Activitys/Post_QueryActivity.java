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
import android.widget.EditText;
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

public class Post_QueryActivity extends AppCompatActivity {
    EditText query;
    Button submit;
    SharedPreferences sp;
    Spinner skill;
    ArrayList<String> skillArray;
    String sSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__query);
        getSupportActionBar().setTitle("Post Queries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        query = findViewById(R.id.post_querytext);
        submit = findViewById(R.id.post_querybutton);
        skill = findViewById(R.id.post_query_skill);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (query.getText().toString().equals("")) {
                    query.setError("Please insert your query");
                } else {
                    if (new ConnectionDetector(Post_QueryActivity.this).isConnectingToInternet()) {
                        new addQuery().execute();

                    } else {
                        new ConnectionDetector(Post_QueryActivity.this).connectiondetect();
                    }
                }

            }


        });

        if(new ConnectionDetector(Post_QueryActivity.this).isConnectingToInternet()){
            new getSkill().execute();
        }
        else{
            new ConnectionDetector(Post_QueryActivity.this).connectiondetect();
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
    }

    private class getSkill extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Post_QueryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getExpertise.php",MakeServiceCall.POST,new HashMap<String, String>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    skillArray = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        skillArray.add(jsonObject.getString("name"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(Post_QueryActivity.this,android.R.layout.simple_list_item_1,skillArray);
                    skill.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearData() {
        query.setText("");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class addQuery extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Post_QueryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userId", sp.getString(ConstantSP.USERID, ""));
            hashMap.put("query", query.getText().toString());
            hashMap.put("skill",sSkill);
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "addQuery.php", MakeServiceCall.POST, hashMap);

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    Toast.makeText(Post_QueryActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    clearData();
                    startActivity(new Intent(Post_QueryActivity.this, My_queriesActivity.class));
                } else {
                    Toast.makeText(Post_QueryActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
