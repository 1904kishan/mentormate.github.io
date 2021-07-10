package com.example.mentormate.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.mentormate.Adapter.MembershipAdapter;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.MembershipList;

import java.util.ArrayList;

public class MembershipActivity extends AppCompatActivity {

    ListView listView;

    String[] packages={"Silver","Gold"};
    String[] valid={"6 Months","12 Months"};
    String[] price={"Rs.3000","Rs.5000"};
    String[] description={"silver","gold"};

    MembershipAdapter adapter;

    ArrayList<MembershipList>arraymembershiplist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        getSupportActionBar().setTitle("Membership");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        listView=findViewById(R.id.membership_list);
        arraymembershiplist=new ArrayList<>();
        for(int i=0;i<packages.length;i++)
        {
            MembershipList list=new MembershipList();
            list.setPackages(packages[i]);
            list.setValid(valid[i]);
            list.setPrice(price[i]);
            list.setDescription(description[i]);

            arraymembershiplist.add(list);
        }
        MembershipAdapter adapter=new MembershipAdapter(MembershipActivity.this,arraymembershiplist);
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
