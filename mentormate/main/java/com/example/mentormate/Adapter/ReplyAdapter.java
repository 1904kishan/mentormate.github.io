package com.example.mentormate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mentormate.Activitys.ReplyActivity;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ReplyList;

import java.util.ArrayList;

public class ReplyAdapter extends BaseAdapter {

    Context context;
    ArrayList<ReplyList> arrayreplylist;

    public ReplyAdapter(ReplyActivity replyActivity, ArrayList<ReplyList> arrayreplylist) {
        this.context = replyActivity;
        this.arrayreplylist = arrayreplylist;
    }

    @Override
    public int getCount() {
            return arrayreplylist.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayreplylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_reply_view,null);
        TextView expertmsg=view.findViewById(R.id.reply_view_expertmsg);
        TextView usermsg=view.findViewById(R.id.reply_view_usertmsg);

        expertmsg.setText(arrayreplylist.get(i).getExpertmsg());
        usermsg.setText(arrayreplylist.get(i).getUsermsg());
        return view;
    }
}
