package com.example.mentormate.Chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ChatListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog pd;
    ArrayList<ChatList> chatLists;
    ChatListAdapter chatAdapter;
    SwipeRefreshLayout refreshLayout;
    SharedPreferences sp;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        sp = getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
        if (sp.getString(ConstantSP.USERTYPE, "").equals("Expert")) {
            getSupportActionBar().setTitle("User List");
        } else {
            getSupportActionBar().setTitle("Expert List");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = (SearchView) findViewById(R.id.chat_list_search_view);

        recyclerView = (RecyclerView) findViewById(R.id.chat_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatListActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (new ConnectionDetector(ChatListActivity.this).isConnectingToInternet()) {
            new getData().execute();
        } else {
            new ConnectionDetector(ChatListActivity.this).connectiondetect();
        }

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_list_swipe);

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                chatAdapter.filter(text);
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(ChatListActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (sp.getString(ConstantSP.USERTYPE, "").equals("User")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", sp.getString(ConstantSP.USERID, ""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getExpertList.php", MakeServiceCall.POST, hashMap);
            }
            else{
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userId", sp.getString(ConstantSP.USERID, ""));
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getUserList.php", MakeServiceCall.POST, hashMap);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    chatLists = new ArrayList<ChatList>();
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        ChatList list = new ChatList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("name"));
                        list.setUnread(String.valueOf(jsonObject.getString("unread")));
                        chatLists.add(list);
                    }
                    chatAdapter = new ChatListAdapter(ChatListActivity.this, chatLists);
                    recyclerView.setAdapter(chatAdapter);
                    Collections.sort(chatLists, new Comparator<ChatList>() {
                        @Override
                        public int compare(ChatList lhs, ChatList rhs) {
                            return rhs.getUnread().trim().compareTo(lhs.getUnread().trim());
                        }
                    });
                    chatAdapter.notifyDataSetChanged();
                }
                else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ChatListActivity.this);
                    builder.setMessage("Chat List Not Available");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
