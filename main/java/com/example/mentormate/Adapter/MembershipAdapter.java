package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.MembershipActivity;
import com.example.mentormate.SetGet.MembershipList;
import com.example.mentormate.R;

import java.util.ArrayList;

public class MembershipAdapter extends BaseAdapter {
    Context context;
    ArrayList<MembershipList> arraymembershiplist;

    public MembershipAdapter(MembershipActivity activity, ArrayList<MembershipList> arraymembershiplist){
        this.context=activity;
        this.arraymembershiplist=arraymembershiplist;
    }
    @Override
    public int getCount() {
        return arraymembershiplist.size();
    }

    @Override
    public Object getItem(int i) {
        return arraymembershiplist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_membership,null);
        TextView packages=view.findViewById(R.id.custom_membership_packages);
        TextView valid=view.findViewById(R.id.custom_membership_valid);
        TextView price=view.findViewById(R.id.custom_membership_price);
        TextView description=view.findViewById(R.id.custom_membership_description);

        packages.setText(arraymembershiplist.get(i).getPackages());
        valid.setText(arraymembershiplist.get(i).getValid());
        price.setText(arraymembershiplist.get(i).getPrice());
        description.setText(arraymembershiplist.get(i).getDescription());
        return view;
    }
}
