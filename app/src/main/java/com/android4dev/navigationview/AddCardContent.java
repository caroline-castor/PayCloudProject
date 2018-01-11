package com.android4dev.navigationview;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * Created by Carol on 21/12/2015.
 */

public class AddCardContent extends android.support.v4.app.Fragment {
    String token, cardName, numberCard, csvCard, validCard;
    SessionManager sessionManager;
    EditText edtCardName;
    EditText edtNumberCard;
    EditText edtCsvCard;
    EditText edtValidCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_add_card,container,false);
        edtCardName = (EditText) v.findViewById(R.id.edtNicknameCard);
        edtNumberCard = (EditText) v.findViewById(R.id.edtNumberCard);
        edtCsvCard = (EditText) v.findViewById(R.id.edtCsc);
        edtValidCard = (EditText) v.findViewById(R.id.edtValidData);
        Button btnSaveCard = (Button) v.findViewById(R.id.btnSaveCard);
        sessionManager = new SessionManager();

        token = sessionManager.getPreferencesToken(AddCardContent.this.getActivity().getApplication().getApplicationContext());


        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardName = edtCardName.getText().toString();
                numberCard = edtNumberCard.getText().toString();
                csvCard = edtCsvCard.getText().toString();
                validCard = edtValidCard.getText().toString();

                new ExecuteTaskLogin().execute(token,cardName);
            }
        });
        return v;
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
                    Toast.makeText(AddCardContent.this.getActivity().getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(AddCardContent.this.getActivity().getApplicationContext(), "Ocorreu um erro, tente novamente", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(AddCardContent.this.getActivity().getApplicationContext(),"Servidor Indisponivel",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            System.out.println(result);
        }



        public String PostData(String[] valuse) {
            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("https://webservice-flaviojr.c9users.io/register_card");
                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("token", valuse[0]));
                list.add(new BasicNameValuePair("cardName", valuse[1]));
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
