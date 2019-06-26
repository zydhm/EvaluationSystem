package com.example.evaluation_system.more.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseFragment;
import com.example.evaluation_system.information.InfoActivity;
import com.example.evaluation_system.login.LoginActivity;
import com.example.evaluation_system.login.TimeManager;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.teacher.testMain.TeacherTestActivity;
import com.example.evaluation_system.test.teacher.testUpload.TestUploadActivity;

import butterknife.BindView;

public class TeacherMoreFragment extends BaseFragment {
    @BindView(R.id.more_user_info)
    RelativeLayout infoRl;
    @BindView(R.id.more_test)
    RelativeLayout testRl;
    @BindView(R.id.more_logout)
    RelativeLayout logoutRl;
    UserManager mUserManager;
    TimeManager mTimeManager;



    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_teacher_more;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mUserManager=new UserManager(getContext());
        mTimeManager=new TimeManager(getContext());

        /**
         * 点击个人信息
         */
        infoRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), InfoActivity.class));
            }
        });

        /**
         * 点击试卷
         */
        testRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), TeacherTestActivity.class));
            }
        });



        /**
         * 点击注销后
         */
        logoutRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("提示")
                        .setMessage("确定要退出登录吗？")
                        .setCancelable(true)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mTimeManager.clean();
                                dialogInterface.dismiss();
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
