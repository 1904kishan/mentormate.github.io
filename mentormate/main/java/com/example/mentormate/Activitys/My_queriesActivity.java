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

import com.example.mentormate.Adapter.QueryAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.QueryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class My_queriesActivity extends AppCompatActivity {
    ListView listview;
    SharedPreferences sp;
    String[] id={"abc","xyz","mon"};
    String[] date={"1/1/2019","8/2/2019","11/11/2019"};
    String[] query={"book my expert","coffee hub","book my chhotu"};

    QueryAdapter adapter;

    ArrayList<QueryList> arrayquerylist;

    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_queries);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        add = findViewById(R.id.myquery_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(My_queriesActivity.this,Post_QueryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        if(sp.getString(ConstantSP.QUERYUSER,"").equals("user")){
            getSupportActionBar().setTitle("My Queries");
            add.setVisibility(View.VISIBLE);
        }
        else {
            getSupportActionBar().setTitle("Queries");
            add.setVisibility(View.GONE);
        }
        listview=findViewById(R.id.myquery_list);
        /*arrayquerylist=new ArrayList<>();
        for(int i=0;i<id.length;i++)
        {
            QueryList list=new QueryList();
            list.setId(id[i]);
            list.setDate(date[i]);
            list.setQuery(query[i]);

            arrayquerylist.add(list);
        }
        QueryAdapter adapter=new QueryAdapter(My_queriesActivity.this,arrayquerylist);
        listview.setAdapter(adapter);*/


        if(new ConnectionDetector(My_queriesActivity.this).isConnectingToInternet()){
            new getData().execute();
        }
        else{
            new ConnectionDetector(My_queriesActivity.this).connectiondetect();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(My_queriesActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(sp.getString(ConstantSP.QUERYUSER,"").equals("user")){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
                hashMap.put("expertId","");
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getQuery.php",MakeServiceCall.POST,hashMap);
            }
            else {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("userId","");
                hashMap.put("expertId",sp.getString(ConstantSP.USERID,""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getQuery.php",MakeServiceCall.POST,hashMap);
            }

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {

                    JSONArray jsonarray=object.getJSONArray("response");
                    arrayquerylist=new ArrayList<>();
                    for(int i=0;i<jsonarray.length();i++)
                    {
                        JSONObject jsonobject=jsonarray.getJSONObject(i);
                        QueryList list=new QueryList();
                        list.setId(jsonobject.getString("id"));
                        list.setDate(jsonobject.getString("date"));
                        list.setQuery(jsonobject.getString("query"));
                        list.setBid(jsonobject.getString("bid"));
                        list.setName(jsonobject.getString("name"));

                        arrayquerylist.add(list);
                    }
                    QueryAdapter adapter=new QueryAdapter(My_queriesActivity.this,arrayquerylist);
                    listview.setAdapter(adapter);

                } else {
                    Toast.makeText(My_queriesActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    }

