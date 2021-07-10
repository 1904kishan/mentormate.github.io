package com.example.mentormate.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login, signup;
    SharedPreferences sp;

    //sp.edit().clear().commit();
    //Intent Code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.login_signup_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("")) {
                    email.setError("Email Required");
                } else if (password.getText().toString().equals("")) {
                    password.setError("Password Required");
                } else {
                    if(email.getText().toString().equals("admin@gmail.com")&&password.getText().toString().equals("admin")){
                        sp.edit().putString(ConstantSP.USERTYPE,"Admin").commit();
                        sp.edit().putString(ConstantSP.NAME,"Admin").commit();
                        sp.edit().putString(ConstantSP.EMAIL,"admin@gmail.com").commit();
                        sp.edit().putString(ConstantSP.CITY,"Ahmedabad").commit();
                        sp.edit().putString(ConstantSP.STATE,"Gujarat").commit();
                        sp.edit().putString(ConstantSP.CONTACT_NO,"9876543210").commit();
                        sp.edit().putString(ConstantSP.PASSWORD,"admin").commit();
                        sp.edit().putString(ConstantSP.GENDER,"Female").commit();
                        sp.edit().putString(ConstantSP.EXPERIENCE,"").commit();
                        sp.edit().putString(ConstantSP.SKILL,"").commit();
                        sp.edit().putString(ConstantSP.DOCUMENT,"").commit();
                        startActivity(new Intent(LoginActivity.this,Admin_profileActivity.class));
                    }
                    else {
                        if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                            new logindata().execute();
                        } else {
                            new ConnectionDetector(LoginActivity.this).connectiondetect();
                        }
                    }


                   /* Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    clearData();*/
                }
            }
        });

    }

    private void clearData() {
        email.setText("");
        password.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private class logindata extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hs = new HashMap<>();
            hs.put("email", email.getText().toString());
            hs.put("password", password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "login.php", MakeServiceCall.POST, hs);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equalsIgnoreCase("True")) {
                    JSONArray array = object.getJSONArray("response");
                    Toast.makeText(LoginActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject = array.getJSONObject(i);
                        if (jsonobject.getString("userType").equals("User")) {
                            sp.edit().putString(ConstantSP.USERTYPE, jsonobject.getString("userType")).commit();
                            sp.edit().putString(ConstantSP.USERID, jsonobject.getString("id")).commit();
                            sp.edit().putString(ConstantSP.NAME, jsonobject.getString("name")).commit();
                            sp.edit().putString(ConstantSP.EMAIL, jsonobject.getString("email")).commit();
                            sp.edit().putString(ConstantSP.CITY, jsonobject.getString("city")).commit();
                            sp.edit().putString(ConstantSP.STATE, jsonobject.getString("state")).commit();
                            sp.edit().putString(ConstantSP.CONTACT_NO, jsonobject.getString("contact_no")).commit();
                            sp.edit().putString(ConstantSP.PASSWORD, jsonobject.getString("password")).commit();
                            sp.edit().putString(ConstantSP.GENDER, jsonobject.getString("gender")).commit();
                            sp.edit().putString(ConstantSP.EXPERIENCE, "").commit();
                            sp.edit().putString(ConstantSP.SKILL, "").commit();
                            sp.edit().putString(ConstantSP.DOCUMENT, "").commit();
                            startActivity(new Intent(LoginActivity.this, User_profileActivity.class));
                        } else if (jsonobject.getString("userType").equals("Expert")) {
                            sp.edit().putString(ConstantSP.USERTYPE, jsonobject.getString("userType")).commit();
                            sp.edit().putString(ConstantSP.USERID, jsonobject.getString("id")).commit();
                            sp.edit().putString(ConstantSP.NAME, jsonobject.getString("name")).commit();
                            sp.edit().putString(ConstantSP.EMAIL, jsonobject.getString("email")).commit();
                            sp.edit().putString(ConstantSP.CITY, jsonobject.getString("city")).commit();
                            sp.edit().putString(ConstantSP.STATE, jsonobject.getString("state")).commit();
                            sp.edit().putString(ConstantSP.CONTACT_NO, jsonobject.getString("contact_no")).commit();
                            sp.edit().putString(ConstantSP.PASSWORD, jsonobject.getString("password")).commit();
                            sp.edit().putString(ConstantSP.GENDER, jsonobject.getString("gender")).commit();
                            sp.edit().putString(ConstantSP.EXPERIENCE, jsonobject.getString("experience")).commit();
                            sp.edit().putString(ConstantSP.SKILL, jsonobject.getString("skill")).commit();
                            sp.edit().putString(ConstantSP.DOCUMENT, jsonobject.getString("document")).commit();
                            startActivity(new Intent(LoginActivity.this, Expert_profileActivity.class));
                        }

                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

}
