package com.example.evaluation_system.test.student;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseApp;
import com.example.evaluation_system.base.BaseFragment;
import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.student.begintest.unfinishedtestdetail.UnfinishedTestDetailActivity;
import com.example.evaluation_system.widget.RecyclerAdapter;

import butterknife.BindView;


public class UnFinishedTestFragment extends BaseFragment implements TestContract.View {

    private static final String TAG = "ShareFragment";


    @BindView(R.id.test_fragment_swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.test_fragment_recycler)
    RecyclerView mRecycler;

    //RecyclerView的适配器
    RecyclerAdapter<Test> adapter;
    //MVP中的Presenter
    private UnFinishedTestPresenter mPresenter;
    UserManager mUserManager;

    public UnFinishedTestFragment() {
        mPresenter = new UnFinishedTestPresenter(this);
        mUserManager=new UserManager(BaseApp.getContext());
        adapter = new RecyclerAdapter<Test>() {
            @Override
            protected int getItemViewType(int position, Test UnFinishedTest) {
                return R.layout.unfinished_test_recycler_item;
            }

            @Override
            protected ViewHolder<Test> onCreateViewHolder(View root, int viewType) {
                return new UnFinishedTestFragment.ViewHolder(root);
            }
        };
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_unfinished;
    }

    @Override
    protected void initData() {
        mPresenter.getInitData(mUserManager.getStudent().getUserid());
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,
                false));
        mRecycler.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshData(mUserManager.getStudent().getUserid());
            }
        });


    }



    /**
     * 监听NestedScrollView滑动到底部类
     */
    class VerticalOnScrollListener implements NestedScrollView.OnScrollChangeListener {

        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if ((scrollY >= (v.getChildAt(v.getChildCount() - 1)).getMeasuredHeight()
                    - v.getMeasuredHeight()) && scrollY > oldScrollY) {
                if (adapter.getItemCount() > 0) {
                    if (mPresenter.canLoadMore()) {
//                        showLoadMoreView();
                        mPresenter.loadMore();
                    } else {
//                        showNoMoreView();
                    }
                }
            }
        }
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
        protected void onBind(final Test unFinishedTest) {
            testNameTv.setText(unFinishedTest.getTesttopic());
            ImageManager.getInstance().loadImage(mContext,
                    unFinishedTest.getTestpicture(),
                    testImg);
            beginTv.setText(unFinishedTest.getBegin());
            endTv.setText(unFinishedTest.getEnd());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UnfinishedTestDetailActivity.class);
                    intent.putExtra("unFinishedTest", unFinishedTest);
                    startActivity(intent);
                }
            });
        }
    }
}
