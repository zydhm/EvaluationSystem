package com.example.evaluation_system.main;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.more.teacher.TeacherMoreFragment;
import com.example.evaluation_system.qustionbank.QuestionBankFragment;
import com.example.evaluation_system.studentmanage.StudentManageFragment;

public class TeacherMainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar bottomNavigationBar;

    //TODO:修改为老师相关fragment
    QuestionBankFragment mQuestionBankFragment;
    TeacherMoreFragment mTeacherMoreFragment;
    StudentManageFragment mStudentManageFragment;
//        FinishedTestFragment mFinishedTestFragment;
//        UnFinishedTestFragment mUnFinishedTestFragment;


    /**
     * 初始化内容布局
     *
     * @return
     */
    @Override
    protected View initContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        return view;
    }

    /**
     * 初始化一些设置
     */
    @Override
    protected void initOptions() {
        super.initOptions();
//        修改为老师相关fragment
        mQuestionBankFragment = new QuestionBankFragment();
        mStudentManageFragment=new StudentManageFragment();
//        mUnFinishedTestFragment = new UnFinishedTestFragment();
//        mFinishedTestFragment = new FinishedTestFragment();
        mTeacherMoreFragment = new TeacherMoreFragment();
        initViews();
        this.setDefaultFragment();
    }


/**
 * 申请权限，暂时还不严谨此处先略过
 */
/**private void requestPermissions() {
 PermissionGen
 .with(this)
 .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
 Manifest.permission.CAMERA)
 .addRequestCode(100)
 .request();
 }

 @PermissionSuccess(requestCode = 100)
 private void requestPermissionSuccess() {
 Toast.makeText(this, "permission rquest success",
 Toast.LENGTH_SHORT).show();
 }

 @PermissionFail(requestCode = 100)
 public void requestPermissionFail() {
 Toast.makeText(this, "permission request failed",
 Toast.LENGTH_SHORT).show();
 }

 @RequiresApi(api = Build.VERSION_CODES.M)
 @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
 }*/

    /**
     * 初始化控件
     */
    private void initViews() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_bar_finished, "学生"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bar_unfinished, "题库"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bar_more, "更多"))
                .setBarBackgroundColor(R.color.white)
                .setActiveColor(R.color.colorPrimary)
                .setInActiveColor(R.color.secondary_text)
                .setFirstSelectedPosition(1)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                if (mStudentManageFragment == null) {
                    mStudentManageFragment = new StudentManageFragment();
                }
                fragmentTransaction.replace(R.id.tb, mStudentManageFragment);
                break;
            case 1:
        if (mQuestionBankFragment == null) {
            mQuestionBankFragment = new QuestionBankFragment();
        }
        fragmentTransaction.replace(R.id.tb, mQuestionBankFragment);
                break;
            case 2:
        if (mTeacherMoreFragment == null) {
            mTeacherMoreFragment = new TeacherMoreFragment();
        }
        fragmentTransaction.replace(R.id.tb, mTeacherMoreFragment);
                break;
            default:
                if (mQuestionBankFragment == null) {
                    mQuestionBankFragment = new QuestionBankFragment();
                }
                fragmentTransaction.replace(R.id.tb, mQuestionBankFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void setDefaultFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mQuestionBankFragment == null) {
            mQuestionBankFragment = new QuestionBankFragment();
        }
        fragmentTransaction.replace(R.id.tb, mQuestionBankFragment);
        fragmentTransaction.commit();
    }

}
