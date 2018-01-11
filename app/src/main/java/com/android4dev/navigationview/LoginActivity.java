package com.android4dev.navigationview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText edtLogin, edtPass;
    Button btnLogin;
    String login,pass;
    SessionManager manager;
    TextView tv_signup;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager=new SessionManager();
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        tv_signup = (TextView)findViewById(R.id.link_signup);

        edtLogin = (EditText) findViewById(R.id.editTextLogin);

        edtPass = (EditText) findViewById(R.id.editTextPass);

        btnLogin = (Button) findViewById(R.id.btnEnter);

        btnLogin.setEnabled(true);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,Activity_SignUp.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnLogin.isEnabled()) {
                    btnLogin.setEnabled(false);
                    btnLogin.setFocusable(true);
                    if (!TextUtils.isEmpty(edtLogin.getText().toString())) {
                        if (!TextUtils.isEmpty(edtPass.getText().toString())) {
                            login = edtLogin.getText().toString();
                            pass = edtPass.getText().toString();
                            spinner.setVisibility(View.VISIBLE);
                            spinner.setIndeterminate(true);
                            new ExecuteTaskLogin().execute(login, pass);
                        } else {
                            //NÃO PREENCHEU CAMPO SENHA
                            Toast.makeText(getApplicationContext(), "Senha inválida", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //NÃO PREENCHEU CAMPO LOGIN
                        Toast.makeText(getApplicationContext(), "Login inválido", Toast.LENGTH_LONG).show();
                    }
                }
                btnLogin.setEnabled(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

/*    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }*/

    /**
     * Created by Caroline on 16/03/2016.
     */
    class ExecuteTaskLogin  extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {

            String res=PostData(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject mainObject;
            User currentUser = new User();
            String login, token;
            try {
                mainObject = new JSONObject(result);
                boolean typeS = mainObject.getBoolean("type");

                if(typeS) {
                    JSONObject dataObject = mainObject.getJSONObject("data");
                    login = dataObject.getString("email");
                    token = dataObject.getString("token");
                    currentUser.setLogin(login);
                    currentUser.setToken(token);
                    manager.setPreferencesLogin(LoginActivity.this.getApplicationContext(), login);
                    manager.setPreferencesToken(LoginActivity.this.getApplicationContext(),token);
                    manager.setPreferencesStatus(LoginActivity.this.getApplication().getApplicationContext(), "1");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"Usuario ou senha inválida",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Servidor Indisponivel",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            btnLogin.setEnabled(true);
            System.out.println(result);
        }



        public String PostData(String[] valuse) {
            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("https://webservice-flaviojr.c9users.io/authenticate");
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
