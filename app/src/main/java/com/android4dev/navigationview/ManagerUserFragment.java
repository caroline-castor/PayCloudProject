package com.android4dev.navigationview;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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


/**
 * Created by Carol on 31/03/2016.
 */
public class ManagerUserFragment extends android.support.v4.app.Fragment {

    TextView tvName, tvLogin,tvLogradouro,tvPhone,tvDocumento, tvNumero, tvBairro;
    EditText tvNameEditForm;
    ToggleButton tvNameEdit;
    String name, login, logradouro, phone, documento, token, email, numero, bairro;
    SessionManager sessionManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_manager_user,container,false);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvNameEditForm = (EditText) v.findViewById(R.id.tvName);
        tvNameEdit = (ToggleButton) v.findViewById(R.id.tgglBtn_name);
        makeEditable(false,tvNameEditForm);
        tvNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeEditable(true, tvNameEditForm);
            }
        });

        tvNameEditForm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    name = tvNameEdit.getText().toString();
                    makeEditable(false,tvNameEditForm);
                }
            }
        });

        tvLogin= (TextView) v.findViewById(R.id.tvLogin);
        tvPhone= (TextView) v.findViewById(R.id.tvPhone);
        tvLogradouro= (TextView) v.findViewById(R.id.tvLogradouro);
        tvDocumento= (TextView) v.findViewById(R.id.tvDocumento);
        tvNumero = (TextView)v.findViewById(R.id.tvNumero);
        tvBairro = (TextView)v.findViewById(R.id.tvBairro);
        tvLogin = (TextView) v.findViewById(R.id.tvEmail);
        sessionManager = new SessionManager();
        token = sessionManager.getPreferencesToken(ManagerUserFragment.this.getActivity().getApplicationContext());
        email = sessionManager.getPreferences(ManagerUserFragment.this.getActivity().getApplicationContext());

        new ExecuteTaskLogin().execute(token,email);

        return v;

    }

    private void makeEditable(boolean isEditable,EditText et){
        if(isEditable){
            et.setFocusable(true);
            et.setEnabled(true);
            et.setClickable(true);
            et.setFocusableInTouchMode(true);

        }else{
            et.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            et.setFocusable(false);
            et.setClickable(false);
            et.setFocusableInTouchMode(false);
            et.setEnabled(false);
            et.setKeyListener(null);
        }
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
                    Toast.makeText(ManagerUserFragment.this.getActivity().getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();
                    JSONObject dataObject = mainObject.getJSONObject("data");
                    login = dataObject.getString("email");
                    logradouro=dataObject.getString("logradouro");
                    phone = dataObject.getString("celular");
                    documento = dataObject.getString("documento");
                    name = dataObject.getString("nome");
                    numero = dataObject.getString("numero");
                    bairro = dataObject.getString("bairro");

                    tvName.setText(name);
                    tvDocumento.setText(documento);
                    tvPhone.setText(phone);
                    tvLogradouro.setText(logradouro);
                    tvLogin.setText(login);
                    tvBairro.setText(bairro);
                    tvNumero.setText(numero);


                }else{
                    Toast.makeText(ManagerUserFragment.this.getActivity().getApplicationContext(), "Ocorreu um erro, tente novamente", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(ManagerUserFragment.this.getActivity().getApplicationContext(),"Servidor Indisponivel",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            System.out.println(result);
        }



        public String PostData(String[] valuse) {
            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("https://webservice-flaviojr.c9users.io/return_user");
                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("token", valuse[0]));
                list.add(new BasicNameValuePair("email", valuse[1]));
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
