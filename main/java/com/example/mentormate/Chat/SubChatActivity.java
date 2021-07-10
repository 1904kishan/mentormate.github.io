package com.example.mentormate.Chat;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SubChatActivity extends AppCompatActivity {

    SharedPreferences sp;
    RecyclerView recyclerView;
    ArrayList<SubChatList> chatLists;
    SubChatListAdapter chatAdapter;
    SwipeRefreshLayout refreshLayout;
    EditText message;
    ImageView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_chat);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        getSupportActionBar().setTitle(sp.getString(ConstantSP.CHATNAME, ""));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.sub_chat_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(SubChatActivity.this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (new ConnectionDetector(SubChatActivity.this).isConnectingToInternet()) {
            new getData().execute();
            new updateData().execute();
        } else {
            new ConnectionDetector(SubChatActivity.this).connectiondetect();
        }


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sub_chat_swipe);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getData().execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        refreshLayout.setRefreshing(false);
                    }
                }, 2500); // Delay in millis
            }
        });

        message = (EditText) findViewById(R.id.sub_chat_message);
        submit = (ImageView) findViewById(R.id.sub_chat_send);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().equalsIgnoreCase("")) {
                    message.setError("Message Required");
                    return;
                } else {
                    if (new ConnectionDetector(SubChatActivity.this).isConnectingToInternet()) {
                        new insertData().execute();
                    } else {
                        new ConnectionDetector(SubChatActivity.this).connectiondetect();
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            if(sp.getString(ConstantSP.USERTYPE,"").equals("User")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", "U"+sp.getString(ConstantSP.USERID, ""));
                hashMap.put("receiverId", "E"+sp.getString(ConstantSP.CHATID, ""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getChat.php", MakeServiceCall.POST, hashMap);
            }
            else{
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", "E"+sp.getString(ConstantSP.USERID, ""));
                hashMap.put("receiverId", "U"+sp.getString(ConstantSP.CHATID, ""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getChat.php", MakeServiceCall.POST, hashMap);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    chatLists = new ArrayList<SubChatList>();
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        SubChatList list = new SubChatList();
                        list.setId(jsonObject.getString("id"));
                        list.setSenderId(jsonObject.getString("senderId"));
                        list.setReceiverId(jsonObject.getString("receiverId"));
                        list.setMessage(jsonObject.getString("message"));
                        list.setDate(jsonObject.getString("created_date"));
                        chatLists.add(list);
                    }
                    chatAdapter = new SubChatListAdapter(SubChatActivity.this, chatLists);
                    recyclerView.setAdapter(chatAdapter);
                } else {
                    Toast.makeText(SubChatActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class insertData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SubChatActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            if(sp.getString(ConstantSP.USERTYPE,"").equals("User")){
                hashMap.put("userId", "U"+sp.getString(ConstantSP.USERID, ""));
                hashMap.put("receiverId", "E"+sp.getString(ConstantSP.CHATID, ""));
                hashMap.put("message", message.getText().toString());
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "addChat.php", MakeServiceCall.POST, hashMap);
            }
            else{
                hashMap.put("userId", "E"+sp.getString(ConstantSP.USERID, ""));
                hashMap.put("receiverId", "U"+sp.getString(ConstantSP.CHATID, ""));
                hashMap.put("message", message.getText().toString());
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "addChat.php", MakeServiceCall.POST, hashMap);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    new getData().execute();
                    message.setText("");
                } else {
                    Toast.makeText(SubChatActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class updateData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (sp.getString(ConstantSP.USERTYPE, "").equals("User")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", "U"+sp.getString(ConstantSP.USERID, ""));
                hashMap.put("receiverId", "E"+sp.getString(ConstantSP.CHATID, ""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "updateChat.php", MakeServiceCall.POST, hashMap);
            }
            else{
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", "E"+sp.getString(ConstantSP.USERID, ""));
                hashMap.put("receiverId", "U"+sp.getString(ConstantSP.CHATID, ""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "updateChat.php", MakeServiceCall.POST, hashMap);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                } else {
                    Toast.makeText(SubChatActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
