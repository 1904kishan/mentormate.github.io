package com.example.mentormate.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.mentormate.Adapter.ManageComplaintAdapter;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ManageComplaintList;

import java.util.ArrayList;

public class Manage_complaintActivity extends AppCompatActivity {

    ListView listView;

    String[] name={"xyz","abc"};
    String[] category={"user","expert"};
    String[] date={"1/1/2020","5/9/2019"};
    String[] sub={"technical issue","technical issue"};
    String[] message={"i am not able to post any query","i am not able to reply to some customer"};

    ManageComplaintAdapter adapter;

    ArrayList<ManageComplaintList> arraymanagecomplaintlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_complaint);
        getSupportActionBar().setTitle("Manage Complaint");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=findViewById(R.id.manage_complaint_list);
        arraymanagecomplaintlist=new ArrayList<>();
        for(int i=0;i<name.length;i++){
            ManageComplaintList list=new ManageComplaintList();
            list.setName(name[i]);
            list.setCategory(category[i]);
            list.setDate(date[i]);
            list.setSub(sub[i]);
            list.setMessage(message[i]);

            arraymanagecomplaintlist.add(list);

        }
        ManageComplaintAdapter adapter=new ManageComplaintAdapter(Manage_complaintActivity.this,arraymanagecomplaintlist);
        listView.setAdapter(adapter);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
