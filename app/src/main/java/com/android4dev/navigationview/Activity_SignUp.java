package com.android4dev.navigationview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Activity_SignUp extends AppCompatActivity {

    TextView tv_return_login;
    EditText edtLogin;
    EditText edtPass;
    EditText edtConfirmPass;
    Button btn_register;
    String login;
    String pass;
    String confirmPass;
    ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__sign_up);
        edtLogin = (EditText) findViewById(R.id.input_email);
        edtPass = (EditText) findViewById(R.id.input_password);
        edtConfirmPass = (EditText) findViewById(R.id.input_confirmPass);
        tv_return_login = (TextView) findViewById(R.id.link_login);
        tv_return_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_SignUp.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });



        btn_register = (Button) findViewById(R.id.btn_signup);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = edtLogin.getText().toString();
                pass = edtPass.getText().toString();
                confirmPass = edtConfirmPass.getText().toString();
                if(pass.equals(confirmPass)) {
                    new ExecuteTaskLogin().execute(login, pass);
                }else{
                    Toast.makeText(Activity_SignUp.this.getApplicationContext(),"Senhas não conferem",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    class ExecuteTaskLogin  extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {

            String res=PostData(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject mainObject;
            try {
                mainObject = new JSONObject(result);
                boolean typeS = mainObject.getBoolean("type");

                if(typeS) {
                    Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();
                    Intent i= new Intent(Activity_SignUp.this,LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro, tente novamente", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Servidor Indisponivel",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            System.out.println(result);
        }



        public String PostData(String[] valuse) {
            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("https://webservice-flaviojr.c9users.io/signup");
                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("email", valuse[0]));
                list.add(new BasicNameValuePair("password",valuse[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=  httpClient.execute(httpPost);
                s= readResponse(httpResponse);
            }
            catch(Exception exception)  {}
            return s;
        }
        public String readResponse(HttpResponse res) {
            InputStream is;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null) {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e) {}
            return return_text;
        }
    }


}
