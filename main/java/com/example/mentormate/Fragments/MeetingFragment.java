package com.example.mentormate.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mentormate.Adapter.MeetingAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.MeetingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MeetingFragment extends Fragment {

    ListView listview;

    String[] name = {"abc", "xyz", "mon"};
    String[] date = {"2/9/2018", "1/1/2001", "15/12/2019"};

    MeetingAdapter adapter;
    SharedPreferences sp;

    ArrayList<MeetingList> arraymeetinglist;

    public MeetingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting, container, false);
        sp = getActivity().getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
        listview = view.findViewById(R.id.meeting_fragment_list);

        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new getMeetingRequest().execute();
        } else {
            new ConnectionDetector(getActivity()).connectiondetect();
        }
        return view;

    }

    private class getMeetingRequest extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userId","");
            hashMap.put("expertId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("type","meeting");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getRequest.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    arraymeetinglist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        MeetingList list = new MeetingList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("username"));
                        list.setQuery(jsonObject.getString("query"));
                        list.setStatus(jsonObject.getString("status"));
                        list.setDate(jsonObject.getString("created_date"));
                        list.setAmount(jsonObject.getString("amount"));
                        list.setPaymentType(jsonObject.getString("paymentType"));
                        list.setTransactionId(jsonObject.getString("transactionId"));
                        arraymeetinglist.add(list);
                    }
                    adapter = new MeetingAdapter(getActivity(), arraymeetinglist);
                    listview.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getActivity(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
