package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mentormate.Adapter.ManagecustomerAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ManagecustomerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageCustomerActivity extends AppCompatActivity {
    ListView listView;
    SharedPreferences sp;
    String[] name = {"eye"};
    String[] email = {"eye@gmail.com"};
    String[] city = {"Ahmedabad"};
    String[] state = {"Gujarat"};
    String[] contact = {"1234567899"};
    String[] gender = {"female"};

    ArrayList<ManagecustomerList> arrayuserdetaillist;
    ManagecustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        getSupportActionBar().setTitle("Manage Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.user_detail_list);

        if (new ConnectionDetector(ManageCustomerActivity.this).isConnectingToInternet()) {
            new getData().execute();
        } else {
            new ConnectionDetector(ManageCustomerActivity.this).connectiondetect();
        }

        /*arrayuserdetaillist = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            ManagecustomerList list = new ManagecustomerList();
            list.setName(name[i]);
            list.setEmail(email[i]);
            list.setCity(city[i]);
            list.setState(state[i]);
            list.setContact(contact[i]);
            list.setGender(gender[i]);

            arrayuserdetaillist.add(list);
        }
        ManagecustomerAdapter adapter = new ManagecustomerAdapter(ManageCustomerActivity.this, arrayuserdetaillist);
        listView.setAdapter(adapter);*/

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ManageCustomerActivity.this);
            pd.setCancelable(true);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "getCustomerList.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("Status").equals("True")) {
                    JSONArray array = jsonObject.getJSONArray("response");
                    arrayuserdetaillist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        ManagecustomerList list = new ManagecustomerList();
                        list.setName(object.getString("name"));
                        list.setEmail(object.getString("email"));
                        list.setCity(object.getString("city"));
                        list.setState(object.getString("state"));
                        list.setContact(object.getString("contact_no"));
                        list.setGender(object.getString("gender"));
                        arrayuserdetaillist.add(list);
                    }
                    ManagecustomerAdapter adapter = new ManagecustomerAdapter(ManageCustomerActivity.this, arrayuserdetaillist);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(ManageCustomerActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
