package com.example.evaluation_system.more.student.selftest;

import android.content.Intent;
import android.util.Log;

import com.example.evaluation_system.base.model.SelfTestResultVo;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.base.model.Question;
import com.example.evaluation_system.base.model.SelfTest;
import com.example.evaluation_system.base.model.Sq;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.more.student.selftest.selftestresult.SelfTestResultActivity;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.utils.ParseUtil;
import com.example.evaluation_system.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class SelfTestPresenter {
    private static final String TAG = "SelfTestPresenter";

    SelfTestActivity mSelfTestActivity;
    ArrayList<Knowledge> mKnowledgeList;

    //知识点编号
    int knowledgeIndex = 0;

    //测试题在试卷中的编号
    int selfTestQuestionId = 0;

    //在某个知识点中，测试题的编号
    int selfTestQuestionIndex = 0;

    String knowledges = "";

    int nextLevel = 2;

    //储存每个知识点考题
    ArrayList<ArrayList<Question>> mQuestionLists;
    ArrayList<ArrayList<Sq>> mSqLists;
    SelfTest mSelfTest;

    Map<String, RequestBody> sqParamsMap;


    public SelfTestPresenter(SelfTestActivity selfTestActivity, ArrayList<Knowledge> knowledgeList) {
        mSelfTestActivity = selfTestActivity;
        mKnowledgeList = knowledgeList;
        initData();
    }

    public void initData() {
        mQuestionLists = new ArrayList<>();
        mSqLists = new ArrayList<>();
        if (mKnowledgeList != null) {
            for (int i = 0; i < mKnowledgeList.size(); i++) {
                mQuestionLists.add(new ArrayList<Question>());
                mSqLists.add(new ArrayList<Sq>());
            }
        }
    }

    public void initSelfTest() {
        for (int i = 0; i < mKnowledgeList.size(); i++) {
            knowledges = knowledges + mKnowledgeList.get(i).getKnowledgecontent() + ",";
        }
        if (!knowledges.equals("")) {
            knowledges = knowledges.substring(0, knowledges.length() - 1);
        }
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        api.insertASelfTest(mSelfTestActivity.getStudentId(), knowledges)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SelfTest>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SelfTest selfTest) {
                        mSelfTest = selfTest;
                        Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
                        api.selectASelfTestQuestion(null, mKnowledgeList.get(knowledgeIndex).getKnowledgeid(), nextLevel)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Question>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Question question) {
                                        mQuestionLists.get(knowledgeIndex).add(question);
                                        mSelfTestActivity.initSelfTestView(question, ++selfTestQuestionId);
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

    //初始化下一道题
    public void initNextSelfTestQuestion(String studentAnswer) {
        //对当前题的答题结果做本地保存
        Sq sq = new Sq();
        sq.setSelftestid(mSelfTest.getSelftestid());
        sq.setQuestionid(mQuestionLists.get(knowledgeIndex).get(selfTestQuestionIndex).getQuestionid());
        sq.setStudentanswer(studentAnswer);
        sq.setTestQuestionid(selfTestQuestionId);
        sq.setResult(studentAnswer.equals(mQuestionLists.get(knowledgeIndex).get(selfTestQuestionIndex).getCorrectanswer()));
        mSqLists.get(knowledgeIndex).add(sq);

        //将当前答题信息上传至后端
        sqParamsMap = new HashMap<String, RequestBody>();
        sqParamsMap.put("selftestid", ParseUtil.parseRequestBody("" + sq.getSelftestid()));
        sqParamsMap.put("questionid", ParseUtil.parseRequestBody("" + sq.getQuestionid()));
        sqParamsMap.put("testQuestionid", ParseUtil.parseRequestBody("" + sq.getTestQuestionid()));
        sqParamsMap.put("studentanswer", ParseUtil.parseRequestBody(sq.getStudentanswer()));
        sqParamsMap.put("result", ParseUtil.parseRequestBody("" + sq.getResult()));
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        api.insertASq(sqParamsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Sq>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Sq sq) {
                        initNext();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void finishSelfTest() {
        Intent intent = new Intent(mSelfTestActivity, SelfTestResultActivity.class);
        intent.putExtra("selfTest", mSelfTest);
        intent.putParcelableArrayListExtra("knowledgeList", mKnowledgeList);
        mSelfTestActivity.startActivity(intent);
        mSelfTestActivity.finish();
    }


    //确定下一题难度，获取下一题
    public void initNext() {
        /**
         * 确定下一道题的难度
         */
        if (mQuestionLists.get(knowledgeIndex).size() != Constant.SELFTESTQUESTIONNUM) {
            if (mSqLists.get(knowledgeIndex).get(selfTestQuestionIndex).getResult()) {//当前题做对的情况下
                switch (mQuestionLists.get(knowledgeIndex).get(selfTestQuestionIndex).getLevel()) {//判断当前题目的难度
                    case 1:
                    case 2://难度加一，获取下一道题
                        nextLevel++;
                        selfTestQuestionIndex++;
                        break;
                    case 3:
                        switch (mQuestionLists.get(knowledgeIndex).get(selfTestQuestionIndex - 1).getLevel()) {
                            case 2://再出一道相同难度的题目
                                selfTestQuestionIndex++;
                                break;//如果上一题的难度是2，需要再做一道当前难度的题
                            case 3:
                                if (knowledgeIndex + 1 >= mKnowledgeList.size()) {//已经测完所有知识点
                                    //结束测试
                                    ToastUtil.shortToast("测试结束");
                                    finishSelfTest();
                                    return;
                                } else {//切到下一知识点
                                    //获取下一知识点第一道测试题
                                    knowledgeIndex++;
                                    nextLevel = 2;//难度重置
                                    selfTestQuestionIndex = 0;//在某个知识点中，测试题的编号重置
                                }
                                break;
                        }
                        break;
                }
            } else {//当前题目没有做对
                switch (mQuestionLists.get(knowledgeIndex).get(selfTestQuestionIndex).getLevel()) {
                    case 1:
                        switch (mQuestionLists.get(knowledgeIndex).get(selfTestQuestionIndex - 1).getLevel()) {
                            case 1:
                                if (knowledgeIndex + 1 >= mKnowledgeList.size()) {//已经测完所有知识点
                                    //结束测试
                                    ToastUtil.shortToast("测试结束");
                                    finishSelfTest();
                                    return;
                                } else {//切到下一知识点
                                    //获取下一知识点第一道测试题
                                    knowledgeIndex++;
                                    nextLevel = 2;//难度重置
                                    selfTestQuestionIndex = 0;//在某个知识点中，测试题的编号重置
                                }
                                break;
                            case 2:
                                //再出一道相同难度的题目
                                selfTestQuestionIndex++;
                                break;
                        }
                        ;
                        break;

                    case 2:
                    case 3:
                        //获取降一难度的下一道题
                        nextLevel--;
                        selfTestQuestionIndex++;
                        break;
                }

            }
        } else {//当前知识点已测5题
            if (knowledgeIndex + 1 >= mKnowledgeList.size()) {//已经测完所有知识点
                //结束测试
                ToastUtil.shortToast("测试结束");
                finishSelfTest();
                return;
            } else {//切到下一知识点
                //获取下一知识点第一道测试题
                knowledgeIndex++;
                nextLevel = 2;//难度重置
                selfTestQuestionIndex = 0;//在某个知识点中，测试题的编号重置
            }
        }


        List<Integer> preQuestionIdList = null;
        if (selfTestQuestionIndex != 0) {
            preQuestionIdList = new ArrayList<>();
            for (int i = 0; i < mQuestionLists.get(knowledgeIndex).size(); i++) {
                preQuestionIdList.add(mQuestionLists.get(knowledgeIndex).get(i).getQuestionid());
            }
        }

        //获取下一道题
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        api.selectASelfTestQuestion(preQuestionIdList, mKnowledgeList.get(knowledgeIndex).getKnowledgeid(), nextLevel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Question>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Question question) {
                        mQuestionLists.get(knowledgeIndex).add(question);
                        mSelfTestActivity.initSelfTestView(question, ++selfTestQuestionId);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
