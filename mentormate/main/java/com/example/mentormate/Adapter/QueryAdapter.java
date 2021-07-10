package com.example.mentormate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mentormate.Activitys.BidActivity;
import com.example.mentormate.Activitys.My_queriesActivity;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.QueryList;

import java.util.ArrayList;

public class QueryAdapter extends BaseAdapter {

    Context context;
    ArrayList<QueryList> arrayquerylist;
    SharedPreferences sp;
    String sExpertId;

    public QueryAdapter(My_queriesActivity my_queriesActivity, ArrayList<QueryList> arrayquerylist) {
        this.context = my_queriesActivity;
        this.arrayquerylist = arrayquerylist;
        sp = context.getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return arrayquerylist.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayquerylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_query_view, null);
        TextView date = view.findViewById(R.id.custom_Query_view_date);
        TextView name = view.findViewById(R.id.custom_Query_view_name);
        TextView query = view.findViewById(R.id.custom_Query_view_query);
        TextView bidCount = view.findViewById(R.id.custom_Query_view_bid_count);
        Button bid = view.findViewById(R.id.custom_Query_view_bid);
        Button request = view.findViewById(R.id.custom_Query_request);
        bidCount.setText("Bid : " + arrayquerylist.get(i).getBid());
        date.setText(arrayquerylist.get(i).getDate());
        name.setText(arrayquerylist.get(i).getName());
        query.setText(arrayquerylist.get(i).getQuery());

        bidCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSP.QUERYID, arrayquerylist.get(i).getId()).commit();
                context.startActivity(new Intent(context, BidActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        if (sp.getString(ConstantSP.QUERYUSER, "").equals("user")) {
            bid.setVisibility(View.GONE);
            request.setVisibility(View.GONE);
        } else {
            bid.setVisibility(View.VISIBLE);
            request.setVisibility(View.GONE);
        }

        /*request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sExpertId = arrayquerylist.get(i).getId();
                if(new ConnectionDetector(context).isConnectingToInternet()){
                    new addRequest().execute();
                }
                else{
                    new ConnectionDetector(context).connectiondetect();
                }
            }
        });*/

        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSP.QUERYID, arrayquerylist.get(i).getId()).commit();
                context.startActivity(new Intent(context, BidActivity.class));
            }
        });


        return view;
    }

    /*private class addRequest extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("type","meeting");
            hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("expertId",sExpertId);
            hashMap.put("status","Pending");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"addRequest.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
