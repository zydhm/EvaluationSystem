package com.example.evaluation_system.utils;

public class TimeUtil {

    public static String getDate(int year, int month, int day){
        String temp;
        if(month<10){
            temp=year+"-0"+(month+1);
        }else {
            temp=year+"-"+(month+1);
        }
        if(day<10){
            temp=temp+"-0"+day;
        }else {
            temp=temp+"-"+day;
        }
        return temp;
    }

    public static String getTime(int hour, int min){
        String temp;
        if(hour<10){
            temp="0"+hour;
        }else {
            temp=""+hour;
        }
        if(min<10){
            temp=temp+":0"+min;
        }else{
            temp=temp+":"+min;
        }
        return temp;
    }
}
