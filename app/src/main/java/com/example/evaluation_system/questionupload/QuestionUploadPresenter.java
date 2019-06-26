package com.example.evaluation_system.questionupload;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.evaluation_system.base.model.Question;
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

public class QuestionUploadPresenter {
    private static final String TAG = "QuestionUploadPresenter";
    private ProgressDialog upLoading;

    Context mContext;

    public QuestionUploadPresenter(Context context) {
        mContext = context;
    }


    public void uploadQuestion(Map questionParams, MultipartBody.Part questionPic) {
        upLoading = ProgressDialog.show(mContext, "提示", "正在上传", false, true);
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_QUESTION_URL);
        api.uploadAQuestion(questionParams, questionPic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Question>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Question question) {
                        upLoading.dismiss();
                        ToastUtil.shortToast("上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast("上传失败");
                        upLoading.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateQuestion(Map questionParams, MultipartBody.Part questionPic){
        upLoading = ProgressDialog.show(mContext, "提示", "正在上传", false, true);
        Api api=HttpManager.getInstance().getApiService(Constant.BASE_QUESTION_URL);
        api.updateAQuestion(questionParams,questionPic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Question>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Question question) {
                        upLoading.dismiss();
                        ToastUtil.shortToast("上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast("上传失败");
                        upLoading.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
