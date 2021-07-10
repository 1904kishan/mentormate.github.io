package com.example.mentormate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mentormate.Activitys.Book_ExpertActivity;
import com.example.mentormate.Activitys.Manage_feedbackActivity;
import com.example.mentormate.SetGet.BookexpertList;
import com.example.mentormate.R;

import java.util.ArrayList;

public class BookexpertAdapter extends BaseAdapter {
    Button feedback;
    Context context;
    ArrayList<BookexpertList> arraybookexpertList;

    public BookexpertAdapter(Book_ExpertActivity activity, ArrayList<BookexpertList> arraybookexpertList){
        this.context=activity;
        this.arraybookexpertList=arraybookexpertList;
    }
    @Override
    public int getCount() {
        return arraybookexpertList.size();
    }

    @Override
    public Object getItem(int i) {
        return arraybookexpertList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_book_expert,null);
        TextView name=view.findViewById(R.id.Custom_bookexpert_name);
        TextView category=view.findViewById(R.id.Custom_bookexpert_category);
        TextView date=view.findViewById(R.id.Custom_bookexpert_date);
        TextView query=view.findViewById(R.id.Custom_bookexpert_query);
        TextView bid=view.findViewById(R.id.Custom_bookexpert_bid);

        name.setText(arraybookexpertList.get(i).getName());
        category.setText(arraybookexpertList.get(i).getCategory());
        date.setText(arraybookexpertList.get(i).getDate());
        query.setText("Query : "+arraybookexpertList.get(i).getQuery());
        bid.setText("Bid : "+arraybookexpertList.get(i).getBid());

        feedback=view.findViewById(R.id.Custom_bookexpert_feedback);


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, Manage_feedbackActivity.class));
                           }
        });

        return view;
    }
}
