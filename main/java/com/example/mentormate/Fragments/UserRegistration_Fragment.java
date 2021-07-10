package com.example.mentormate.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mentormate.Activitys.LoginActivity;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserRegistration_Fragment extends Fragment {

    Spinner city,state;
    EditText name,email,contact,password;
    RadioGroup rg;
    Button submit;

    String[] cityArray = {"Ahmedabad","Anand","Vadodara"};
    String[] stateArray = {"Gujarat"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String sSelectedCity,sSelectedState,sGender;

    public UserRegistration_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_user_registration_, container, false);

        name = view.findViewById(R.id.user_reg_name);
        email = view.findViewById(R.id.user_reg_email);
        contact =view.findViewById(R.id.user_reg_cno);
        password = view.findViewById(R.id.user_reg_password);
        city = view.findViewById(R.id.user_reg_city);
        state = view.findViewById(R.id.user_reg_state);
        rg = view.findViewById(R.id.user_reg_rg);
        submit = view.findViewById(R.id.user_reg_button);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = view.findViewById(id);
                sGender = rb.getText().toString();
            }
        });

        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_checked,cityArray);
        city.setAdapter(cityAdapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sSelectedCity = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter stateAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_checked,stateArray);
        state.setAdapter(stateAdapter);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sSelectedState = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    name.setError("Name Required");
                }
                else if(email.getText().toString().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().length()<10||contact.getText().toString().length()>10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(rg.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
                        new userSignupData().execute();
                    }
                    else{
                        new ConnectionDetector(getActivity()).connectiondetect();
                    }
                    /*Toast.makeText(getActivity(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    clearData();*/
                }
            }
        });


   return view;
   
    }

    private void clearData() {
        name.setText("");
        email.setText("");
        contact.setText("");
        password.setText("");
    }

    private class userSignupData extends AsyncTask<String,String,String> {

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
            hashMap.put("name",name.getText().toString());
            hashMap.put("email",email.getText().toString());
            hashMap.put("city",sSelectedCity);
            hashMap.put("state",sSelectedState);
            hashMap.put("contact_no",contact.getText().toString());
            hashMap.put("password",password.getText().toString());
            hashMap.put("gender",sGender);
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"usersignup.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    Toast.makeText(getActivity(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    clearData();
                }
                else{
                    Toast.makeText(getActivity(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



   /*  @Override
   public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
        }
        return super.onOptionsItemSelected(item);
    }*/

}
