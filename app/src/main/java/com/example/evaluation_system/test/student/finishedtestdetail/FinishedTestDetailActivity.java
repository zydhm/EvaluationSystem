package com.example.evaluation_system.test.student.finishedtestdetail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.FinishedTestQuestionDetailVo;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.test.model.FinishedTest;
import com.example.evaluation_system.widget.RecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FinishedTestDetailActivity extends BaseToolbarActivity {
    private static final String TAG = "FinishedTestDetailActiv";
    @BindView(R.id.rl_finished_test_detail)
    RecyclerView detailRl;
    RecyclerAdapter mRecyclerAdapter;
    FinishedTest mFinishedTest;
    ArrayList<FinishedTestQuestionDetailVo> mFinishedTestQuestionDetailVos;


    @Override
    protected View initContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_finished_test_detail,null);
    }

    @Override
    protected String initToolbarTitle() {
        return "考试详情";
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mFinishedTest=getIntent().getParcelableExtra("finishedTest");
        return true;
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        Log.d(TAG, "initOptions: DetailActivityTaskId"+getTaskId());
        if(mFinishedTest!=null){
            initData(mFinishedTest.getStudentid(),mFinishedTest.getTestid());
        }

    }


    public void initData(Integer studentId,Integer testId){
        Log.d(TAG, "initData: ");
        Api api= HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        api.selectFinishedTestQuestionDetailVo(testId,studentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<FinishedTestQuestionDetailVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<FinishedTestQuestionDetailVo> finishedTestQuestionDetailVos) {
                        if(finishedTestQuestionDetailVos!=null&&finishedTestQuestionDetailVos.size()!=0){
                            mFinishedTestQuestionDetailVos=finishedTestQuestionDetailVos;
                            initRecyclerView();
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

    public void initRecyclerView() {
        mRecyclerAdapter = new RecyclerAdapter<FinishedTestQuestionDetailVo>() {
            @Override
            protected int getItemViewType(int position, FinishedTestQuestionDetailVo finishedTestQuestionDetailVo) {
                return R.layout.child_item_self_test_result;
            }

            @Override
            protected ViewHolder<FinishedTestQuestionDetailVo> onCreateViewHolder(View root, int viewType) {
                return new FinishedTestDetailActivity.ViewHolder(root);
            }
        };
        mRecyclerAdapter.add(mFinishedTestQuestionDetailVos);
        detailRl.setLayoutManager(new LinearLayoutManager(FinishedTestDetailActivity.this,LinearLayoutManager.VERTICAL,false));
        detailRl.setAdapter(mRecyclerAdapter);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<FinishedTestQuestionDetailVo> {
        //考试结果图片
        @BindView(R.id.iv_selfTestResult_result)
        ImageView resultIv;
        //问题类型
        @BindView(R.id.tv_selfTestResult_questionType)
        TextView questionTypeTv;

        @BindView(R.id.tv_selfTestResult_testQuestionId)
        TextView testQuestionIdTv;

        //问题内容
        @BindView(R.id.tv_selfTestResult_questionContent)
        TextView questionContentTv;
        //问题图
        @BindView(R.id.iv_selfTestResult_questionPic)
        ImageView questionImg;

        @BindView(R.id.cb_selfTestResult_answerA)
        CheckBox answerACb;
        @BindView(R.id.tv_selfTestResult_answerA)
        TextView answerATv;

        @BindView(R.id.cb_selfTestResult_answerB)
        CheckBox answerBCb;
        @BindView(R.id.tv_selfTestResult_answerB)
        TextView answerBTv;

        @BindView(R.id.cb_selfTestResult_answerC)
        CheckBox answerCCb;
        @BindView(R.id.tv_selfTestResult_answerC)
        TextView answerCTv;

        @BindView(R.id.cb_selfTestResult_answerD)
        CheckBox answerDCb;
        @BindView(R.id.tv_selfTestResult_answerD)
        TextView answerDTv;

        @BindView(R.id.tv_selfTestResult_correctAnswer)
        TextView correctAnswerTv;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final FinishedTestQuestionDetailVo finishedTestQuestionDetailVo) {
            if (finishedTestQuestionDetailVo.getResult()) {
                resultIv.setImageResource(R.drawable.ic_correct);
                correctAnswerTv.setVisibility(View.GONE);
            } else {
                resultIv.setImageResource(R.drawable.ic_wrong);
                correctAnswerTv.setVisibility(View.VISIBLE);
                correctAnswerTv.setText("正确答案:"+finishedTestQuestionDetailVo.getQuestion().getCorrectanswer());
            }

            questionTypeTv.setText(finishedTestQuestionDetailVo.getQuestion().getQuestiontype());
            testQuestionIdTv.setText(""+finishedTestQuestionDetailVo.getTestQuestionid());
            questionContentTv.setText(finishedTestQuestionDetailVo.getQuestion().getQuestioncontent());
            if (finishedTestQuestionDetailVo.getQuestion().getQuestionpicture() != null && finishedTestQuestionDetailVo.getQuestion().getQuestionpicture() != "") {
                questionImg.setVisibility(View.VISIBLE);
                ImageManager.getInstance().loadImage(FinishedTestDetailActivity.this,
                        finishedTestQuestionDetailVo.getQuestion().getQuestionpicture(), questionImg);
            } else {
                questionImg.setVisibility(View.GONE);
            }

            if(finishedTestQuestionDetailVo.getStudentanswer().contains("A")){
                answerACb.setChecked(true);
            }else {
                answerACb.setChecked(false);
            }
            answerATv.setText(finishedTestQuestionDetailVo.getQuestion().getAnswera());

            if(finishedTestQuestionDetailVo.getStudentanswer().contains("B")){
                answerBCb.setChecked(true);
            }else {
                answerBCb.setChecked(false);
            }
            answerBTv.setText(finishedTestQuestionDetailVo.getQuestion().getAnswerb());

            if(finishedTestQuestionDetailVo.getQuestion().getAnswerc()!=null&&!finishedTestQuestionDetailVo.getQuestion().getAnswerc().equals(""))
            {
                answerCCb.setVisibility(View.VISIBLE);
                answerCTv.setVisibility(View.VISIBLE);
                if (finishedTestQuestionDetailVo.getStudentanswer().contains("C")) {
                    answerCCb.setChecked(true);
                } else {
                    answerCCb.setChecked(false);
                }
                answerCTv.setText(finishedTestQuestionDetailVo.getQuestion().getAnswerc());
            }else {
                answerCCb.setVisibility(View.GONE);
                answerCTv.setVisibility(View.GONE);
            }
            if(finishedTestQuestionDetailVo.getQuestion().getAnswerd()!=null&&!finishedTestQuestionDetailVo.getQuestion().getAnswerd().equals("")) {
                answerDCb.setVisibility(View.VISIBLE);
                answerDTv.setVisibility(View.VISIBLE);
                if (finishedTestQuestionDetailVo.getStudentanswer().contains("D")) {
                    answerDCb.setChecked(true);
                } else {
                    answerDCb.setChecked(false);
                }
                answerDTv.setText(finishedTestQuestionDetailVo.getQuestion().getAnswerd());
            }else{
                answerDCb.setVisibility(View.GONE);
                answerDTv.setVisibility(View.GONE);
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ": 123456789");
    }
}
