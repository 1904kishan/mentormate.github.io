package com.example.mentormate.Chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ADMIN on 1/3/2019.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ChatList> photosLists;
    ProgressDialog pd;
    String sUserId;
    SharedPreferences sp;
    ArrayList<ChatList> search_subAdminLists;

    public ChatListAdapter(ChatListActivity activity, ArrayList<ChatList> photosLists) {
        this.context = activity;
        this.photosLists = photosLists;
        sp = context.getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
        this.search_subAdminLists = new ArrayList<>();
        this.search_subAdminLists.addAll(photosLists);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, unread;
        CardView cardView;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.custom_chat_list_name);
            unread = (TextView) view.findViewById(R.id.custom_chat_list_unread);
            cardView = (CardView) view.findViewById(R.id.custom_chat_list_card);
            linearLayout = (LinearLayout) view.findViewById(R.id.custom_chat_list_linear);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        photosLists.clear();
        if (charText.length() == 0) {
            photosLists.addAll(search_subAdminLists);
        } else {
            for (ChatList wp : search_subAdminLists) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    photosLists.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_chat_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(photosLists.get(position).getName());

        if (photosLists.get(position).getUnread().equals("0")) {
            holder.unread.setVisibility(View.GONE);
        } else {
            holder.unread.setText(photosLists.get(position).getUnread());
            holder.unread.setVisibility(View.VISIBLE);
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSP.CHATID,photosLists.get(position).getId()).commit();
                sp.edit().putString(ConstantSP.CHATNAME,photosLists.get(position).getName()).commit();
                context.startActivity(new Intent(context,SubChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        holder.unread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSP.CHATID,photosLists.get(position).getId()).commit();
                sp.edit().putString(ConstantSP.CHATNAME,photosLists.get(position).getName()).commit();
                context.startActivity(new Intent(context,SubChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSP.CHATID,photosLists.get(position).getId()).commit();
                sp.edit().putString(ConstantSP.CHATNAME,photosLists.get(position).getName()).commit();
                context.startActivity(new Intent(context,SubChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSP.CHATID,photosLists.get(position).getId()).commit();
                sp.edit().putString(ConstantSP.CHATNAME,photosLists.get(position).getName()).commit();
                context.startActivity(new Intent(context,SubChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    @Override
    public int getItemCount() {
        return photosLists.size();
    }

}