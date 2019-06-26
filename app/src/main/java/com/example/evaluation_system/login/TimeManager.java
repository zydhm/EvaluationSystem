package com.example.evaluation_system.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeManager {
    private static final String TAG = "TimeManager";
    private Context mContext;
    private Date mDate;
    private SimpleDateFormat sf;
    private SharedPreferences pref;


    public TimeManager(Context context){
        this.mContext=context;
        sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pref=mContext.getSharedPreferences("Time",Context.MODE_PRIVATE);
    }

    public void getDate(){
        mDate=new Date();
    }

    public boolean isLoginTime(){
        getDate();
        String dataStr=pref.getString("date",null);
        if(dataStr!=null){
            try {
                Date data= (Date) sf.parse(dataStr);
                int result=mDate.compareTo(data);
                if(result<0){
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public void setTime(){
        if(mDate==null){
            mDate=new Date();
        }
        Date date=new Date(mDate.getTime()+24*60*60*1000);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("date",sf.format(date));
        editor.apply();
    }

    public void clean(){
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("date","");
        editor.apply();
    }

    public Date String2Date(String dateS) throws ParseException {
        return (Date) sf.parse(dateS);
    }

}
