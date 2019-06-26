package com.example.evaluation_system.information;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.Student;
import com.example.evaluation_system.login.UserManager;

import java.io.File;

import butterknife.BindView;

public class InfoActivity extends BaseToolbarActivity {
    private static String TAG = "InfoActivity";
    public static final int CHOOSE_PHOTO = 2;
    @BindView(R.id.info_userName_text2Id)
    TextView info_userNameTv;
    @BindView(R.id.info_loginName_text2Id)
    TextView info_loginNameTv;
    @BindView(R.id.info_tel_editId)
    EditText info_telEv;
    @BindView(R.id.info_email_editId)
    EditText info_emailEv;
    @BindView(R.id.relative4)
    RelativeLayout classRl;
    @BindView(R.id.info_class_textId2)
    TextView info_classTv;
    //“保存”按键
    @BindView(R.id.toolbar_textView)
    TextView toolbarTv;

    private Student user;
    UserManager mUserManager;
    InformationPresenter mInformationPresenter;
//    private File file;


    @Override
    protected View initContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_info, null);
        return view;
    }

    @Override
    protected String initToolbarTitle() {
        toolbarTv.setVisibility(View.GONE);
        return "个人信息";
    }

    @Override
    protected void initOptions() {
        mUserManager = new UserManager(this);
        mInformationPresenter = new InformationPresenter(this);
        if(mUserManager.getUserType().equals("teacher")) {
            info_userNameTv.setText(mUserManager.getTeacher().getUsername());
            info_loginNameTv.setText(mUserManager.getTeacher().getLoginname());
            info_telEv.setText(mUserManager.getTeacher().getTel() + "");
            info_emailEv.setText(mUserManager.getTeacher().getEmail());
            info_loginNameTv.setHint(mUserManager.getTeacher().getLoginname());
            info_telEv.setHint(mUserManager.getTeacher().getTel());
            classRl.setVisibility(View.GONE);
        }else{
            info_userNameTv.setText(mUserManager.getStudent().getUsername());
            info_loginNameTv.setText(mUserManager.getStudent().getLoginname());
            info_telEv.setText(mUserManager.getStudent().getTel() + "");
            info_emailEv.setText(mUserManager.getStudent().getEmail());
            info_telEv.setHint(mUserManager.getStudent().getTel());
            info_classTv.setText(mUserManager.getStudent().getClassName());
        }
//        /*
//        ****************************按下图片进入相册*****************************
//         */
//        circle_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                choosePhotoFromAlbum();
//            }
//        });
        /*
         **************************按下保存发送网络请求************************
         */
        toolbarTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    user = new Student();
                }
                if (!info_emailEv.getText().toString().trim().equals("")) {
                    user.setEmail(info_emailEv.getText().toString().trim());
                }

                if (!info_telEv.getText().toString().trim().equals("")) {
                    user.setTel(info_telEv.getText().toString().trim());
                }

                mInformationPresenter.updateStudent(mUserManager.getTeacher().getUserid(), mUserManager.getTeacher().getTel(), mUserManager.getTeacher().getEmail());
            }
        });
    }

    @Override
    protected void initHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
