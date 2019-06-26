package com.example.evaluation_system.test.teacher.testMain;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.teacher.testMain.testdetail.TestDetailActivity;
import com.example.evaluation_system.test.teacher.testUpload.TestUploadActivity;
import com.example.evaluation_system.widget.RecyclerAdapter;

import butterknife.BindView;

public class TeacherTestActivity extends BaseActivity {
    @BindView(R.id.bt_test_add)
    FloatingActionButton testAddBtn;
    @BindView(R.id.teacher_test_swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.teacher_test_recycler)
    RecyclerView mRecyclerView;
//    @BindView(R.id.teacher_test_empty)
//    View mEmptyView;
//    @BindView(R.id.teacher_test_load_more)
//    TextView mLoadMoreTv;
//    @BindView(R.id.teacher_test_no_more)
//    TextView mNoMoreView;

    TeacherTestPresenter mTeacherTestPresenter;
    UserManager mUserManager;
    RecyclerAdapter mRecyclerAdapter;

    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_teacher_test, null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        mTeacherTestPresenter = new TeacherTestPresenter(this);
        mUserManager = new UserManager(this);
        testAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherTestActivity.this, TestUploadActivity.class));
            }
        });
        mRecyclerAdapter = new RecyclerAdapter<Test>() {
            @Override
            protected int getItemViewType(int position, Test test) {
                return R.layout.unfinished_test_recycler_item;
            }

            @Override
            protected ViewHolder<Test> onCreateViewHolder(View root, int viewType) {
                return new TeacherTestActivity.ViewHolder(root);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mTeacherTestPresenter.initData(mUserManager.getTeacher().getUserid());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTeacherTestPresenter.refreshData(mUserManager.getTeacher().getUserid());
            }
        });
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Test> {
        //考试主题
        @BindView(R.id.test_item_test_name)
        TextView testNameTv;
        //开始时间
        @BindView(R.id.test_item_begin_date)
        TextView beginTv;
        //结束时间
        @BindView(R.id.test_item_end_date)
        TextView endTv;
        //考试图
        @BindView(R.id.test_item_test_img)
        ImageView testImg;
        //根布局
        @BindView(R.id.test_item_root)
        RelativeLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Test test) {
            testNameTv.setText(test.getTesttopic());
            ImageManager.getInstance().loadImage(TeacherTestActivity.this,
                    test.getTestpicture(),
                    testImg);
            beginTv.setText(test.getBegin());
            endTv.setText(test.getEnd());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TeacherTestActivity.this, TestDetailActivity.class);
                    intent.putExtra("test", test);
                    startActivity(intent);
                }
            });
        }
    }

//    public void showEmptyView() {
//        if (mEmptyView.getVisibility() == View.GONE) {
//            mEmptyView.setVisibility(View.VISIBLE);
//        }
//        if (mRecyclerView.getVisibility() == View.VISIBLE) {
//            mRecyclerView.setVisibility(View.GONE);
//        }
//        if (mLoadMoreTv.getVisibility() == View.VISIBLE) {
//            mLoadMoreTv.setVisibility(View.GONE);
//        }
//        if (mNoMoreView.getVisibility() == View.VISIBLE) {
//            mNoMoreView.setVisibility(View.GONE);
//        }
//    }
//
//    public void hideEmptyView(){
//        if (mEmptyView.getVisibility() == View.VISIBLE) {
//            mEmptyView.setVisibility(View.GONE);
//        }
//    }
//    public void hideLoadMore() {
//        if (mLoadMoreTv.getVisibility() == View.VISIBLE) {
//            mLoadMoreTv.setVisibility(View.GONE);
//        }
//    }


}
