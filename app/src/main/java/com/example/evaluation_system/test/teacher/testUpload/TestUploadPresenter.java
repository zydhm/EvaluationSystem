package com.example.evaluation_system.test.teacher.testUpload;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.MainThread;

import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.utils.ToastUtil;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TestUploadPresenter {
    Context mContext;
    private ProgressDialog upLoading;
    public TestUploadPresenter(Context context){
        mContext=context;
    }

    public void uploadATest(Map<String, RequestBody> paramsMap, MultipartBody.Part testPic){
        Api api= HttpManager.getInstance().getApiService(Constant.BASE_TEST_URL);
        upLoading=ProgressDialog.show(mContext, "提示", "正在上传", false, true);
        api.insertATest(paramsMap,testPic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Test>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Test test) {
                        upLoading.dismiss();
                        ToastUtil.shortToast("上传成功！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        upLoading.dismiss();
                        ToastUtil.shortToast("上传失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
