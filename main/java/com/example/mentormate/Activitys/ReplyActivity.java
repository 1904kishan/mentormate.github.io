package com.example.mentormate.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.mentormate.Adapter.ReplyAdapter;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ReplyList;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity {
    ListView listview;
    String[] expertmsg={"a","b","c"};

    String[] usermsg={"x","y","z"};

    ArrayList<ReplyList> arrayreplylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        getSupportActionBar().setTitle("Check Reply");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listview=findViewById(R.id.reply_listview);

        arrayreplylist=new ArrayList<>();
        for(int i=0;i<expertmsg.length;i++)
        {
            ReplyList list=new ReplyList();
            list.setExpertmsg(expertmsg[i]);
            list.setUsermsg(usermsg[i]);
            arrayreplylist.add(list);
        }
        ReplyAdapter adapter=new ReplyAdapter(ReplyActivity.this,arrayreplylist);
        listview.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
