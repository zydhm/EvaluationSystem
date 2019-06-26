package com.example.evaluation_system.qustionbank;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseFragment;
import com.example.evaluation_system.base.model.Chapter;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.base.model.KnowledgeQueryVo;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.questionupload.QuestionUploadActivity;
import com.example.evaluation_system.qustionbank.adapter.QuestionBankAdapter;
import com.example.evaluation_system.qustionbank.question.QuestionActivity;
import com.example.evaluation_system.test.teacher.testMain.testdetail.adapter.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QuestionBankFragment extends BaseFragment {
    private static final String TAG = "QuestionBankFragment";
    @BindView(R.id.bt_question_add)
    FloatingActionButton mQuestionAddBtn;
    @BindView(R.id.elv_selectKnowledge)
    ExpandableListView selectKnowledgeElv;

    QuestionBankAdapter mQuestionBankAdapter;

    ArrayList<String> mChapterContentArrayList;
    ArrayList<ArrayList<Knowledge>> mKnowledgeArrayLists;
    ArrayList<Knowledge> tempArrayList;
    String tempChapterContent;
    Knowledge tempKnowledge;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_question_bank;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mChapterContentArrayList = new ArrayList<>();
        mKnowledgeArrayLists = new ArrayList<>();
        mQuestionBankAdapter=new QuestionBankAdapter();
        mQuestionAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), QuestionUploadActivity.class));
            }
        });
        initKnowledgeElv();
    }

    @Override
    protected void initData() {
        super.initData();
        if(mChapterContentArrayList==null||mChapterContentArrayList.size()==0){
            getDataFromServer("1");
        }
    }

    public void getDataFromServer(String courseId){
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_KNOWLEDGE_URL);
        api.queryKnowledge(courseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<KnowledgeQueryVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<KnowledgeQueryVo> knowledgeQueryVos) {
                        for (int i = 0; i < knowledgeQueryVos.size(); i++) {
//                            tempChapterContent = new String();
                            tempChapterContent = knowledgeQueryVos.get(i).getChaptername();
                            mChapterContentArrayList.add(tempChapterContent);
                            tempArrayList = new ArrayList<Knowledge>();
                            for (int j = 0; j < knowledgeQueryVos.get(i).getKnowledgeList().size(); j++) {
//                                tempKnowledge = new Knowledge();
                                tempKnowledge = knowledgeQueryVos.get(i).getKnowledgeList().get(j);
                                tempArrayList.add(tempKnowledge);
                            }
                            mKnowledgeArrayLists.add(tempArrayList);
                        }
                        mQuestionBankAdapter.setChapterContentArrayList(mChapterContentArrayList);
                        mQuestionBankAdapter.setKnowledgeArrayLists(mKnowledgeArrayLists);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initKnowledgeElv(){
        selectKnowledgeElv.setAdapter(mQuestionBankAdapter);
        selectKnowledgeElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent=new Intent(getContext(),QuestionActivity.class);
                intent.putExtra("knowledge",mKnowledgeArrayLists.get(i).get(i1));
                getActivity().startActivity(intent);
                //TODO:跳到相关知识点题目显示页面
                return false;
            }
        });
    }
}
