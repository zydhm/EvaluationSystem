package com.example.evaluation_system.test.knowledge;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.KnowledgeQueryVo;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.more.student.selftest.SelfTestActivity;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.test.knowledge.adapter.KnowledgeAdapter;
import com.example.evaluation_system.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QueryKnowledgeActivity extends BaseToolbarActivity {
    private static final String TAG = "QueryKnowledgeActivity";
    @BindView(R.id.elv_selectKnowledge)
    ExpandableListView selectKnowledgeElv;
    @BindView(R.id.toolbar_textView)
    TextView saveTv;
    KnowledgeAdapter mKnowledgeAdapter;
    ArrayList<String> mChapterContentArrayList;
    ArrayList<ArrayList<Knowledge>> mKnowledgeArrayList;
    ArrayList<ArrayList<Boolean>> mCheckList;
    ArrayList<Knowledge> tempArrayList;

    ArrayList<Knowledge> mResultArrayList;

    ArrayList<Boolean> tempCheckList;
    String tempChapterContent;
    Knowledge tempKnowledge;
    Intent mIntent;

    int requestCode;


    //出题界面跳转过来时,需要实现单选功能，x、y用以记录上一次被选中的项目，当有新项目被选中时查找取消上一次选中的项目
    int x = -1, y = -1;

    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_knowledge_select, null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        saveTv.setVisibility(View.VISIBLE);
        mIntent = getIntent();
        //获取请求码，确认是从哪个活动发出的请求
        requestCode = mIntent.getIntExtra("requestCode", Constant.NOREQUEST);
        //如果从自测界面跳转过来
        if (requestCode == Constant.REQUESTKNOWLEDGEFROMSELFTEST) {
            saveTv.setText("开始");
        }
        mChapterContentArrayList = new ArrayList<String>();
        mKnowledgeArrayList = new ArrayList<ArrayList<Knowledge>>();
        mCheckList = new ArrayList<ArrayList<Boolean>>();
        mKnowledgeAdapter = new KnowledgeAdapter();
        getInitData();
    }

    public void getInitData() {
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_KNOWLEDGE_URL);
        api.queryKnowledge("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<KnowledgeQueryVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<KnowledgeQueryVo> knowledgeQueryVos) {
                        for (int i = 0; i < knowledgeQueryVos.size(); i++) {
                            tempChapterContent = new String();
                            tempChapterContent = knowledgeQueryVos.get(i).getChaptername();
                            mChapterContentArrayList.add(tempChapterContent);
                            tempArrayList = new ArrayList<Knowledge>();
                            tempCheckList = new ArrayList<Boolean>();
                            for (int j = 0; j < knowledgeQueryVos.get(i).getKnowledgeList().size(); j++) {
                                tempKnowledge = new Knowledge();
                                tempKnowledge = knowledgeQueryVos.get(i).getKnowledgeList().get(j);
                                tempArrayList.add(tempKnowledge);
                                tempCheckList.add(false);
                            }
                            mKnowledgeArrayList.add(tempArrayList);
                            mCheckList.add(tempCheckList);
                        }
                        mKnowledgeAdapter.setChapterContentArrayList(mChapterContentArrayList);
                        mKnowledgeAdapter.setKnowledgeArrayLists(mKnowledgeArrayList);
                        mKnowledgeAdapter.setIsCheckArrayLists(mCheckList);
                        finishInit();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void finishInit() {
        selectKnowledgeElv.setAdapter(mKnowledgeAdapter);
        selectKnowledgeElv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });

        selectKnowledgeElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                if (requestCode == Constant.REQUESTKNOWLEDGEFROMQUESTION) {
                    //不是第一次进行选择
                    if (x != -1) {
                        //此次选择与上一次选择相同
                        if (x == i && y == i1) {
                            mCheckList.get(x).set(y, !mCheckList.get(i).get(i1));
                            mKnowledgeAdapter.onChildViewClicked(view);
                        } else {//此次选择与上一次选择不同
                            mCheckList.get(x).set(y, false);
                            mCheckList.get(i).set(i1, true);
                            mKnowledgeAdapter.onChildViewClicked(view);
                            x = i;
                            y = i1;
                        }
                    } else {//第一次选择
                        mCheckList.get(i).set(i1, !mCheckList.get(i).get(i1));
                        mKnowledgeAdapter.onChildViewClicked(view);
                        x = i;
                        y = i1;
                    }
                } else if (requestCode == Constant.REQUESTKNOWLEDGEFROMTEST || requestCode == Constant.REQUESTKNOWLEDGEFROMSELFTEST) {
                    mCheckList.get(i).set(i1, !mCheckList.get(i).get(i1));
                    mKnowledgeAdapter.onChildViewClicked(view);
                }
                return false;
            }
        });

        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResultArrayList=new ArrayList<>();
                for (int i = 0; i < mCheckList.size(); i++) {
                    for (int j = 0; j < mCheckList.get(i).size(); j++) {
                        if (mCheckList.get(i).get(j))
                        mResultArrayList.add(mKnowledgeArrayList.get(i).get(j));
                    }
                }
                if (requestCode == Constant.REQUESTKNOWLEDGEFROMSELFTEST) {
                    if (mResultArrayList.size() == 0) {
                        ToastUtil.shortToast("请选择知识点！");
                        return;
                    } else {
                        Intent intent = new Intent(QueryKnowledgeActivity.this, SelfTestActivity.class);
                        intent.putParcelableArrayListExtra("result", mResultArrayList);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    mIntent.putParcelableArrayListExtra("result", mResultArrayList);
                    if (requestCode == Constant.REQUESTKNOWLEDGEFROMTEST) {
                        setResult(Constant.RESULTKNOWLEDGETOTEST, mIntent);
                    }else if(requestCode == Constant.REQUESTKNOWLEDGEFROMQUESTION) {
                        setResult(Constant.RESULTKNOWLEDGETOQUESTION, mIntent);
                    }
                    finish();
                }
        }
    });
}


    @Override
    protected String initToolbarTitle() {
        return "知识点";
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
