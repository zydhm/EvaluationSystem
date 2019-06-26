package com.example.evaluation_system.test.teacher.testMain;

import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TeacherTestPresenter {

    TeacherTestActivity mTeacherTestActivity;

    ArrayList<Test> mTestArrayList;

    public TeacherTestPresenter(TeacherTestActivity teacherTestActivity){
        mTeacherTestActivity=teacherTestActivity;
        mTestArrayList=new ArrayList<>();
    }

    public void initData(Integer teacherId){
        Api api=HttpManager.getInstance().getApiService(Constant.BASE_TEACHER_URL);
        api.selectTestByTeacherId(teacherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Test>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Test> tests) {
                        if(tests==null||tests.size()==0){
//                            mTeacherTestActivity.showEmptyView();
                        }else {
//                            mTeacherTestActivity.hideEmptyView();
                            mTestArrayList.addAll(tests);
                            mTeacherTestActivity.mRecyclerAdapter.add(tests);
//                            mTeacherTestActivity.hideLoadMore();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void refreshData(Integer teacherId){
        mTestArrayList.clear();
        mTeacherTestActivity.mRecyclerAdapter.clear();
        initData(teacherId);
        mTeacherTestActivity.mSwipeRefreshLayout.setRefreshing(false);
    }


}
