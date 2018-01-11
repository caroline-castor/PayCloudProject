package com.android4dev.navigationview;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Carol on 31/03/2016.
 */
public class UserFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment_user,container,false);
        return v;
    }

    protected void onPostExecute(String result) {
        JSONObject mainObject;
        User currentUser = new User();
        String nome, endereco, telefone, email;
        try{
            mainObject = new JSONObject(result);
        }catch (Exception e){

        }

    }



    public String PostData(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("https://webservice-flaviojr.c9users.io/return_user");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("nome", valuse[0]));
            list.add(new BasicNameValuePair("documento",valuse[1]));
            list.add(new BasicNameValuePair("email",valuse[2]));
            list.add(new BasicNameValuePair("logradouro",valuse[3]));
            list.add(new BasicNameValuePair("telefone",valuse[4]));
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

