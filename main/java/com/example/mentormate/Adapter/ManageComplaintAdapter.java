package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.Manage_complaintActivity;
import com.example.mentormate.SetGet.ManageComplaintList;
import com.example.mentormate.R;

import java.util.ArrayList;

public class ManageComplaintAdapter extends BaseAdapter {

    Context context;
    ArrayList<ManageComplaintList> arraymanagecomplaintlist;

    public ManageComplaintAdapter(Manage_complaintActivity activity, ArrayList<ManageComplaintList> arraymanagecomplaintlist){
        this.context=activity;
       this.arraymanagecomplaintlist=arraymanagecomplaintlist;
    }
    @Override
    public int getCount() {
        return arraymanagecomplaintlist.size();
    }

    @Override
    public Object getItem(int i) {
        return arraymanagecomplaintlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_manage_complaint,null);
        TextView name=view.findViewById(R.id.custom_manage_complaint_name);
        TextView category=view.findViewById(R.id.custom_manage_complaint_category);
        TextView date=view.findViewById(R.id.custom_manage_complaint_date);
        TextView sub=view.findViewById(R.id.custom_manage_complaint_sub);
        TextView message=view.findViewById(R.id.custom_manage_complaint_message);

        name.setText(arraymanagecomplaintlist.get(i).getName());
        category.setText(arraymanagecomplaintlist.get(i).getCategory());
        date.setText(arraymanagecomplaintlist.get(i).getDate());
        sub.setText("Subject : "+arraymanagecomplaintlist.get(i).getSub());
        message.setText("Message : "+arraymanagecomplaintlist.get(i).getMessage());



        return view;
    }
}
