package com.example.evaluation_system.more.student.selftest.selftestresult;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.SelfTest;
import com.example.evaluation_system.base.model.SelfTestResultVo;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SelfTestResultActivity extends BaseActivity {
    private static final String TAG = "SelfTestResultActivity";
    @BindView(R.id.elv_selfTestResult)
    ExpandableListView selfTestResultElv;

    SelfTestResultAdapter mSelfTestResultAdapter;
    SelfTest mSelfTest;
    List<Knowledge> mKnowledgeList;
    List<List<SelfTestResultVo>> mSelfTestResultVoLists;
    List<String> mMasteryList;
    ArrayList<SelfTestResultVo> tempSelfTestResultVos;
    int knowledgeIndex = 0;

    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_self_test_result, null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        mKnowledgeList = getIntent().getParcelableArrayListExtra("knowledgeList");
        mSelfTest = getIntent().getParcelableExtra("selfTest");
        mSelfTestResultVoLists = new ArrayList<>();
        initDate();
    }

    public void initDate() {
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_STUDENT_URL);
        api.selectSelfTestResult(mSelfTest.getSelftestid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelfTestResultVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<SelfTestResultVo> selfTestResultVos) {
                        tempSelfTestResultVos=new ArrayList<>();
                        for (int j = 0; j < selfTestResultVos.size(); j++) {
                            if (selfTestResultVos.get(j).getQuestion().getKnowledgeid().equals(mKnowledgeList.get(knowledgeIndex).getKnowledgeid())) {
                                tempSelfTestResultVos.add(selfTestResultVos.get(j));
                            } else {
                                mSelfTestResultVoLists.add(tempSelfTestResultVos);
                                knowledgeIndex++;
                                j--;
                                tempSelfTestResultVos = new ArrayList<>();
                            }
                        }
                        mSelfTestResultVoLists.add(tempSelfTestResultVos);
                        analyseMastery();
                        mSelfTestResultAdapter = new SelfTestResultAdapter();
                        mSelfTestResultAdapter.setMasteryList(mMasteryList);
                        mSelfTestResultAdapter.setSelfTestResultVoLists(mSelfTestResultVoLists);
                        mSelfTestResultAdapter.setKnowledgeList(mKnowledgeList);
                        selfTestResultElv.setAdapter(mSelfTestResultAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void analyseMastery() {
        mMasteryList = new ArrayList<>();
        for (int i = 0; i < mKnowledgeList.size(); i++) {
            if (mSelfTestResultVoLists.get(i).size() < Constant.SELFTESTQUESTIONNUM) {
                if (mSelfTestResultVoLists.get(i).get(mSelfTestResultVoLists.get(i).size() - 1).getQuestion().getLevel() == 1) {
                    mMasteryList.add("一无所知");
                } else if (mSelfTestResultVoLists.get(i).get(mSelfTestResultVoLists.get(i).size() - 1).getQuestion().getLevel() == 3) {
                    mMasteryList.add("炉火纯青");
                }
            } else {
                int points=0;
                for (int j = 0; j < mSelfTestResultVoLists.get(i).size(); j++) {
                    //遍历测试题，计算总分
                    if (mSelfTestResultVoLists.get(i).get(j).getResult()) {
                        if(mSelfTestResultVoLists.get(i).get(j).getQuestion().getLevel()==1){
                            points+=Constant.EASYWEIGHT;
                        }else if(mSelfTestResultVoLists.get(i).get(j).getQuestion().getLevel()==2){
                            points+=Constant.NORMALWEIGHT;
                        }else if(mSelfTestResultVoLists.get(i).get(j).getQuestion().getLevel()==3){
                            points+=Constant.HARDWEIGHT;
                        }
                    }
                }
                if (points<=((float)Constant.SELFTESTQUESTIONNUM/Constant.BASEQUESTIONNUM)*Constant.BASELEVEL1) {
                    mMasteryList.add("略知一二");
                } else if (((float)Constant.SELFTESTQUESTIONNUM/Constant.BASEQUESTIONNUM)*Constant.BASELEVEL1<points&&points<((float)Constant.SELFTESTQUESTIONNUM/Constant.BASEQUESTIONNUM)*Constant.BASELEVEL2) {
                    mMasteryList.add("中规中矩");
                } else if (((float)Constant.SELFTESTQUESTIONNUM/Constant.BASEQUESTIONNUM)*Constant.BASELEVEL2<=points&&points<((float)Constant.SELFTESTQUESTIONNUM/Constant.BASEQUESTIONNUM)*Constant.BASELEVEL3) {
                    mMasteryList.add("驾轻就熟");
                } else if(((float)Constant.SELFTESTQUESTIONNUM/Constant.BASEQUESTIONNUM)*Constant.BASELEVEL3<=points){
                    mMasteryList.add("炉火纯青");
                }
            }
        }
    }

}
