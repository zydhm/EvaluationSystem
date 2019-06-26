package com.example.evaluation_system.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    private  final String TAG = this.toString();

    protected static Context mContext;
    //根部据view
    protected View mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            //初始化当前的根布局，但是不在创建时就添加到container中，所以传入false
            mRoot = inflater.inflate(getContentLayoutId(), container, false);
            initWidget(mRoot);
        } else {
            if (mRoot.getParent() != null) {
                //如果mRoot回收不及时，父布局不为空时，就将其从父布局中移除，从而绑定到当前的父布局中
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        ButterKnife.bind(this, mRoot);
        Log.e(TAG, "onCreateView: ");
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //将initData放在View创建成功后，防止初始化一些界面相关的数据时造成空指针，但要牢记在这个方法中不要new 对象
        //防止replace后重复创建对象，以造成内存吃紧，大的对象最好是直接在构造函数中创建，或者是在业务逻辑中确认不会
        // 重复创建可以在里面创建
        Log.e(TAG, "onViewCreated: ");
        initData();
    }

    /**
     * 绑定Activity，在onCreateView前调用
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
        if (getActivity() != null) {
            mContext = getActivity();
        }
        Log.e(TAG, "onAttach: ");
    }

    /**
     * 得到内容布局id，由子类来完成
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     * @param view
     */
    protected void initWidget(View view) {
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化一些数据，但要牢记在这个方法中不要new 对象
     //防止replace后重复创建对象，以造成内存吃紧，大的对象最好是直接在构造函数中创建，或者是在业务逻辑中确认不会
     // 重复创建可以在里面创建
     */
    protected void initData() {

    }

    /**
     * 在onAttch中初始化Bundle数据
     * @param bundle
     */
    protected void initArgs(Bundle bundle) {

    }


}
