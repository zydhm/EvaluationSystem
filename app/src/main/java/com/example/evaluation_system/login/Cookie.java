package com.example.evaluation_system.login;

import android.content.Context;
import android.content.SharedPreferences;


public class Cookie {
    private Context mContext;
    Cookie(Context context){
        this.mContext=context;
    }
    public boolean existCookie(){
        SharedPreferences pref=mContext.getSharedPreferences("Cookie",Context.MODE_PRIVATE);
        String cookie=pref.getString("cookie",null);
        if(null==cookie){
            return false;
        }
        else return true;
    }
    public String getCookie(){
        SharedPreferences pref=mContext.getSharedPreferences("Cookie",Context.MODE_PRIVATE);
        String cookie=pref.getString("cookie",null);
        return cookie;
    }
}
