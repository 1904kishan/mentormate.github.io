package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.Manage_feedbackActivity;
import com.example.mentormate.SetGet.ManageFeedbackList;
import com.example.mentormate.R;

import java.util.ArrayList;

public class ManageFeedbackAdapter extends BaseAdapter {
    Context context;
    ArrayList<ManageFeedbackList> arraymanagefeedbacklist;

    public ManageFeedbackAdapter(Manage_feedbackActivity activity, ArrayList<ManageFeedbackList> arraymanagefeedbacklist){
        this.context=activity;
        this.arraymanagefeedbacklist=arraymanagefeedbacklist;
    }
    @Override
    public int getCount() {
        return arraymanagefeedbacklist.size();
    }

    @Override
    public Object getItem(int i) {
        return arraymanagefeedbacklist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_manage_feedback,null);
        TextView name=view.findViewById(R.id.custom_manage_feedback_name);
        TextView email=view.findViewById(R.id.custom_manage_feedback_email);
        TextView date=view.findViewById(R.id.custom_manage_feedback_date);
        TextView sub=view.findViewById(R.id.custom_manage_feedback_sub);
        TextView feedback=view.findViewById(R.id.custom_manage_feedback_feed);

        name.setText(arraymanagefeedbacklist.get(i).getName());
        email.setText(arraymanagefeedbacklist.get(i).getEmail());
        date.setText(arraymanagefeedbacklist.get(i).getDate());
        sub.setText("Skill : "+arraymanagefeedbacklist.get(i).getSub());
        feedback.setText("Feedback : "+arraymanagefeedbacklist.get(i).getFeedback());

        return view;
    }
}
