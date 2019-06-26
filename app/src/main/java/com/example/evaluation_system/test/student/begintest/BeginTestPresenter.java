package com.example.evaluation_system.test.student.begintest;


import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.evaluation_system.base.model.Answer;
import com.example.evaluation_system.base.model.QuestionCustom;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.database.AppDatabaseHelper;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.test.model.TestMessage;
import com.example.evaluation_system.utils.ParseUtil;
import com.example.evaluation_system.utils.ToastUtil;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BeginTestPresenter {

    private static final String TAG = "BeginTestPresenter";

    //数据库管理者
    AppDatabaseHelper mAppDatabaseHelper;
    BeginTestActivity mBeginTestActivity;
    //存储后端传过来的考题
    List<QuestionCustom> mQuestionCustomList;
    //存储考生答题情况
    List<Answer> mAnswerList;
    private int testQuestionId = 0;
    Map<String, RequestBody> paramsMap;
//    List<Boolean> mResultList;

    public BeginTestPresenter(BeginTestActivity beginTestActivity) {
        mBeginTestActivity = beginTestActivity;
        mAppDatabaseHelper = AppDatabaseHelper.getInstance(mBeginTestActivity);
    }

    public void initTestQuestion(final Integer testId, Integer studentId, final Long time) {
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_QUESTION_URL);
        api.selectQuestionByTestId(testId, studentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<QuestionCustom>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<QuestionCustom> questionCustoms) {
                        if (questionCustoms != null) {

                            mQuestionCustomList = questionCustoms;
                            //将试题插入数据库中
                            insertQuestionCustomsIntoDB(questionCustoms);
                            mAnswerList = new ArrayList<>();
                            mBeginTestActivity.initView(mQuestionCustomList.get(testQuestionId));
                            //初始化考试状态信息
                            ContentValues values=new ContentValues();
                            values.put("testStatus_id",1);
                            values.put("time",time);
                            values.put("test_id",testId);
                            values.put("testQuestion_id",testQuestionId);
                            values.put("reload_time",0);
                            mAppDatabaseHelper.insert("testStatus",null,values);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast("还想再考一次么？？");
                        mBeginTestActivity.finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


//将试题插入数据库中方便现场还原
    public void insertQuestionCustomsIntoDB(List<QuestionCustom> questionCustoms){
        for(int i=0;i<questionCustoms.size();i++) {
            ContentValues values = new ContentValues();
            values.put("question_id",questionCustoms.get(i).getQuestionid());
            values.put("teacher_id",questionCustoms.get(i).getTeacherid());
            values.put("knowledge_id",questionCustoms.get(i).getKnowledgeid());
            values.put("course_id",questionCustoms.get(i).getCourseid());
            values.put("question_content",questionCustoms.get(i).getQuestioncontent());
            values.put("answerA",questionCustoms.get(i).getAnswera());
            values.put("answerB",questionCustoms.get(i).getAnswerb());
            if(questionCustoms.get(i).getAnswerc()!=null){
                values.put("answerC",questionCustoms.get(i).getAnswerc());
            }
            if(questionCustoms.get(i).getAnswerd()!=null){
                values.put("answerD",questionCustoms.get(i).getAnswerd());
            }
            if(questionCustoms.get(i).getLimitedtime()!=null){
                values.put("limited_time",questionCustoms.get(i).getLimitedtime());
            }
            values.put("question_type",questionCustoms.get(i).getQuestiontype());
            if(questionCustoms.get(i).getQuestionpicture()!=null) {
                values.put("question_picture", questionCustoms.get(i).getQuestionpicture());
            }
            values.put("level",questionCustoms.get(i).getLevel());
            if(questionCustoms.get(i).getCorrectrate()!=null) {
                values.put("correct_rate", questionCustoms.get(i).getCorrectrate());
            }
            values.put("testQuestion_id",questionCustoms.get(i).getTestQuestionid());
            mAppDatabaseHelper.insert("questionCustom",null,values);
        }
    }

    public void initNextQuestion(Integer studentId, Integer testId, String answer,Long time) {
        if (mAnswerList == null) {
            mAnswerList = new ArrayList<>();
        }
        if (mAnswerList.size() < testQuestionId + 1) {
            //插入新题
            Answer answerMsg = new Answer();
            answerMsg.setStudentid(studentId);
            answerMsg.setTestid(testId);
            answerMsg.setQuestionid(mQuestionCustomList.get(testQuestionId).getQuestionid());
            answerMsg.setResult(answer.equals(mQuestionCustomList.get(testQuestionId).getCorrectanswer()));
            answerMsg.setTestQuestionid(testQuestionId+1);
            answerMsg.setStudentanswer(answer);
            mAnswerList.add(answerMsg);

            ContentValues values = new ContentValues();
            values.put("student_id", answerMsg.getStudentid());
            values.put("test_id", answerMsg.getTestid());
            values.put("question_id", answerMsg.getQuestionid());
            values.put("result", answerMsg.getResult());
            values.put("testQuestion_id", answerMsg.getTestQuestionid());
            values.put("student_answer", answerMsg.getStudentanswer());
            mAppDatabaseHelper.insert("answer", null, values);
        } else {
            //更改做过的题
            mAnswerList.get(testQuestionId).setResult(answer.equals(mQuestionCustomList.get(testQuestionId).getCorrectanswer()));
            mAnswerList.get(testQuestionId).setStudentanswer(answer);

            ContentValues values = new ContentValues();
            values.put("result", mAnswerList.get(testQuestionId).getResult());
            values.put("student_answer", mAnswerList.get(testQuestionId).getStudentanswer());
            mAppDatabaseHelper.update("answer", null, values, "student_id=? and test_id=? and question_id=?", new String[]{String.valueOf(studentId), String.valueOf(testId), String.valueOf(mQuestionCustomList.get(testQuestionId).getQuestionid())});
        }


        testQuestionId++;

        //储存当前考试状态
        ContentValues values=new ContentValues();
        values.put("time",time);
        values.put("testQuestion_id",testQuestionId);
        mAppDatabaseHelper.update("testStatus",null,values,"testStatus_id=?",new String[]{String.valueOf(1)});

        //用户点击了“提交”,进行了最后一次题目上传
        if (testQuestionId != mQuestionCustomList.size()) {
            if (testQuestionId + 1 == mQuestionCustomList.size()) {
                mBeginTestActivity.setLastView();
            }
            //还是在更改做过的题
            if (mAnswerList.size() >= testQuestionId + 1) {
                mBeginTestActivity.alterQuestion(mQuestionCustomList.get(testQuestionId), mAnswerList.get(testQuestionId));
            } else {
                mBeginTestActivity.initNewQuestion(mQuestionCustomList.get(testQuestionId));
            }
        } else {
            testQuestionId--;
        }
    }

    //用户点击“上一题”
    public void initPreQuestion(Integer studentId, Integer testId, String answer,Long time) {
        if (testQuestionId + 1 == mQuestionCustomList.size()) {
            mBeginTestActivity.cancelLastView();
        }
        if (testQuestionId == 0) {
            ToastUtil.shortToast("已经是第一道题了！");
        } else {
            if (mAnswerList == null) {
                mAnswerList = new ArrayList<>();
            }
            if (mAnswerList.size() < testQuestionId + 1) {
                //插入新题
                Answer answerMsg = new Answer();
                answerMsg.setStudentid(studentId);
                answerMsg.setTestid(testId);
                answerMsg.setQuestionid(mQuestionCustomList.get(testQuestionId).getQuestionid());
                answerMsg.setResult(answer.equals(mQuestionCustomList.get(testQuestionId).getCorrectanswer()));
                answerMsg.setTestQuestionid(testQuestionId+1);
                answerMsg.setStudentanswer(answer);
                mAnswerList.add(answerMsg);

                ContentValues values = new ContentValues();
                values.put("student_id", answerMsg.getStudentid());
                values.put("test_id", answerMsg.getTestid());
                values.put("question_id", answerMsg.getQuestionid());
                values.put("result", answerMsg.getResult());
                values.put("testQuestion_id", answerMsg.getTestQuestionid());
                values.put("student_answer", answerMsg.getStudentanswer());
                mAppDatabaseHelper.insert("answer", null, values);
            } else {
                //更改做过的题
                mAnswerList.get(testQuestionId).setResult(answer.equals(mQuestionCustomList.get(testQuestionId).getCorrectanswer()));
                mAnswerList.get(testQuestionId).setStudentanswer(answer);

                ContentValues values = new ContentValues();
                values.put("result", mAnswerList.get(testQuestionId).getResult());
                values.put("student_answer", mAnswerList.get(testQuestionId).getStudentanswer());
                mAppDatabaseHelper.update("answer", null, values, "student_id=? and test_id=? and question_id=?", new String[]{String.valueOf(studentId), String.valueOf(testId), String.valueOf(mQuestionCustomList.get(testQuestionId).getQuestionid())});
            }

            testQuestionId--;
            //存储当前考试状态
            ContentValues values=new ContentValues();
            values.put("time",time);
            values.put("testQuestion_id",testQuestionId);
            mAppDatabaseHelper.update("testStatus",null,values,"testStatus_id=?",new String[]{String.valueOf(1)});
            mBeginTestActivity.alterQuestion(mQuestionCustomList.get(testQuestionId), mAnswerList.get(testQuestionId));
        }
    }


    public void finishTest(final Integer studentId, final Integer testId, String answer,long time) {
        initNextQuestion(studentId, testId, answer,time);
        completeTestResult();
        updateTest();
    }

    //时间截止，自动补全结果
    public void completeTestResult() {
        //将余下所有题目的学生答案设为“”
        for (int i = mAnswerList.size(); i < mQuestionCustomList.size(); i++) {
            Answer answerMsg = new Answer();
            answerMsg.setStudentid(mBeginTestActivity.getStudentId());
            answerMsg.setTestid(mBeginTestActivity.getTestId());
            answerMsg.setQuestionid(mQuestionCustomList.get(i).getQuestionid());
            answerMsg.setStudentanswer("");
            answerMsg.setResult(false);
            answerMsg.setTestQuestionid(i + 1);
            mAnswerList.add(answerMsg);
        }
    }

    //计算考试总成绩
    public Integer getTestResult() {
        int correct = 0;
        for (int i = 0; i < mAnswerList.size(); i++) {
            if (mAnswerList.get(i).getResult()) {
                correct++;
            }
        }
        return correct*100 / mQuestionCustomList.size();
    }

    public void updateTest(){
        final Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        Gson gson = new Gson();
        String json = gson.toJson(mAnswerList);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        api.insertAnswers(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Answer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Answer> answers) {
                        paramsMap = new HashMap<String, RequestBody>();
                        paramsMap.put("studentid", ParseUtil.parseRequestBody("" + mBeginTestActivity.getStudentId()));
                        paramsMap.put("testid", ParseUtil.parseRequestBody("" + mBeginTestActivity.getTestId()));
                        paramsMap.put("completion", ParseUtil.parseRequestBody("" + true));
                        paramsMap.put("testresult", ParseUtil.parseRequestBody("" + getTestResult()));
                        api.updateATestMessage(paramsMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<TestMessage>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(TestMessage testMessage) {
                                        ToastUtil.shortToast("上传成功，考试结束！");
                                        //清空数据库
                                        mAppDatabaseHelper.deleteAllData();
                                        mBeginTestActivity.stopCountDownTimer();
                                        mBeginTestActivity.finish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //恢复考试现场
    public void resumeTest(){
        Log.d(TAG, "resumeTest: this is a test");
        resumeQuestionCustoms();
        resumeAnswers();
        resumeTestQuestionId();
        //用户点击了“提交”,进行了最后一次题目上传
        if (testQuestionId != mQuestionCustomList.size()) {
            if (testQuestionId + 1 == mQuestionCustomList.size()) {
                mBeginTestActivity.setLastView();
            }
            //还是在更改做过的题
            if (mAnswerList.size() >= testQuestionId + 1) {
                mBeginTestActivity.alterQuestion(mQuestionCustomList.get(testQuestionId), mAnswerList.get(testQuestionId));
            } else {
                mBeginTestActivity.initNewQuestion(mQuestionCustomList.get(testQuestionId));
            }
        } else {
            testQuestionId--;
        }

    }


    public void resumeQuestionCustoms(){
        Cursor cursor=mAppDatabaseHelper.query("questionCustom",null,null,null,null,null);
        if(cursor.moveToFirst()){
            mQuestionCustomList=new ArrayList<>();
            do{
                QuestionCustom questionCustom=new QuestionCustom();
                questionCustom.setQuestionid(cursor.getInt(cursor.getColumnIndex("question_id")));
                questionCustom.setTeacherid(cursor.getInt(cursor.getColumnIndex("teacher_id")));
                questionCustom.setKnowledgeid(cursor.getInt(cursor.getColumnIndex("knowledge_id")));
                questionCustom.setCourseid(cursor.getInt(cursor.getColumnIndex("course_id")));
                questionCustom.setQuestioncontent(cursor.getString(cursor.getColumnIndex("question_content")));
                questionCustom.setAnswera(cursor.getString(cursor.getColumnIndex("answerA")));
                questionCustom.setAnswerb(cursor.getString(cursor.getColumnIndex("answerB")));
                questionCustom.setAnswerc(cursor.getString(cursor.getColumnIndex("answerC")));
                questionCustom.setAnswerd(cursor.getString(cursor.getColumnIndex("answerD")));
                questionCustom.setCorrectanswer(cursor.getString(cursor.getColumnIndex("correct_answer")));
                questionCustom.setLimitedtime(cursor.getInt(cursor.getColumnIndex("limited_time")));
                questionCustom.setQuestiontype(cursor.getString(cursor.getColumnIndex("question_type")));
                questionCustom.setQuestionpicture(cursor.getString(cursor.getColumnIndex("question_picture")));
                questionCustom.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                questionCustom.setCorrectrate(cursor.getInt(cursor.getColumnIndex("correct_rate")));
                questionCustom.setTestQuestionid(cursor.getInt(cursor.getColumnIndex("testQuestion_id")));
                mQuestionCustomList.add(questionCustom);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void resumeAnswers(){
        Cursor cursor=mAppDatabaseHelper.query("answer",null,null,null,null,null);
        if(cursor.moveToFirst()){
            mAnswerList=new ArrayList<>();
            do{
                Answer answer=new Answer();
                answer.setStudentid(cursor.getInt(cursor.getColumnIndex("student_id")));
                answer.setTestid(cursor.getInt(cursor.getColumnIndex("test_id")));
                answer.setQuestionid(cursor.getInt(cursor.getColumnIndex("question_id")));
                answer.setTestQuestionid(cursor.getInt(cursor.getColumnIndex("testQuestion_id")));
                if(cursor.getInt(cursor.getColumnIndex("result"))==1) {
                    answer.setResult(true);
                }else{
                    answer.setResult(false);
                }
                answer.setStudentanswer(cursor.getString(cursor.getColumnIndex("student_answer")));
                mAnswerList.add(answer);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(mAnswerList==null){
            mAnswerList=new ArrayList<>();
        }
    }

    public void resumeTestQuestionId(){
        Cursor cursor=mAppDatabaseHelper.query("testStatus",null,null,null,null,null);
        if(cursor.moveToFirst()) {
            testQuestionId=cursor.getInt(cursor.getColumnIndex("testQuestion_id"));
            int reloadTime=cursor.getInt(cursor.getColumnIndex("reload_time"))+1;
            if(reloadTime>Constant.RELOADTIMELIMIT){
                ToastUtil.shortToast("您因离开考试界面次数过多，试卷正在提交..");
                completeTestResult();
                updateTest();
            }else{
                ContentValues values=new ContentValues();
                values.put("reload_time",reloadTime);
                mAppDatabaseHelper.update("testStatus",null,values,"testStatus_id=?",new String[]{String.valueOf(1)});
            }
        }
        cursor.close();
    }

}
