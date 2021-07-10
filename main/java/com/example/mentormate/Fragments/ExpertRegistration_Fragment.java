package com.example.mentormate.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentormate.Activitys.LoginActivity;
import com.example.mentormate.ConnectionDetector;
import com.example.mentormate.ConstantSP;
import com.example.mentormate.MakeServiceCall;
import com.example.mentormate.R;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpertRegistration_Fragment extends Fragment {

    Spinner city, state;
    EditText name, email, contact, password, experience;
    RadioGroup rg;
    Button submit;
    Spinner skill;

    TextView selectIv;
    ImageView iv;

    String[] cityArray = {"Ahmedabad", "Anand", "Vadodara"};
    String[] stateArray = {"Gujarat"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String sSelectedCity, sSelectedState, sGender;

    SharedPreferences sp;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    Cursor c = null;

    ProgressDialog pd;

        ArrayList<String> skillArray;
        String sSkill;

    public ExpertRegistration_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_expert_registration_, container, false);

        selectIv = view.findViewById(R.id.expert_reg_doc);
        iv = view.findViewById(R.id.expert_reg_doc_iv);

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (Permission()) {
                        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);
                    } else {
                        Permissioncall();
                    }
                } else {
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);
                }
            }
        });

        name = view.findViewById(R.id.expert_reg_name);
        email = view.findViewById(R.id.expert_reg_email);
        contact = view.findViewById(R.id.expert_reg_cno);
        password = view.findViewById(R.id.expert_reg_password);
        city = view.findViewById(R.id.expert_reg_city);
        state = view.findViewById(R.id.expert_reg_state);
        rg = view.findViewById(R.id.expert_reg_rg);
        submit = view.findViewById(R.id.expert_reg_button);
        experience = view.findViewById(R.id.expert_reg_experience);
        skill = view.findViewById(R.id.expert_reg_skills);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = view.findViewById(id);
                sGender = rb.getText().toString();
            }
        });


        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_checked, cityArray);
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

        ArrayAdapter stateAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_checked, stateArray);
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

        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sSkill = skillArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
            new getSkill().execute();
        }
        else{
            new ConnectionDetector(getActivity()).connectiondetect();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().length() < 10 || contact.getText().toString().length() > 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (experience.getText().toString().equals("")) {
                    experience.setError("Experience Required");
                } else {
                    /*if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                        new expertSignupData().execute();
                    } else {
                        new ConnectionDetector(getActivity()).connectiondetect();
                    }*/

                    uploadMultipart();

                   /* Toast.makeText(getActivity(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    clearData();*/
                }
            }
        });

        return view;


    }

    private String getImage(Uri uri) {
        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().managedQuery(uri, s_array, null, null, null);
            int id = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (c.moveToFirst()) {
                do {
                    path = c.getString(id);
                }
                while (c.moveToNext());
                c.close();
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

    private void uploadMultipart() {
        String path = getImage(filePath);
        if (!path.equals("")) {
            try {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                new MultipartUploadRequest(getActivity(), ConstantSP.URL + "expertsignup.php")
                        .addParameter("name", name.getText().toString())
                        .addParameter("email", email.getText().toString())
                        .addParameter("city", sSelectedCity)
                        .addParameter("state", sSelectedState)
                        .addParameter("contact_no", contact.getText().toString())
                        .addParameter("password", password.getText().toString())
                        .addParameter("gender", sGender)
                        .addParameter("experience", experience.getText().toString())
                        .addParameter("skill", sSkill)
                        .addFileToUpload(path, "file")
                        .setMaxRetries(2)
                        .startUpload();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        clearData();
                    }
                }, 2500);
            } catch (Exception exc) {
                Toast.makeText(getActivity(), "Registration Unsuccessfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please Select Ceritificate", Toast.LENGTH_SHORT).show();
            /*if (new ConnectionDetector(SignupActivity.this).isConnectingToInternet()) {
                pd = new ProgressDialog(SignupActivity.this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                addData();
            } else {
                Toast.makeText(SignupActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                //new ConnectionDetector(SignupActivity.this).connectiondetect();
            }*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            //getPath(filePath);
            if (!filePath.equals("")) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    iv.setImageBitmap(bitmap);
                    iv.setImageURI(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
        } else {
            //Toast.makeText(LoginActivity.this, "Image Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean Permission() {
        int permissiocode = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissiocode == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void Permissioncall() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "write external store..", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void clearData() {
        name.setText("");
        email.setText("");
        contact.setText("");
        password.setText("");
        experience.setText("");
    }


    private class expertSignupData extends AsyncTask<String, String, String> {
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
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", name.getText().toString());
            hashMap.put("email", email.getText().toString());
            hashMap.put("city", sSelectedCity);
            hashMap.put("state", sSelectedState);
            hashMap.put("contact_no", contact.getText().toString());
            hashMap.put("password", password.getText().toString());
            hashMap.put("gender", sGender);
            hashMap.put("experience", experience.getText().toString());
            hashMap.put("skill", sSkill);
            hashMap.put("document", "");
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL + "expertsignup.php", MakeServiceCall.POST, hashMap);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equals("True")) {
                    Toast.makeText(getActivity(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    clearData();
                } else {
                    Toast.makeText(getActivity(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class getSkill extends AsyncTask<String,String,String> {

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
            return new MakeServiceCall().MakeServiceCall(ConstantSP.URL+"getExpertise.php",MakeServiceCall.POST,new HashMap<String, String>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equals("True")){
                    JSONArray array = object.getJSONArray("response");
                    skillArray = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        skillArray.add(jsonObject.getString("name"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,skillArray);
                    skill.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
