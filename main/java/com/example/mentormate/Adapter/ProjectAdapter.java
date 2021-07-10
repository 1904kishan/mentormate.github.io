package com.example.mentormate.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentormate.Activitys.Manage_requestActivity;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.SetGet.ProjectList;
import com.example.mentormate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectAdapter extends BaseAdapter {
    Context context;
    ArrayList<ProjectList> arrayprojectlist;
    String sMeetingId,sMeetingStatus;
    SharedPreferences sp;

    public ProjectAdapter(FragmentActivity ProjectFragment, ArrayList<ProjectList> arrayprojectlist){
        this.context=ProjectFragment;
        this.arrayprojectlist=arrayprojectlist;
        sp = context.getSharedPreferences(ConstantSP.PREF,Context.MODE_PRIVATE);
    }
    @Override
    public int getCount() {
        return arrayprojectlist.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayprojectlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.projectrequest_custom,null);
        TextView name=view.findViewById(R.id.projectrequest_custom_name);
        TextView query=view.findViewById(R.id.projectrequest_custom_sub);
        TextView status=view.findViewById(R.id.projectrequest_custom_status);
        TextView date=view.findViewById(R.id.projectrequest_custom_date);
        RelativeLayout acceptLayout = view.findViewById(R.id.custom_projectrequest_accept_layout);
        Button accept = view.findViewById(R.id.projectrequest_custom_accept);
        Button cancel = view.findViewById(R.id.projectrequest_custom_cancel);


        name.setText(arrayprojectlist.get(i).getName());
        query.setText(arrayprojectlist.get(i).getQuery());
        status.setText(arrayprojectlist.get(i).getStatus());
        date.setText(arrayprojectlist.get(i).getDate());

        if(sp.getString(ConstantSP.USERTYPE,"").equals("User")){
            acceptLayout.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
        }
        else {
            if (arrayprojectlist.get(i).getStatus().equals("Pending")) {
                acceptLayout.setVisibility(View.VISIBLE);
                status.setVisibility(View.GONE);
            } else {
                acceptLayout.setVisibility(View.GONE);
                status.setVisibility(View.VISIBLE);
            }
        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sMeetingId = arrayprojectlist.get(i).getId();
                sMeetingStatus = "Accepted";
                if(new ConnectionDetector(context).isConnectingToInternet()){
                    new updateRequestStatus().execute();
                }
                else{
                    new ConnectionDetector(context).connectiondetect();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sMeetingId = arrayprojectlist.get(i).getId();
                sMeetingStatus = "Cancelled";
                if(new ConnectionDetector(context).isConnectingToInternet()){
                    new updateRequestStatus().execute();
                }
                else{
                    new ConnectionDetector(context).connectiondetect();
                }
            }
        });
        return view;
    }

    private class updateRequestStatus extends AsyncTask<String,String,String> {

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
            hashMap.put("status",sMeetingStatus);
            hashMap.put("id",sMeetingId);
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"updateRequest.php", MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Manage_requestActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
