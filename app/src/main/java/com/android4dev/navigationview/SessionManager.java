package com.android4dev.navigationview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Caroline on 25/03/2016.
 */
public class SessionManager {

    public void setPreferencesLogin(Context context, String login){
        Editor editor = context.getSharedPreferences("PayCloudSystem",context.MODE_PRIVATE).edit();
        editor.putString("login",login);
        editor.commit();
    }

    public void setPreferencesToken(Context context, String token){
        Editor editor = context.getSharedPreferences("PayCloudSystem",context.MODE_PRIVATE).edit();
        editor.putString("token",token);
        editor.commit();
    }

    public void setPreferencesStatus(Context context, String status){
        Editor editor = context.getSharedPreferences("PayCloudSystem",context.MODE_PRIVATE).edit();
        editor.putString("status", status);
        editor.commit();
    }

    public String getPreferences(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("PayCloudSystem", Context.MODE_PRIVATE);
        String position = prefs.getString("login", "");
        return position;
    }

    public String getPreferencesStatus(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("PayCloudSystem", Context.MODE_PRIVATE);
        String position = prefs.getString("status", "0");
        return position;
    }

    public String getPreferencesToken(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("PayCloudSystem",Context.MODE_PRIVATE);
        String position = prefs.getString("token", "");
        return position;
    }


}
