package com.example.mentormate.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentormate.Activitys.Manage_requestActivity;
import com.example.mentormate.Activitys.Request_meetingActivity;
import com.example.mentormate.AddPaymentActivity;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.MeetingList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MeetingAdapter extends BaseAdapter {
    Context context;
    ArrayList<MeetingList> arraymeetinglist;
    String sMeetingId,sMeetingStatus;
    SharedPreferences sp;
    String sAmount;

    public MeetingAdapter(FragmentActivity meetingFragment, ArrayList<MeetingList> arraymeetinglist) {
        this.context=meetingFragment;
        this.arraymeetinglist=arraymeetinglist;
        sp = context.getSharedPreferences(ConstantSP.PREF,Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return arraymeetinglist.size();
    }

    @Override
    public Object getItem(int i) {
        return arraymeetinglist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_fragment_meeting,null);
        TextView name=view.findViewById(R.id.custom_meetingfragment_name);
        TextView query=view.findViewById(R.id.custom_meetingfragment_query);
        TextView status=view.findViewById(R.id.custom_meetingfragment_status);
        TextView date=view.findViewById(R.id.custom_meetingfragment_date);
        TextView transaction=view.findViewById(R.id.custom_meetingfragment_transaction);
        TextView amount=view.findViewById(R.id.custom_meetingfragment_amount);
        Button pay=view.findViewById(R.id.custom_meetingfragment_pay);

        RelativeLayout acceptLayout = view.findViewById(R.id.custom_meetingfragment_accept_layout);
        Button accept = view.findViewById(R.id.custom_meetingfragment_accept);
        Button cancel = view.findViewById(R.id.custom_meetingfragment_cancel);


        name.setText(arraymeetinglist.get(i).getName());
        query.setText(arraymeetinglist.get(i).getQuery());
        status.setText(arraymeetinglist.get(i).getStatus());
        date.setText(arraymeetinglist.get(i).getDate());

        if(sp.getString(ConstantSP.USERTYPE,"").equals("User")){
            acceptLayout.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
            if(arraymeetinglist.get(i).getStatus().equals("Pending")){
                transaction.setVisibility(View.GONE);
                pay.setVisibility(View.GONE);
            }
            else{
                if(arraymeetinglist.get(i).getStatus().equals("Accepted")){
                    amount.setVisibility(View.VISIBLE);
                    amount.setText("Rs."+arraymeetinglist.get(i).getAmount());
                    if(arraymeetinglist.get(i).getPaymentType().equals("")){
                        transaction.setVisibility(View.GONE);
                        pay.setVisibility(View.VISIBLE);
                    }
                    else{
                        transaction.setVisibility(View.VISIBLE);
                        pay.setVisibility(View.GONE);
                        if(arraymeetinglist.get(i).getPaymentType().equals("Cash")){
                            transaction.setText("Cash");
                        }
                        else{
                            transaction.setText(arraymeetinglist.get(i).getPaymentType()+" ( "+arraymeetinglist.get(i).getTransactionId()+" )");
                        }
                    }
                }
                else{
                    transaction.setVisibility(View.GONE);
                    pay.setVisibility(View.GONE);
                }
            }
        }
        else {
            if (arraymeetinglist.get(i).getStatus().equals("Pending")) {
                acceptLayout.setVisibility(View.VISIBLE);
                status.setVisibility(View.GONE);
                transaction.setVisibility(View.GONE);
                pay.setVisibility(View.GONE);
            } else {
                acceptLayout.setVisibility(View.GONE);
                status.setVisibility(View.VISIBLE);
                if(arraymeetinglist.get(i).getStatus().equals("Accepted")){
                    if(arraymeetinglist.get(i).getPaymentType().equals("")){
                        transaction.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                    }
                    else{
                        transaction.setVisibility(View.VISIBLE);
                        pay.setVisibility(View.GONE);
                        if(arraymeetinglist.get(i).getPaymentType().equals("Cash")){
                            transaction.setText("Cash");
                        }
                        else{
                            transaction.setText(arraymeetinglist.get(i).getPaymentType()+" ( "+arraymeetinglist.get(i).getTransactionId()+" )");
                        }
                    }
                }
                else{
                    transaction.setVisibility(View.GONE);
                    pay.setVisibility(View.GONE);
                }
            }
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_meeting);
                final EditText amount = (EditText) dialog.findViewById(R.id.dialog_meeting_amount);
                Button submit = (Button) dialog.findViewById(R.id.dialog_meeting_submit);
                dialog.show();
                dialog.setCancelable(false);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(amount.getText().toString().equals("")){
                            amount.setError("Amount Required");
                        }
                        else if(amount.getText().toString().length()<2){
                            amount.setError("Valid Amount Required");
                        }
                        else{
                            sMeetingId = arraymeetinglist.get(i).getId();
                            sMeetingStatus = "Accepted";
                            sAmount = amount.getText().toString();

                            if(new ConnectionDetector(context).isConnectingToInternet()){
                                new updateRequestStatus().execute();
                            }
                            else{
                                new ConnectionDetector(context).connectiondetect();
                            }
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sMeetingId = arraymeetinglist.get(i).getId();
                sMeetingStatus = "Cancelled";
                sAmount = "";
                if(new ConnectionDetector(context).isConnectingToInternet()){
                    new updateRequestStatus().execute();
                }
                else{
                    new ConnectionDetector(context).connectiondetect();
                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSP.REQUESTID,arraymeetinglist.get(i).getId()).commit();
                sp.edit().putString(ConstantSP.AMOUNT,arraymeetinglist.get(i).getAmount()).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Online", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp.edit().putString(ConstantSP.REQUESTID,arraymeetinglist.get(i).getId()).commit();
                        sp.edit().putString(ConstantSP.AMOUNT,arraymeetinglist.get(i).getAmount()).commit();
                        context.startActivity(new Intent(context, AddPaymentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));          
                    }
                });
                builder.setNeutralButton("Cash", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new updateTransaction().execute();
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    private class updateTransaction extends AsyncTask<String,String,String> {

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
            hashMap.put("transactionId","");
            hashMap.put("paymentType","Cash");
            hashMap.put("id",sp.getString(ConstantSP.REQUESTID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"updateTransactionRequest.php", MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Request_meetingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else{
                    Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            hashMap.put("amount",sAmount);
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
