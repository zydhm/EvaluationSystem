package com.example.evaluation_system.main;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.database.AppDatabaseHelper;
import com.example.evaluation_system.more.student.StudentMoreFragment;
import com.example.evaluation_system.test.student.FinishedTestFragment;

import com.example.evaluation_system.test.student.UnFinishedTestFragment;
import com.example.evaluation_system.test.student.begintest.BeginTestActivity;

public class StudentMainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar bottomNavigationBar;
    StudentMoreFragment mStudentMoreFragment;
    FinishedTestFragment mFinishedTestFragment;
    UnFinishedTestFragment mUnFinishedTestFragment;
    AppDatabaseHelper mAppDatabaseHelper;


    /**
     * 初始化内容布局
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
        mAppDatabaseHelper=AppDatabaseHelper.getInstance(this);
        mUnFinishedTestFragment = new UnFinishedTestFragment();
        mFinishedTestFragment = new FinishedTestFragment();
        mStudentMoreFragment = new StudentMoreFragment();
        initViews();
        this.setDefaultFragment();
        //TODO:有考试正在进行则跳到考试界面
        if(!mAppDatabaseHelper.isTestStatusEmpty()){
            startActivity(new Intent(this, BeginTestActivity.class));
        }
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }*/

    /**
     *初始化控件
     */
    private void initViews() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_bar_unfinished, "已完成"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bar_finished, "未完成"))
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
                if (mFinishedTestFragment == null) {
                    mFinishedTestFragment = new FinishedTestFragment();
                }
                fragmentTransaction.replace(R.id.tb, mFinishedTestFragment);
                break;
            case 1:
                if (mUnFinishedTestFragment == null) {
                    mUnFinishedTestFragment = new UnFinishedTestFragment();
                }
                fragmentTransaction.replace(R.id.tb, mUnFinishedTestFragment);
                break;
            case 2:
                if (mStudentMoreFragment == null) {
                    mStudentMoreFragment = new StudentMoreFragment();
                }
                fragmentTransaction.replace(R.id.tb, mStudentMoreFragment);
                break;
            default:
                if (mUnFinishedTestFragment == null) {
                    mUnFinishedTestFragment = new UnFinishedTestFragment();
                }
                fragmentTransaction.replace(R.id.tb, mUnFinishedTestFragment);
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
        if (mUnFinishedTestFragment == null) {
            mUnFinishedTestFragment = new UnFinishedTestFragment();
        }
        fragmentTransaction.replace(R.id.tb, mUnFinishedTestFragment);
        fragmentTransaction.commit();
    }

}