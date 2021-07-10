package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class BidActivity extends AppCompatActivity {

    EditText edit;
    Button bid;
    SharedPreferences sp;
    ListView listView;
    ArrayList<BidList> bidLists;
    BidAdapter bidAdapter;
    RelativeLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        getSupportActionBar().setTitle("Bid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        bottomLayout = findViewById(R.id.bid_bottom_layout);
        edit=findViewById(R.id.bid_typehere);
        bid=findViewById(R.id.bid_send);

        if(sp.getString(ConstantSP.USERTYPE,"").equals("User")){
            bottomLayout.setVisibility(View.GONE);
        }
        else{
            bottomLayout.setVisibility(View.VISIBLE);
        }

        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals("")) {
                    edit.setError("Please Insert Your Bid");
                } else {
                    if (new ConnectionDetector(BidActivity.this).isConnectingToInternet()) {
                        new addBid().execute();
                    } else {
                        new ConnectionDetector(BidActivity.this).connectiondetect();
                    }
                }
            }
            });

        listView = findViewById(R.id.bid_list);

        if (new ConnectionDetector(BidActivity.this).isConnectingToInternet()) {
            new getBid().execute();
        } else {
            new ConnectionDetector(BidActivity.this).connectiondetect();
        }

        }

    private class addBid extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(BidActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("query",sp.getString(ConstantSP.QUERYID,""));
            hashMap.put("expertId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("bid",edit.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"addBid.php",MakeServiceCall.POST,hashMap);

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    Toast.makeText(BidActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    clearData();
                    startActivity(new Intent(BidActivity.this,View_queriesActivity.class));
                } else {
                    Toast.makeText(BidActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void clearData() {
        edit.setText("");
    }

    private class getBid extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(BidActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("query",sp.getString(ConstantSP.QUERYID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getBid.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    bidLists=new ArrayList<>();
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject jsonObject = array.getJSONObject(i);
                        BidList list=new BidList();
                        list.setId(jsonObject.getString("id"));
                        list.setExpertId(jsonObject.getString("expertId"));
                        list.setName(jsonObject.getString("name"));
                        list.setQuery(jsonObject.getString("query"));
                        list.setBid(jsonObject.getString("bid"));
                        list.setDate(jsonObject.getString("date"));
                        bidLists.add(list);
                    }
                    bidAdapter=new BidAdapter(BidActivity.this,bidLists);
                    listView.setAdapter(bidAdapter);
                }
                else{
                    Toast.makeText(BidActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
