package com.example.evaluation_system.more.student.selftest;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.base.model.Question;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.login.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SelfTestActivity extends BaseActivity {
    @BindView(R.id.tv_selfTest_questionType)
    TextView questionTypeTv;
    @BindView(R.id.tv_selfTest_testQuestionId)
    TextView testQuestionIdTv;
    @BindView(R.id.tv_selfTest_questionContent)
    TextView questionContentTv;
    @BindView(R.id.iv_selfTest_questionPic)
    ImageView questionIv;
    @BindView(R.id.rl_selfTest_answerA)
    RelativeLayout answerARl;
    @BindView(R.id.rl_selfTest_answerB)
    RelativeLayout answerBRl;
    @BindView(R.id.rl_selfTest_answerC)
    RelativeLayout answerCRl;
    @BindView(R.id.rl_selfTest_answerD)
    RelativeLayout answerDRl;
    @BindView(R.id.cb_selfTest_answerA)
    CheckBox answerACb;
    @BindView(R.id.cb_selfTest_answerB)
    CheckBox answerBCb;
    @BindView(R.id.cb_selfTest_answerC)
    CheckBox answerCCb;
    @BindView(R.id.cb_selfTest_answerD)
    CheckBox answerDCb;
    @BindView(R.id.tv_selfTest_answerA)
    TextView answerATv;
    @BindView(R.id.tv_selfTest_answerB)
    TextView answerBTv;
    @BindView(R.id.tv_selfTest_answerC)
    TextView answerCTv;
    @BindView(R.id.tv_selfTest_answerD)
    TextView answerDTv;
    @BindView(R.id.tv_selfTest_next)
    TextView nextTv;


    ArrayList<Knowledge> mKnowledgeList;
    SelfTestPresenter mSelfTestPresenter;
    UserManager mUserManager;

    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_self_test,null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        mKnowledgeList=getIntent().getParcelableArrayListExtra("result");
        mSelfTestPresenter=new SelfTestPresenter(this,mKnowledgeList);
        mUserManager=new UserManager(this);
        mSelfTestPresenter.initSelfTest();
        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelfTestPresenter.initNextSelfTestQuestion(getAnswer());
            }
        });

    }

    public void initSelfTestView(Question question,Integer testQuestionId){
        questionTypeTv.setText(question.getQuestiontype()+"题");
        if(question.getQuestiontype().equals("多选")){
            setMulCheck();
        }else {
            setJudgeOrSingle();
        }
        testQuestionIdTv.setText("第"+testQuestionId+"题");
        questionContentTv.setText(question.getQuestioncontent());
        if(question.getQuestionpicture()!=null) {
            questionIv.setVisibility(View.VISIBLE);
            ImageManager.getInstance().loadImage(this,
                    question.getQuestionpicture(),
                    questionIv);
        }else{
            questionIv.setVisibility(View.GONE);
        }
        answerATv.setText(question.getAnswera());
        answerBTv.setText(question.getAnswerb());
        if(question.getAnswerc()!=null) {
            answerCTv.setText(question.getAnswerc());
            answerCRl.setVisibility(View.VISIBLE);
            answerCCb.setVisibility(View.VISIBLE);
        }else{
            answerCRl.setVisibility(View.GONE);
            answerCCb.setVisibility(View.GONE);
        }

        if(question.getAnswerd()!=null) {
            answerDTv.setText(question.getAnswerc());
            answerDTv.setText(question.getAnswerc());
            answerDRl.setVisibility(View.VISIBLE);
            answerDCb.setVisibility(View.VISIBLE);
        }else{
            answerDRl.setVisibility(View.GONE);
            answerDCb.setVisibility(View.GONE);
        }
        answerACb.setChecked(false);
        answerBCb.setChecked(false);
        answerCCb.setChecked(false);
        answerDCb.setChecked(false);
    }

    //设置多选逻辑
    public void setMulCheck(){
        answerARl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerACb.toggle();
            }
        });

        answerBRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerBCb.toggle();
            }
        });

        answerCRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerCCb.toggle();
            }
        });

        answerDRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerDCb.toggle();
            }
        });
    }

    //设置判断或单选题选择逻辑
    public void setJudgeOrSingle(){
        answerARl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerACb.setChecked(true);
                answerBCb.setChecked(false);
                answerCCb.setChecked(false);
                answerDCb.setChecked(false);
            }
        });
        answerBRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerBCb.setChecked(true);
                answerACb.setChecked(false);
                answerCCb.setChecked(false);
                answerDCb.setChecked(false);
            }
        });
        answerCRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerCCb.setChecked(true);
                answerACb.setChecked(false);
                answerBCb.setChecked(false);
                answerDCb.setChecked(false);
            }
        });
        answerDRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerDCb.setChecked(true);
                answerBCb.setChecked(false);
                answerCCb.setChecked(false);
                answerACb.setChecked(false);
            }
        });
    }

    //获取考生答案
    //获取考生填写的答案
    public String getAnswer(){
        String answer="";
        if(answerACb.isChecked()){
            answer+="A,";
        }
        if(answerBCb.isChecked()){
            answer+="B,";
        }
        if(answerCCb.isChecked()){
            answer+="C,";
        }
        if(answerDCb.isChecked()){
            answer+="D,";
        }
        if(!answer.equals("")){
            answer=answer.substring(0,answer.length()-1);
        }
        return answer;
    }

    public Integer getStudentId(){
        return mUserManager.getStudent().getUserid();
    }
}
