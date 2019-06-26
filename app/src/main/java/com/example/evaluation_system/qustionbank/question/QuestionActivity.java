package com.example.evaluation_system.qustionbank.question;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.base.model.Question;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.questionupload.QuestionUploadActivity;
import com.example.evaluation_system.widget.RecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QuestionActivity extends BaseToolbarActivity {
    @BindView(R.id.rv_question)
    RecyclerView questionRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.bt_question_add)
    FloatingActionButton mQuestionAddBtn;

    RecyclerAdapter mRecyclerAdapter;
    Knowledge mKnowledge;
    ArrayList<Question> mQuestionArrayList;

    @Override
    protected View initContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_question,null);
    }

    @Override
    protected String initToolbarTitle() {
        if(mKnowledge==null){
            mKnowledge=getIntent().getParcelableExtra("knowledge");
        }
        return mKnowledge.getKnowledgecontent();
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        if(mKnowledge==null){
            mKnowledge=getIntent().getParcelableExtra("knowledge");
        }
        mQuestionArrayList=new ArrayList<>();
        mRecyclerAdapter=new RecyclerAdapter<Question>() {
            @Override
            protected int getItemViewType(int position, Question question) {
                return R.layout.recycler_item_question;
            }

            @Override
            protected ViewHolder<Question> onCreateViewHolder(View root, int viewType) {
                return new QuestionActivity.ViewHolder(root);
            }
        };
        questionRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        questionRv.setAdapter(mRecyclerAdapter);
        mQuestionAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestionActivity.this, QuestionUploadActivity.class));
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerAdapter.clear();
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        initData();
    }

    public void initData(){
        Api api= HttpManager.getInstance().getApiService(Constant.BASE_QUESTION_URL);
        api.selectQuestionsByKnowledgeId(mKnowledge.getKnowledgeid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Question>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Question> questions) {
                        mQuestionArrayList=questions;
                        mRecyclerAdapter.add(questions);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Question>{
        @BindView(R.id.rl_question)
        RelativeLayout questionRl;
        @BindView(R.id.tv_question_questionContent)
        TextView questionContentTv;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Question question) {
            questionContentTv.setText(question.getQuestioncontent());
            questionRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:跳转到题目详情界面
                    Intent intent=new Intent(QuestionActivity.this,QuestionUploadActivity.class);
                    intent.putExtra("requestCode",Constant.UPDATEQUESITON);
                    intent.putExtra("question",question);
                    intent.putExtra("knowledge",mKnowledge);
                    startActivity(intent);
                }
            });
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
}
