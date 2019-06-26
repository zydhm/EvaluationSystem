package com.example.evaluation_system.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evaluation_system.base.model.Student;
import com.example.evaluation_system.base.model.Teacher;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.main.StudentMainActivity;
import com.example.evaluation_system.main.TeacherMainActivity;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private Context mContext;
    private SharedPreferences pref;
    private ProgressDialog logining;

    public LoginPresenter(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences("loginMessage", Context.MODE_PRIVATE);
    }

    public void login(final String userType, String loginName, String password, final Context context, final TimeManager timeManager, final UserManager userManager) {
        if (userType.equals("student")) {//学生登录
            logining = ProgressDialog.show(mContext, "提示", "正在登陆", false, true);
            Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
            api.studentLogin(loginName, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Student>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Student user) {
                            timeManager.setTime();
                            userManager.setStudent(user);
                            userManager.setUserType(userType);
                            logining.dismiss();
                            Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                            ((Activity) mContext).startActivity(new Intent((Activity) mContext, StudentMainActivity.class));
                            ((Activity) mContext).finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            logining.dismiss();
                            Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "loginOnError: " + e.getCause());
                            //getSelfListFromServer("02a618bf-0241-11e8-bd05-00163e0ac98c");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {//老师登录
            logining = ProgressDialog.show(mContext, "提示", "正在登陆", false, true);
            Api api = HttpManager.getInstance().getApiService(Constant.BASE_TEACHER_URL);
            api.teacherLogin(loginName, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Teacher>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Teacher user) {
                            timeManager.setTime();
                            userManager.setTeacher(user);
                            userManager.setUserType(userType);
                            Log.d(TAG, "onNext: "+userType);
                            logining.dismiss();
                            Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                            ((Activity) mContext).startActivity(new Intent((Activity) mContext, TeacherMainActivity.class));
                            ((Activity) mContext).finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            logining.dismiss();
                            Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "loginOnError: " + e.getCause());
                            //getSelfListFromServer("02a618bf-0241-11e8-bd05-00163e0ac98c");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public boolean hasRememberPas(EditText editTextA, EditText editTextP) {
        boolean isRemember = pref.getBoolean("remember_PassWord", false);
        if (isRemember) {
            editTextA.setText(pref.getString("account", null));
            editTextP.setText(pref.getString("password", null));
            return true;
        }
        return false;
    }

    public boolean isRemember(String userType,CheckBox mCheckBox, EditText editTextA, EditText editTextP) {
        SharedPreferences.Editor editor = pref.edit();
        if (mCheckBox.isChecked()) {
            editor.putString("userType", userType);
            editor.putString("account", editTextA.getText().toString());
            editor.putString("password", editTextP.getText().toString());
            editor.putBoolean("remember_PassWord", true);
            editor.apply();
            return true;
        } else {
            editor.clear();
            editor.apply();
        }
        return false;
    }
}
