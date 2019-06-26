package com.example.evaluation_system.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.evaluation_system.R;
import butterknife.ButterKnife;

public abstract class BaseToolbarActivity extends AppCompatActivity {

    //toolbar
    //@BindView(R.id.toolbar_base)
    protected Toolbar mToolbar;

    //@BindView(R.id.root_layout_base)
    protected LinearLayout mRootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_toolbar);
        mRootLayout = (LinearLayout) findViewById(R.id.root_layout_base);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_base);
        if (initArgs(savedInstanceState)) {
            mRootLayout.addView(initContentView());
            ButterKnife.bind(this);
            initToolbar();
            initOptions();
        } else {
            finish();
        }
    }

    /**
     * 初始化从其他Activity传过来的参数
     * @param bundle
     * @return 参数错误则返回false，即没必要进行下一步界面的初始化，正确则返回true，默认返回true
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 进行一些初始化设置，默认为空，子类需要设置则由子类重写
     */
    protected void initOptions() {

    }

    /**
     * 设置内容布局，不同子类有不同的布局，由子类来完成
     * @return 返回一个view
     */
    protected abstract View initContentView();

    /**
     * 初始化Toolbar名称，由子类来完成
     * @return 返回toolbar的名称
     */
    protected abstract String initToolbarTitle();

    /**
     * 设置toolbar名称，此方法用于子类中toolbar名称需要变化的情况
     * @param title
     */
    protected void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    /**
     * 初始化Toolbar
     */
    protected void initToolbar() {
        mToolbar.setTitle(initToolbarTitle());
        setSupportActionBar(mToolbar);
        initHomeButton();
    }

    /**
     * 此处为空实现，默认不显示HomeButton，子类可以重写此方法来完成对HomeButton的设置
     */
    protected void initHomeButton() {

    }

    /**
     * 点击导航返回按钮时则finish当前Activity
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
