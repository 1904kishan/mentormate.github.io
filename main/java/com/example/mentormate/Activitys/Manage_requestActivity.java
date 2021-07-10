package com.example.mentormate.Activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.mentormate.Fragments.MeetingFragment;
import com.example.mentormate.Fragments.ProjectFragment;
import com.example.mentormate.R;

public class Manage_requestActivity extends AppCompatActivity {

    TabLayout tab;
    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_request);
        getSupportActionBar().setTitle("Manage Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tab=findViewById(R.id.manage_request_tab);
        pager=findViewById(R.id.manage_request_pager);

        tab.post(new Runnable() {
            @Override
            public void run() {
                tab.setupWithViewPager(pager);
            }
        });

        TabLayoutAdapter adapter=new TabLayoutAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

    }

    static class TabLayoutAdapter extends FragmentPagerAdapter {
        public TabLayoutAdapter(FragmentManager supportFragmentManager) {
           super(supportFragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Meeting";
                case 1:
                    return "Project";
            }
            return null;
        }

        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return new MeetingFragment();
                case 1:
                    return new ProjectFragment();
            }
            return null;
        }


        @Override
        public int getCount() {
            return 2;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
