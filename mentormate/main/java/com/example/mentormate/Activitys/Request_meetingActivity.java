package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mentormate.Adapter.MeetingAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.MeetingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Request_meetingActivity extends AppCompatActivity {
    ListView listview;

    String[] name={"abc","xyz","mon"};

    //MeetAdapter adapter;

    //ArrayList<MeetList> arraymeetlist;
    SharedPreferences sp;
    MeetingAdapter adapter;
    ArrayList<MeetingList> arraymeetinglist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_meeting);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        getSupportActionBar().setTitle("Request Meeting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview=findViewById(R.id.request_meeting_list);
        if (new ConnectionDetector(Request_meetingActivity.this).isConnectingToInternet()) {
            new getMeetingRequest().execute();
        } else {
            new ConnectionDetector(Request_meetingActivity.this).connectiondetect();
        }
        /*arraymeetlist=new ArrayList<>();
        for(int i=0;i<name.length;i++)
        {
            MeetList list=new MeetList();
            list.setName(name[i]);
            arraymeetlist.add(list);
        }
        MeetAdapter adapter=new MeetAdapter(Request_meetingActivity.this,arraymeetlist);
        listview.setAdapter(adapter);*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getMeetingRequest extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Request_meetingActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("expertId","");
            hashMap.put("type","meeting");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getRequest.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    arraymeetinglist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        MeetingList list = new MeetingList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("expertname"));
                        list.setQuery(jsonObject.getString("query"));
                        list.setStatus(jsonObject.getString("status"));
                        list.setDate(jsonObject.getString("created_date"));
                        list.setAmount(jsonObject.getString("amount"));
                        list.setPaymentType(jsonObject.getString("paymentType"));
                        list.setTransactionId(jsonObject.getString("transactionId"));
                        arraymeetinglist.add(list);
                    }
                    adapter = new MeetingAdapter(Request_meetingActivity.this, arraymeetinglist);
                    listview.setAdapter(adapter);
                }
                else{
                    Toast.makeText(Request_meetingActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
