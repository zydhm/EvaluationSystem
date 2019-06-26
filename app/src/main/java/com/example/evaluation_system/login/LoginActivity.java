package com.example.evaluation_system.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.Teacher;
import com.example.evaluation_system.main.StudentMainActivity;
import com.example.evaluation_system.main.TeacherMainActivity;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.roleSelect)
    RadioGroup mRadioGroup;
    @BindView(R.id.student)
    RadioButton btnStudent;
    @BindView(R.id.teacher)
    RadioButton btnTeacher;
    @BindView(R.id.checkbox)
    CheckBox mCheckBox;
    @BindView(R.id.btn_login)
    Button btnLogin;
//    @BindView(R.id.btn_register)
//    Button btnRegister;
    @BindView(R.id.Login_EditText01)
    EditText editTextA;
    @BindView(R.id.Login_EditText02)
    EditText editTextP;
    LoginPresenter loginPresenter;
    TimeManager mTimeManager;
    UserManager mUserManager;
    String userType="teacher";



    @Override
    protected View initContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_login,null);
        return view;
    }

    @Override
    protected void initOptions() {
        loginPresenter = new LoginPresenter(this);
        mTimeManager = new TimeManager(this);
        mUserManager=new UserManager(this);
        if (mTimeManager.isLoginTime()) {//在登录时间范围内则免登陆
            if(mUserManager.getUserType().equals("teacher")) {
                startActivity(new Intent(this, TeacherMainActivity.class));
                finish();
            }else{
                startActivity(new Intent(this, StudentMainActivity.class));
                finish();
            }
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.student:userType="student";break;
                    case R.id.teacher:userType="teacher";break;
                }
            }
        });
        loginPresenter.hasRememberPas(editTextA, editTextP);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.isRemember(userType,mCheckBox,editTextA, editTextP);
                loginPresenter.login(userType,editTextA.getText().toString(), editTextP.getText().toString(), LoginActivity.this, mTimeManager,mUserManager);
            }
        });
    }

}
