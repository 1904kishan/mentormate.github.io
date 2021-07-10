package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ManageExpertList;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Manage_expertActivity extends AppCompatActivity {
    ListView listView;

    String[] name = {"expert A", "expert B"};
    String[] date = {"1/1/2019", "4/11/2019"};
    String[] skill = {"Android", "PHP"};
    String[] experience = {"2 years", "1 year"};

    int[] image = {R.drawable.temp_document, R.drawable.temp_document};

    ManageExpertAdapter adapter;
    ArrayList<ManageExpertList> arraymanageexpertlist;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expert);
        getSupportActionBar().setTitle("Manage Expert");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        listView = findViewById(R.id.manage_expert_list);
        if (new ConnectionDetector(Manage_expertActivity.this).isConnectingToInternet()) {
            new getData().execute();
        } else {
            new ConnectionDetector(Manage_expertActivity.this).connectiondetect();
        }
        /*arraymanageexpertlist=new ArrayList<>();
        for(int i=0;i<name.length;i++)
        {
            ManageExpertList list=new ManageExpertList();
            list.setName(name[i]);
            list.setDate(date[i]);
            list.setSkill(skill[i]);
            list.setExperience(experience[i]);
            list.setImage(image[i]);

            arraymanageexpertlist.add(list);
        }
        ManageExpertAdapter adapter=new ManageExpertAdapter(Manage_expertActivity.this,arraymanageexpertlist);
        listView.setAdapter(adapter);*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Manage_expertActivity.this);
            pd.setCancelable(true);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userId", sp.getString(ConstantSP.USERID, ""));
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getExpertList.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("Status").equals("True")) {
                    JSONArray array = jsonObject.getJSONArray("response");
                    arraymanageexpertlist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        ManageExpertList list = new ManageExpertList();
                        list.setId(object.getString("id"));
                        list.setName(object.getString("name"));
                        list.setDate(object.getString("date"));
                        list.setSkill(object.getString("skill"));
                        list.setExperience(object.getString("experience"));
                        list.setImage(object.getString("document"));
                        list.setStatus(object.getString("status"));
                        arraymanageexpertlist.add(list);
                    }
                    ManageExpertAdapter adapter = new ManageExpertAdapter(Manage_expertActivity.this, arraymanageexpertlist);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(Manage_expertActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ManageExpertAdapter extends BaseAdapter {
        Context context;
        ArrayList<ManageExpertList> arraymanageexpertlist;
        String sId, sStatus;

        public ManageExpertAdapter(Manage_expertActivity activity, ArrayList<ManageExpertList> arraymanageexpertlist) {
            this.context = activity;
            this.arraymanageexpertlist = arraymanageexpertlist;
        }

        @Override
        public int getCount() {
            return arraymanageexpertlist.size();
        }

        @Override
        public Object getItem(int i) {
            return arraymanageexpertlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_manageexpert, null);
            TextView name = view.findViewById(R.id.custom_manage_expert_name);
            TextView date = view.findViewById(R.id.custom_manage_expert_date);
            TextView skill = view.findViewById(R.id.custom_manage_expert_skill);
            TextView status = view.findViewById(R.id.custom_manage_expert_status);
            TextView experience = view.findViewById(R.id.custom_manage_expert_experience);
            ImageView image = view.findViewById(R.id.custom_manage_expert_doc);
            Button verify = view.findViewById(R.id.custom_manage_expert_verify);
            Button block = view.findViewById(R.id.custom_manage_expert_block);

            name.setText(arraymanageexpertlist.get(i).getName());
            status.setText(arraymanageexpertlist.get(i).getStatus());
            date.setText(arraymanageexpertlist.get(i).getDate());
            skill.setText("Skill : " + arraymanageexpertlist.get(i).getSkill());
            experience.setText("Experience : " + arraymanageexpertlist.get(i).getExperience());
            Picasso.with(context).load(ConstantSP.IMAGE_URL + arraymanageexpertlist.get(i).getImage()).placeholder(R.mipmap.ic_launcher).into(image);

            if (arraymanageexpertlist.get(i).getStatus().equals("Pending")) {
                verify.setVisibility(View.VISIBLE);
                block.setVisibility(View.VISIBLE);
            } else {
                verify.setVisibility(View.GONE);
                block.setVisibility(View.GONE);
            }

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sId = arraymanageexpertlist.get(i).getId();
                    sStatus = "Verified";
                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        new updateStatus().execute();
                    } else {
                        new ConnectionDetector(context).connectiondetect();
                    }
                }
            });

            block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sId = arraymanageexpertlist.get(i).getId();
                    sStatus = "Blocked";
                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        new updateStatus().execute();
                    } else {
                        new ConnectionDetector(context).connectiondetect();
                    }
                }
            });

            //image.setImageResource(R.drawable.temp_document);
            return view;
        }

        private class updateStatus extends AsyncTask<String, String, String> {

            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(context);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", sId);
                hashMap.put("status", sStatus);
                return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "updateExpertStatus.php", MakeServiceCall.POST, hashMap);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("Status").equals("True")) {
                        Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        new getData().execute();
                    } else {
                        Toast.makeText(context, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
