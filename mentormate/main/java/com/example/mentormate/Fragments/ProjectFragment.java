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

import com.example.mentormate.Adapter.ProjectAdapter;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;
import com.example.mentormate.SetGet.ProjectList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProjectFragment extends Fragment {
    public ProjectFragment() {
        // Required empty public constructor
    }

    ListView listView;
    String[] name = {"abc", "xyz"};
    String[] date = {"1/1/2019", "5/8/2019"};
    String[] sub = {"Book My Expert", "Coffee Hub"};
    String[] status = {"I will complete your work within 3 months", "i will complete your work but it will cost rs.8000"};

    SharedPreferences sp;
    ProjectAdapter adapter;
    ArrayList<ProjectList> arrayprojectlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        sp = getActivity().getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
        listView = view.findViewById(R.id.project_list);

        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new getProjectRequest().execute();
        } else {
            new ConnectionDetector(getActivity()).connectiondetect();
        }
        return view;
    }

    private class getProjectRequest extends AsyncTask<String, String, String> {

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
            hashMap.put("expertId",sp.getString(ConstantSP.USERID,""));
            hashMap.put("userId","");
            hashMap.put("type","project");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getRequest.php",MakeServiceCall.POST,hashMap);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    arrayprojectlist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        ProjectList list = new ProjectList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("username"));
                        list.setQuery(jsonObject.getString("query"));
                        list.setStatus(jsonObject.getString("status"));
                        list.setDate(jsonObject.getString("created_date"));
                        arrayprojectlist.add(list);
                    }
                    adapter = new ProjectAdapter(getActivity(), arrayprojectlist);
                    listView.setAdapter(adapter);
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
