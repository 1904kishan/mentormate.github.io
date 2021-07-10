package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mentormate.Adapter.ProjectAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ProjectList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Book_ExpertActivity extends AppCompatActivity {
    Button feedback;

    ListView listView;
    String[] name={"xyz","abc"};
    String[] category={"android","php"};
    String[] date={"2/2/2019","18/5/2019"};
    String[] query={"mobile application on cafe","project to provide an plateform to experts"};
    String[] bid={"i will complete your work within 2 months","i will complete your project within 6 months"};

    /*ArrayList<BookexpertList> arraybookexpertlist;

    BookexpertAdapter adapter;*/
    SharedPreferences sp;
    ProjectAdapter adapter;
    ArrayList<ProjectList> arrayprojectlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__expert);
        getSupportActionBar().setTitle("Booking History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp=getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        listView=findViewById(R.id.book_expert_listview);
        /*arraybookexpertlist=new ArrayList<>();
        for(int i=0;i<name.length;i++){
            BookexpertList list=new BookexpertList();
            list.setName(name[i]);
            list.setCategory(category[i]);
            list.setDate(date[i]);
            list.setQuery(query[i]);
            list.setBid(bid[i]);

            arraybookexpertlist.add(list);

        }
        BookexpertAdapter adapter=new BookexpertAdapter(Book_ExpertActivity.this,arraybookexpertlist);
        listView.setAdapter(adapter);*/
        if (new ConnectionDetector(Book_ExpertActivity.this).isConnectingToInternet()) {
            new getProjectRequest().execute();
        } else {
            new ConnectionDetector(Book_ExpertActivity.this).connectiondetect();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getProjectRequest extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Book_ExpertActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("expertId","");
            hashMap.put("type","project");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getRequest.php",MakeServiceCall.POST,hashMap);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    arrayprojectlist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        ProjectList list = new ProjectList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("username"));
                        list.setQuery(jsonObject.getString("query"));
                        list.setStatus(jsonObject.getString("status"));
                        list.setDate(jsonObject.getString("created_date"));
                        arrayprojectlist.add(list);
                    }
                    adapter = new ProjectAdapter(Book_ExpertActivity.this, arrayprojectlist);
                    listView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(Book_ExpertActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
