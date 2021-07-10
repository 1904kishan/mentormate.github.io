package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

class BidAdapter extends BaseAdapter {
    Context context;
    ArrayList<BidList> arrayskilllist;
    String sExpertId,sQueryId,sType;
    SharedPreferences sp;

    public BidAdapter(BidActivity activity, ArrayList<BidList> arrayskilllist){
        this.context=activity;
        this.arrayskilllist=arrayskilllist;
        sp = context.getSharedPreferences(ConstantSP.PREF,Context.MODE_PRIVATE);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_bid,null);
        TextView bid=view.findViewById(R.id.custom_bid_bid);
        TextView name=view.findViewById(R.id.custom_bid_name);
        TextView date=view.findViewById(R.id.custom_bid_date);
        Button request=view.findViewById(R.id.custom_bid_request);
        Button book=view.findViewById(R.id.custom_bid_book);

        bid.setText(arrayskilllist.get(i).getBid());
        date.setText(arrayskilllist.get(i).getDate());
        name.setText(arrayskilllist.get(i).getName());

        if(sp.getString(ConstantSP.USERTYPE,"").equals("User")){
            request.setVisibility(View.VISIBLE);
            book.setVisibility(View.VISIBLE);
        }
        else{
            request.setVisibility(View.GONE);
            book.setVisibility(View.GONE);
        }

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType = "meeting";
                sExpertId = arrayskilllist.get(i).getExpertId();
                sQueryId = arrayskilllist.get(i).getQuery();
                if(new ConnectionDetector(context).isConnectingToInternet()){
                    new addRequest().execute();
                }
                else{
                    new ConnectionDetector(context).connectiondetect();
                }
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType = "project";
                sExpertId = arrayskilllist.get(i).getExpertId();
                sQueryId = arrayskilllist.get(i).getQuery();
                if(new ConnectionDetector(context).isConnectingToInternet()){
                    new addRequest().execute();
                }
                else{
                    new ConnectionDetector(context).connectiondetect();
                }
            }
        });

        return view;
    }

    private class addRequest extends AsyncTask<String,String,String> {

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
            hashMap.put("type",sType);
            hashMap.put("userId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("expertId",sExpertId);
            hashMap.put("queryId",sQueryId);
            hashMap.put("status","Pending");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"addRequest.php", MakeServiceCall.POST,hashMap);
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
    }

}

