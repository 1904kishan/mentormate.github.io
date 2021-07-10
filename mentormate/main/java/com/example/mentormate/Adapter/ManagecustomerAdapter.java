package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.ManageCustomerActivity;
import com.example.mentormate.SetGet.ManagecustomerList;
import com.example.mentormate.R;

import java.util.ArrayList;

public class ManagecustomerAdapter extends BaseAdapter {
    Context context;
    ArrayList<ManagecustomerList> arrayuserdetaillist;

    public ManagecustomerAdapter(ManageCustomerActivity activity, ArrayList<ManagecustomerList> arrayuserdetaillist){
        this.context=activity;
        this.arrayuserdetaillist=arrayuserdetaillist;
    }
    @Override
    public int getCount() {
        return arrayuserdetaillist.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayuserdetaillist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_user_detail,null);
        TextView name=view.findViewById(R.id.custom_userdetail_name);
        TextView email=view.findViewById(R.id.custom_userdetail_email);
        TextView city=view.findViewById(R.id.custom_userdetail_city);
        TextView state=view.findViewById(R.id.custom_userdetail_state);
        TextView contact=view.findViewById(R.id.custom_userdetail_contact);
        TextView gender=view.findViewById(R.id.custom_userdetail_gender);

        name.setText("Name : "+arrayuserdetaillist.get(i).getName());
        email.setText("Email : "+arrayuserdetaillist.get(i).getEmail());
        city.setText("City : "+arrayuserdetaillist.get(i).getCity());
        state.setText("State : "+arrayuserdetaillist.get(i).getState());
        contact.setText("Contact : "+arrayuserdetaillist.get(i).getContact());
        gender.setText("Gender : "+arrayuserdetaillist.get(i).getGender());
        return view;
    }
}
