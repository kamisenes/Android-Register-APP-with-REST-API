package com.example.enes.logandreg;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class register extends AppCompatActivity {

    EditText edtName, edtSurname,edtMail,edtPassword;
    Button  buttonRegister;
    private static  String urls="Your URL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edtName = (EditText) findViewById(R.id.edtname);
        edtSurname = (EditText) findViewById(R.id.edtsurname);
        edtMail = (EditText) findViewById(R.id.editregemail);
        edtPassword = (EditText) findViewById(R.id.edtregpass);
        buttonRegister = (Button) findViewById(R.id.bregister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRequest().execute();
            }
        });
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        String strName=edtName.getText().toString();
        String strSurname=edtSurname.getText().toString();
        String strEmail=edtMail.getText().toString();
        String strPassword=edtPassword.getText().toString();

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{



                URL url = new URL("http://185.190.16.29/uyelik/register.php");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("name",strName );
                postDataParams.put("surname", strSurname);
                postDataParams.put("email", strEmail);
                postDataParams.put("password", strPassword);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("False : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result+"Congrulations",
                    Toast.LENGTH_LONG).show();


        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
