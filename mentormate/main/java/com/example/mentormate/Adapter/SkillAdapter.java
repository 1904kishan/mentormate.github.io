package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.View_skillActivity;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.SkillList;

import java.util.ArrayList;

public class SkillAdapter extends BaseAdapter {
    Context context;
    ArrayList<SkillList> arrayskilllist;

    public SkillAdapter(View_skillActivity activity, ArrayList<SkillList> arrayskilllist){
        this.context=activity;
        this.arrayskilllist=arrayskilllist;
    }
    @Override
    public int getCount() {
        return arrayskilllist.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayskilllist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.viewskill_custom,null);
        TextView skill=view.findViewById(R.id.viewskill_custom_text);
        skill.setText(arrayskilllist.get(i).getSkill());
        return view;
    }
}
