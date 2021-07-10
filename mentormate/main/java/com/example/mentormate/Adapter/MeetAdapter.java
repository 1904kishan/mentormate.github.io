package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.Request_meetingActivity;
import com.example.mentormate.SetGet.MeetList;
import com.example.mentormate.R;

import java.util.ArrayList;

public class MeetAdapter extends BaseAdapter {
    Context context;
    ArrayList<MeetList> arraymeetlist;

    public MeetAdapter(Request_meetingActivity request_meetingActivity, ArrayList<MeetList> arraymeetlist) {
        this.context=request_meetingActivity;
        this.arraymeetlist=arraymeetlist;
    }


    @Override
    public int getCount() {
        return arraymeetlist.size();
    }

    @Override
    public Object getItem(int i) {
        return arraymeetlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_meeting_view,null);
        TextView name=view.findViewById(R.id.custom_meeting_name);
        name.setText(arraymeetlist.get(i).getName());
        return view;
    }
}
