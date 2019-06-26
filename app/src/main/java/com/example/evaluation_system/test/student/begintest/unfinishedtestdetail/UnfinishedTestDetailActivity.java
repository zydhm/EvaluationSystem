package com.example.evaluation_system.test.student.begintest.unfinishedtestdetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.login.TimeManager;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.test.student.begintest.BeginTestActivity;
import com.example.evaluation_system.utils.ToastUtil;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UnfinishedTestDetailActivity extends BaseToolbarActivity {

    private static final String TAG = "UnfinishedTestDetailAct";

    private Test mUnFinishedTest;
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
    //开始答题按钮
    @BindView(R.id.bt_testDetail_start)
    Button startBtn;

    TimeManager mTimeManager;
    String Tip = "考试包含单选、判断、多选三种题型，若考试过程中多次切到其他界面，试卷将自动提交并按作弊处理!";
    Date begin, end;
    String serveTimeS;
    Date serveTimeD;

    /**
     * 初始化布局
     *
     * @return 布局view
     */
    @Override
    protected View initContentView() {
        View view = getLayoutInflater().inflate(R.layout.activity_unfinished_test_detail, null);
        return view;
    }

    /**
     * 初始化Toolbar的标题
     *
     * @return
     */
    @Override
    protected String initToolbarTitle() {
        return "详情";
    }

    /**
     * 初始化由其他activity传过来的变量
     *
     * @param bundle 当activity在特定情况下销毁时保存的数据，重新恢复过来
     * @return
     */
    @Override
    protected boolean initArgs(Bundle bundle) {
        //得到由UnFinishedFragment传过来的test
        mUnFinishedTest = (Test) getIntent().getParcelableExtra("unFinishedTest");
        return true;
    }

    /**
     * 初始化一些设置
     */
    @Override
    protected void initOptions() {
        initViews();
        mTimeManager = new TimeManager(this);
    }

    /**
     * 初始化View
     */
    private void initViews() {
        mRootLayout.setBackgroundColor(getResources().getColor(R.color.white));
        //显示考试信息
        if (mUnFinishedTest != null) {
            ImageManager.getInstance().loadImage(this,
                    mUnFinishedTest.getTestpicture(),
                    testIv);
            testTopicTv.setText(mUnFinishedTest.getTesttopic());
            knowledgesTv.setText(mUnFinishedTest.getKnowledges());
            beginTv.setText(mUnFinishedTest.getBegin());
            endTv.setText(mUnFinishedTest.getEnd());
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取服务器时间，比较服务器时间与考试时间是否冲突
                    Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
                    api.getServerTime()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(String s) {
                                    Log.d(TAG, "onNext: ");
                                    serveTimeS = s;
                                    try {
                                        serveTimeD = mTimeManager.String2Date(serveTimeS);
                                        begin = mTimeManager.String2Date(beginTv.getText().toString());
                                        end = mTimeManager.String2Date(endTv.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (serveTimeD.compareTo(begin) < 0) {
                                        ToastUtil.shortToast("考试时间还没到呢。。");
                                    } else if (serveTimeD.compareTo(end) > 0) {
                                        ToastUtil.shortToast("时间都过了。。");
                                    } else {
                                        ToastUtil.shortToast("准备进入考试...");
                                        new AlertDialog.Builder(UnfinishedTestDetailActivity.this)
                                                .setTitle("提示")
                                                .setMessage(Tip)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //切到考试界面
                                                        Intent intent = new Intent(UnfinishedTestDetailActivity.this, BeginTestActivity.class);
                                                        intent.putExtra("testId", mUnFinishedTest.getTestid());
                                                        intent.putExtra("millisInFutureS", end.getTime() - serveTimeD.getTime());
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
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
            });
        }
    }


    @Override
    protected void initHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
