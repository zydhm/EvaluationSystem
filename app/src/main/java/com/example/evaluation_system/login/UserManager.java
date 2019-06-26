package com.example.evaluation_system.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.evaluation_system.base.model.Student;
import com.example.evaluation_system.base.model.Teacher;


public class UserManager {
    private static String TAG = "UserManager";
    public static volatile Student sStudent;
    public static volatile Teacher sTeacher;
    public static volatile String sUserType;
    private Context mContext;
    private SharedPreferences pref;

    public UserManager(Context context) {
        mContext = context;
        pref = mContext.getSharedPreferences("loginMessage", Context.MODE_PRIVATE);
    }

    public void setUserType(String userType){
        sUserType=userType;
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
    }

    public String getUserType(){
        if(sUserType==null){
            sUserType=pref.getString("userType",null);
        }
        return sUserType;
    }

    public Student getStudent() {
        pref = mContext.getSharedPreferences("student", Context.MODE_PRIVATE);
        if(sStudent==null){
            sStudent=new Student();
            sStudent.setUserid(pref.getInt("userId", 0));
            sStudent.setUsername(pref.getString("userName", null));
            sStudent.setLoginname(pref.getString("loginName",null));
            sStudent.setPassword(pref.getString("password",null));
            sStudent.setClassid(pref.getInt("classId",0));
            sStudent.setTel(pref.getString("tel", null));
            sStudent.setEmail(pref.getString("email", null));
            sStudent.setCreateDate(pref.getString("create_date",null));
            sStudent.setVersion(pref.getString("version",null));
            sStudent.setClassName(pref.getString("className",null));
        }
        return sStudent;
    }

    public void setStudent(Student student) {
        pref = mContext.getSharedPreferences("student", Context.MODE_PRIVATE);
        sStudent = student;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("userId", student.getUserid());
        editor.putString("userName", student.getUsername());
        editor.putString("tel", student.getTel());
        editor.putString("email", student.getEmail());
        editor.putString("loginName", student.getLoginname());
        editor.putString("password", student.getPassword());
        editor.putInt("classId", student.getClassid());
        editor.putString("create_date", student.getCreateDate());
        editor.putString("version", student.getVersion());
        editor.putString("className",student.getClassName());
        editor.apply();
    }

    public Teacher getTeacher() {
        pref = mContext.getSharedPreferences("teacher", Context.MODE_PRIVATE);
        if(sTeacher==null){
            sTeacher=new Teacher();
            sTeacher.setUserid(pref.getInt("userId", 0));
            sTeacher.setUsername(pref.getString("userName", null));
            sTeacher.setLoginname(pref.getString("loginName",null));
            sTeacher.setPassword(pref.getString("password",null));
            sTeacher.setTel(pref.getString("tel", null));
            sTeacher.setEmail(pref.getString("email", null));
            sTeacher.setCreateDate(pref.getString("create_date",null));
            sTeacher.setVersion(pref.getString("version",null));
        }
        return sTeacher;
    }

    public void setTeacher(Teacher teacher) {
        pref = mContext.getSharedPreferences("teacher", Context.MODE_PRIVATE);
        sTeacher = teacher;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("userId", teacher.getUserid());
        editor.putString("userName", teacher.getUsername());
        editor.putString("tel", teacher.getTel());
        editor.putString("email", teacher.getEmail());
        editor.putString("loginName", teacher.getLoginname());
        editor.putString("password", teacher.getPassword());
        editor.putString("create_date", teacher.getCreateDate());
        editor.putString("version", teacher.getVersion());
        editor.apply();
    }

}
