package com.example.evaluation_system.test.teacher.testMain.testdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.base.model.TestDetailCustom;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.test.teacher.testMain.testdetail.adapter.QuestionAdapter;


import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestDetailActivity extends BaseToolbarActivity {
    private Test mTest;
    //考试图
    @BindView(R.id.iv_testDetail_testPic)
    ImageView testIv;
    //考试主题
    @BindView(R.id.tv_testDetail_testTopic)
    TextView testTopicTv;
    //考试包含知识点
    @BindView(R.id.tv_testDetail_knowledges)
    TextView knowledgesTv;
    //考试开始时间
    @BindView(R.id.tv_testDetail_begin)
    TextView beginTv;
    //考试结束时间
    @BindView(R.id.tv_testDetail_end)
    TextView endTv;
    @BindView(R.id.tv_testDetail_class)
    TextView classTv;
    @BindView(R.id.elv_questions)
    ExpandableListView questionsElv;

    TestDetailCustom mTestDetailCustom;
    QuestionAdapter mQuestionAdapter;

    @Override
    protected View initContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_test_detail,null);
    }

    @Override
    protected String initToolbarTitle() {
        return "考试详情";
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mTest=getIntent().getParcelableExtra("test");
        return true;
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        if(mTest!=null){
            ImageManager.getInstance().loadImage(this,
                    mTest.getTestpicture(),
                    testIv);
            testTopicTv.setText(mTest.getTesttopic());
            knowledgesTv.setText(mTest.getKnowledges());
            beginTv.setText(mTest.getBegin());
            endTv.setText(mTest.getEnd());
            classTv.setText(mTest.getClasses());
            initQuestions(mTest.getTestid());
        }
    }

    public void initQuestions(Integer testId){
        Api api= HttpManager.getInstance().getApiService(Constant.BASE_TEACHER_URL);
        api.selectATestDetailCustomByTestId(testId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TestDetailCustom>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TestDetailCustom testDetailCustom) {
                        mTestDetailCustom=testDetailCustom;
                        initQuestionExpandListView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initQuestionExpandListView(){
        mQuestionAdapter=new QuestionAdapter();
        if(mTestDetailCustom!=null){
            mQuestionAdapter.setTestDetailCustom(mTestDetailCustom);
        }
        questionsElv.setAdapter(mQuestionAdapter);
    }

    /**
     * 设置toolbar返回键是否显示与可用
     */
    @Override
    protected void initHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
