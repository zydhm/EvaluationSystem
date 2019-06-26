package com.example.evaluation_system.more.student;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.information.InfoActivity;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseFragment;
import com.example.evaluation_system.login.LoginActivity;
import com.example.evaluation_system.login.TimeManager;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.knowledge.QueryKnowledgeActivity;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentMoreFragment extends BaseFragment {

    private static String TAG="StudentMoreFragment";

    @BindView(R.id.more_user_info)
    RelativeLayout more_user_info_rl;
    @BindView(R.id.more_promotion)
    RelativeLayout more_promotion_rl;
    @BindView(R.id.more_contact)
    RelativeLayout more_contact_rl;
    @BindView(R.id.more_selfTest)
    RelativeLayout more_selfTest_rl;
    @BindView(R.id.more_logout)
    RelativeLayout more_logout_rl;
    TimeManager mTimeManager;


    public StudentMoreFragment() {
        // Required empty public constructor
    }

    //更改个人信息
    @Override
    protected void initWidget(View view) {
        super.initWidget(view);

        mTimeManager=new TimeManager(getActivity());

        more_user_info_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),InfoActivity.class));
                return;
            }
        });

        more_logout_rl.setOnClickListener(new View.OnClickListener() {
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
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
//                                getActivity().finish();
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

        more_selfTest_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), QueryKnowledgeActivity.class);
                intent.putExtra("requestCode", Constant.REQUESTKNOWLEDGEFROMSELFTEST);
                startActivity(intent);
            }
        });
    }
    

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_student_more;
    }



}
