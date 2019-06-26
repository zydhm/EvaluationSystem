package com.example.evaluation_system.test.student.begintest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.Answer;
import com.example.evaluation_system.base.model.QuestionCustom;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.login.TimeManager;
import com.example.evaluation_system.widget.MyCountDownTimer;

import java.text.ParseException;

import butterknife.BindView;

public class BeginTestActivity extends BaseActivity {
    private static final String TAG = "BeginTestActivity";
    @BindView(R.id.tv_beginTest_questionType)
    TextView questionTypeTv;
    @BindView(R.id.tv_beginTest_testQuestionId)
    TextView testQuestionIdTv;
    @BindView(R.id.tv_beginTest_questionContent)
    TextView questionContentTv;
    @BindView(R.id.iv_beginTest_questionPic)
    ImageView questionIv;
    @BindView(R.id.rl_beginTest_answerA)
    RelativeLayout answerARl;
    @BindView(R.id.rl_beginTest_answerB)
    RelativeLayout answerBRl;
    @BindView(R.id.rl_beginTest_answerC)
    RelativeLayout answerCRl;
    @BindView(R.id.rl_beginTest_answerD)
    RelativeLayout answerDRl;
    @BindView(R.id.cb_beginTest_answerA)
    CheckBox answerACb;
    @BindView(R.id.cb_beginTest_answerB)
    CheckBox answerBCb;
    @BindView(R.id.cb_beginTest_answerC)
    CheckBox answerCCb;
    @BindView(R.id.cb_beginTest_answerD)
    CheckBox answerDCb;
    @BindView(R.id.tv_beginTest_answerA)
    TextView answerATv;
    @BindView(R.id.tv_beginTest_answerB)
    TextView answerBTv;
    @BindView(R.id.tv_beginTest_answerC)
    TextView answerCTv;
    @BindView(R.id.tv_beginTest_answerD)
    TextView answerDTv;
    @BindView(R.id.tv_beginTest_timer)
    TextView timerTv;
    @BindView(R.id.tv_beginTest_next)
    TextView nextTv;
    @BindView(R.id.tv_beginTest_pre)
    TextView preTv;


    Integer testId;
    com.example.evaluation_system.login.UserManager mUserManager;
    BeginTestPresenter mBeginTestPresenter;
    MyCountDownTimer mCountDownTimer;
    TimeManager mTimeManager;
    long millisInFuture, hour, min, sec, lMapper;


    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_begin_test, null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        mUserManager = new com.example.evaluation_system.login.UserManager(this);
        mBeginTestPresenter = new BeginTestPresenter(this);
        mTimeManager = new TimeManager(this);
        if (mBeginTestPresenter.mAppDatabaseHelper.isTestStatusEmpty()) {
            testId = getIntent().getIntExtra("testId", -1);
            millisInFuture = getIntent().getLongExtra("millisInFutureS", -1);
            mBeginTestPresenter.initTestQuestion(testId, mUserManager.getStudent().getUserid(), millisInFuture);
            //设置计时器
            try {
                setCountDownTimer();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交试卷
                if (nextTv.getText().toString().equals("提交")) {
                    new AlertDialog.Builder(BeginTestActivity.this)
                            .setTitle("提示")
                            .setMessage("确定要提交试卷吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mBeginTestPresenter.finishTest(mUserManager.getStudent().getUserid(), testId, getAnswer(), mCountDownTimer.getlMapper());
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    mBeginTestPresenter.initNextQuestion(mUserManager.getStudent().getUserid(), testId, getAnswer(), mCountDownTimer.getlMapper());
                }
            }
        });

        preTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBeginTestPresenter.initPreQuestion(mUserManager.getStudent().getUserid(), testId, getAnswer(), mCountDownTimer.getlMapper());
            }
        });
    }



    //初始化做题的界面(开始考试，显示第一题)
    public void initView(QuestionCustom questionCustom) {
        questionTypeTv.setText(questionCustom.getQuestiontype() + "题");
        if (questionCustom.getQuestiontype().equals("多选")) {
            setMulCheck();
        } else {
            setJudgeOrSingle();
        }
        testQuestionIdTv.setText("第" + questionCustom.getTestQuestionid() + "题");
        questionContentTv.setText(questionCustom.getQuestioncontent());
        if (questionCustom.getQuestionpicture() != null) {
            questionIv.setVisibility(View.VISIBLE);
            ImageManager.getInstance().loadImage(this,
                    questionCustom.getQuestionpicture(),
                    questionIv);
        } else {
            questionIv.setVisibility(View.GONE);
        }
        answerATv.setText(questionCustom.getAnswera());
        answerBTv.setText(questionCustom.getAnswerb());
        if (questionCustom.getAnswerc() != null) {
            answerCTv.setText(questionCustom.getAnswerc());
            answerCRl.setVisibility(View.VISIBLE);
            answerCCb.setVisibility(View.VISIBLE);
        } else {
            answerCRl.setVisibility(View.GONE);
            answerCCb.setVisibility(View.GONE);
        }

        if (questionCustom.getAnswerd() != null) {
            answerDTv.setText(questionCustom.getAnswerd());
            answerDRl.setVisibility(View.VISIBLE);
            answerDCb.setVisibility(View.VISIBLE);
        } else {
            answerDRl.setVisibility(View.GONE);
            answerDCb.setVisibility(View.GONE);
        }

        answerACb.setChecked(false);
        answerBCb.setChecked(false);
        answerCCb.setChecked(false);
        answerDCb.setChecked(false);

    }


    //显示做过的题
    public void alterQuestion(QuestionCustom questionCustom, Answer answerMsg) {
        initView(questionCustom);

        //还原学生选项
        if (answerMsg.getStudentanswer().contains("A")) {
            answerACb.setChecked(true);
        }
        if (answerMsg.getStudentanswer().contains("B")) {
            answerBCb.setChecked(true);
        }
        if (answerMsg.getStudentanswer().contains("C")) {
            answerCCb.setChecked(true);
        }
        if (answerMsg.getStudentanswer().contains("D")) {
            answerDCb.setChecked(true);
        }

    }

    //显示未做过的题
    public void initNewQuestion(QuestionCustom questionCustom) {
        initView(questionCustom);
    }


    //获取考生填写的答案
    public String getAnswer() {
        String answer = "";
        if (answerACb.isChecked()) {
            answer += "A,";
        }
        if (answerBCb.isChecked()) {
            answer += "B,";
        }
        if (answerCCb.isChecked()) {
            answer += "C,";
        }
        if (answerDCb.isChecked()) {
            answer += "D,";
        }
        if (!answer.equals("")) {
            answer = answer.substring(0, answer.length() - 1);
        }
        return answer;
    }

    //设置多选逻辑
    public void setMulCheck() {
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
    public void setJudgeOrSingle() {
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

    public void setCountDownTimer() throws ParseException {
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer=null;
        }

        mCountDownTimer = new MyCountDownTimer(millisInFuture, 1000) {

            @Override
            public long getlMapper() {
                return lMapper;
            }

            @Override
            public void onTick(long l) {
                if (!BeginTestActivity.this.isFinishing()) {
                    lMapper = l;
                    hour = l / (1000 * 60 * 60);
                    min = (l - hour * 1000 * 60 * 60) / (1000 * 60);
                    sec = (l - hour * 1000 * 60 * 60 - min * 1000 * 60) / 1000;
                    timerTv.setText(hour + "时" + min + "分" + sec + "秒");
                }else{//活动结束应终止计时避免内存泄漏
                    this.cancel();
                }
            }


            @Override
            public void onFinish() {
                //倒计时结束提交考试结果
                mBeginTestPresenter.finishTest(mUserManager.getStudent().getUserid(), testId, getAnswer(), getlMapper());
            }
        };
        mCountDownTimer.start();
    }

    public void stopCountDownTimer() {
        mCountDownTimer.cancel();
    }


    //最后一题时将“下一题”显示为“提交”
    public void setLastView() {
        nextTv.setText("提交");
    }

    //最后一题时点击上一题显示为“下一题”
    public void cancelLastView() {
        nextTv.setText("下一题");
    }

    //获取学生id
    public Integer getStudentId() {
        return mUserManager.getStudent().getUserid();
    }

    //获取考试id
    public Integer getTestId() {
        return testId;
    }


    //用户按下返回键需确认是否离开考试
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(BeginTestActivity.this)
                .setTitle("提示")
                .setMessage("要弃考了吗大兄弟？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBeginTestPresenter.finishTest(mUserManager.getStudent().getUserid(), testId, getAnswer(), mCountDownTimer.getlMapper());
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mBeginTestPresenter.mAppDatabaseHelper.isTestStatusEmpty()) {
            Cursor cursor = mBeginTestPresenter.mAppDatabaseHelper.query("testStatus", null, null, null, null, null);
            cursor.moveToFirst();
            millisInFuture = cursor.getLong(cursor.getColumnIndex("time"));
            testId = cursor.getInt(cursor.getColumnIndex("test_id"));
            mBeginTestPresenter.resumeTest();
            //设置计时器
            try {
                setCountDownTimer();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
