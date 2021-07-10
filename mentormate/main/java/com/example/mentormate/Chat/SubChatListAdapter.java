package com.example.mentormate.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;

import java.util.ArrayList;

/**
 * Created by ADMIN on 1/3/2019.
 */
public class SubChatListAdapter extends RecyclerView.Adapter<SubChatListAdapter.MyViewHolder> {
    ArrayList<SubChatList> subChatLists;
    Context context;
    SharedPreferences sp;

    public SubChatListAdapter(SubChatActivity activity, ArrayList<SubChatList> subChatLists) {
        this.context = activity;
        this.subChatLists = subChatLists;
        sp = context.getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_time, message;
        LinearLayout layout, chat_bg_layout;

        public MyViewHolder(final View view) {
            super(view);
            user_time = (TextView) view.findViewById(R.id.custom_sub_chat_usertime);
            chat_bg_layout = (LinearLayout) view.findViewById(R.id.custom_sub_chat_layout_background);
            message = (TextView) view.findViewById(R.id.custom_sub_chat_usermsg);
            layout = (LinearLayout) view.findViewById(R.id.custom_sub_chat_layout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sub_chat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.message.setText(subChatLists.get(position).getMessage());

        holder.user_time.setText(subChatLists.get(position).getDate());

        if(sp.getString(ConstantSP.USERTYPE,"").equals("User")) {
            if (("U"+sp.getString(ConstantSP.USERID, "")).equals(subChatLists.get(position).getSenderId())) {
                holder.chat_bg_layout.setBackgroundResource(R.drawable.custom_chat_bg);
                holder.layout.setGravity(Gravity.END);
            } else {
                holder.chat_bg_layout.setBackgroundResource(R.drawable.custom_chat_bg_gray);
                holder.layout.setGravity(Gravity.START);
            }
        }
        else{
            if (("E"+sp.getString(ConstantSP.USERID, "")).equals(subChatLists.get(position).getSenderId())) {
                holder.chat_bg_layout.setBackgroundResource(R.drawable.custom_chat_bg);
                holder.layout.setGravity(Gravity.END);
            } else {
                holder.chat_bg_layout.setBackgroundResource(R.drawable.custom_chat_bg_gray);
                holder.layout.setGravity(Gravity.START);
            }
        }
    }

    @Override
    public int getItemCount() {
        return subChatLists.size();
    }
}