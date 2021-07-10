package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mentormate.Adapter.ManageFeedbackAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ManageFeedbackList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Manage_feedbackActivity extends AppCompatActivity {

    ListView listView;

    String[] name={"xyz","abc"};
    String[] email={"xyz@gmail.com","abc@gmail.com"};
    String[] date={"1/1/2019","5/8/20190"};
    String[] sub={"Android","PHP"};
    String[] feedback={"good performance","trustworthy"};

    ManageFeedbackAdapter adapter;

    ArrayList<ManageFeedbackList> arraymanagefeedbacklist;

    EditText feedbackEdt;
    Button send;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feedback);
        getSupportActionBar().setTitle("Manage Complain");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedbackEdt = findViewById(R.id.manage_feedback_edit);
        send = findViewById(R.id.manage_feedback_send);

        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        if(sp.getString(ConstantSP.USERTYPE,"").equals("Admin")){
            feedbackEdt.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
        }
        else{
            feedbackEdt.setVisibility(View.VISIBLE);
            send.setVisibility(View.VISIBLE);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(feedbackEdt.getText().toString().equals("")){
                 feedbackEdt.setError("Complain Required");
             }
             else{
                 if(new ConnectionDetector(Manage_feedbackActivity.this).isConnectingToInternet()){
                     new addFeedback().execute();
                 }
                 else{
                     new ConnectionDetector(Manage_feedbackActivity.this).connectiondetect();
                 }
             }
            }
        });

        listView=findViewById(R.id.manage_feedback_list);
        if(new ConnectionDetector(Manage_feedbackActivity.this).isConnectingToInternet()){
            new getData().execute();
        }
        else{
            new ConnectionDetector(Manage_feedbackActivity.this).connectiondetect();
        }

    }

    private class addFeedback extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(Manage_feedbackActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("expertId",sp.getString(ConstantSP.FEEDBACKExpId,""));
            hashMap.put("feedback",feedbackEdt.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"addFeedback.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    Toast.makeText(Manage_feedbackActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    if(new ConnectionDetector(Manage_feedbackActivity.this).isConnectingToInternet()){
                        new getData().execute();
                    }
                    else{
                        new ConnectionDetector(Manage_feedbackActivity.this).connectiondetect();
                    }
                }
                else{
                    Toast.makeText(Manage_feedbackActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(Manage_feedbackActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userType",sp.getString(ConstantSP.USERTYPE,""));
            hashMap.put("expertId",sp.getString(ConstantSP.FEEDBACKExpId,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getExpertFeedback.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    arraymanagefeedbacklist = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        ManageFeedbackList list=new ManageFeedbackList();
                        list.setName(jsonObject.getString("userName"));
                        list.setEmail(jsonObject.getString("userEmail"));
                        list.setDate(jsonObject.getString("created_date"));
                        list.setSub("");
                        list.setFeedback(jsonObject.getString("feedback"));
                        arraymanagefeedbacklist.add(list);
                    }
                    ManageFeedbackAdapter adapter=new ManageFeedbackAdapter(Manage_feedbackActivity.this,arraymanagefeedbacklist);
                    listView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(Manage_feedbackActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
