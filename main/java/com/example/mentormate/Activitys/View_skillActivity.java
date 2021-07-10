package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mentormate.Adapter.SkillAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.SkillList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class View_skillActivity extends AppCompatActivity {

    ListView listView;

    String[] skill={"Android","Java","PHP"};

    SkillAdapter adapter;

    ArrayList<SkillList> arrayskilllist;

    FloatingActionButton add;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_skill);
        getSupportActionBar().setTitle("Manage Skill");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        add = findViewById(R.id.viewskill_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_skillActivity.this,Manage_skillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        listView=findViewById(R.id.viewskill_list);
        if(new ConnectionDetector(View_skillActivity.this).isConnectingToInternet()){
            new getSkillData().execute();
        }
        else{
            new ConnectionDetector(View_skillActivity.this).connectiondetect();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getSkillData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(View_skillActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getSkill.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    arrayskilllist=new ArrayList<>();
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject jsonObject = array.getJSONObject(i);
                        SkillList list=new SkillList();
                        list.setSkill(jsonObject.getString("skill"));
                        arrayskilllist.add(list);
                    }
                    SkillAdapter adapter=new SkillAdapter(View_skillActivity.this,arrayskilllist);
                    listView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(View_skillActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
