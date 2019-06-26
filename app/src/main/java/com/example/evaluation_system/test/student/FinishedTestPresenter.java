package com.example.evaluation_system.test.student;


import android.util.Log;

import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.test.model.FinishedTest;
import com.example.evaluation_system.test.model.TestMessage;
import com.example.evaluation_system.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FinishedTestPresenter implements TestContract.Presenter {

    private static final String TAG = "FinishedTestPresenter";
    //MVP中的V
    private FinishedTestFragment mView;

    //RXjava截断器
    private Disposable mDisposable;

    private List<FinishedTest> mFinishedTestList;

    //开始位置
    private int mStartPosition = 0;
    //末位置
    private int mEndPosition = 10;

    //当服务端没有更多数据时
    private boolean isFinishedTestNoMore;

    //当前数据是否能加载更多
    private boolean canLoadMore;


    public FinishedTestPresenter(FinishedTestFragment view) {
        this.mView = view;
    }

    /**
     * 当进入此fragment时，进行的数据请求
     */
    @Override
    public void getInitData(Integer studentId) {
        if (mFinishedTestList != null && mFinishedTestList.size() == 0) {
            getListFromServer(studentId);
        }
        if (mFinishedTestList == null) {
            mFinishedTestList = new ArrayList<>();
            getListFromServer(studentId);
        }
    }

    /**
     * 从服务器中得到所有完成测试数据
     *
     * @return 返回UnFinishedTest序列
     */
    @Override
    public void getListFromServer(Integer studentId) {
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        api.selectFinishedTest(studentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FinishedTest>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(List<FinishedTest> finishedTests) {
                        if(finishedTests==null||finishedTests.size()==0){
//                            mView.showEmptyView();
                        }else {
                            mFinishedTestList.addAll(finishedTests);
                            mView.adapter.add(finishedTests);
//                            mView.hideLoadMore();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast("网络不好,请稍候再试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 判断数据服务器端是否还有数据，具体判断由presenter完成
     *
     * @return 还有则返回true，没有则返回false; 默认返回true
     */
    @Override
    public boolean canLoadMore() {
        return !isFinishedTestNoMore;
    }

    /**
     * 上拉加载更多数据
     */
    @Override
    public void loadMore() {
//        getListFromServer();
    }

    public void refreshData(Integer studentId){
        mFinishedTestList=null;
        mView.adapter.clear();
        getInitData(studentId);
        mView.mSwipeRefreshLayout.setRefreshing(false);
    }
}
