package com.example.evaluation_system.test.student;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseApp;
import com.example.evaluation_system.base.BaseFragment;
import com.example.evaluation_system.images.ImageManager;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.model.FinishedTest;
import com.example.evaluation_system.test.student.finishedtestdetail.FinishedTestDetailActivity;
import com.example.evaluation_system.widget.RecyclerAdapter;

import butterknife.BindView;

public class FinishedTestFragment extends BaseFragment implements TestContract.View {
    private static final String TAG = "FinishedTestFragment";

    @BindView(R.id.test_fragment_swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.test_fragment_recycler)
    RecyclerView mRecycler;

    //RecyclerView的适配器
    RecyclerAdapter<FinishedTest> adapter;
    //MVP中的Presenter
    private FinishedTestPresenter mPresenter;
    UserManager mUserManager;

    public FinishedTestFragment() {
        Log.d(TAG, "FinishedTestFragment: im creating");
        mPresenter = new FinishedTestPresenter(this);
        mUserManager=new UserManager(BaseApp.getContext());
        adapter = new RecyclerAdapter<FinishedTest>() {
            @Override
            protected int getItemViewType(int position, FinishedTest finishedTest) {
                return R.layout.finished_test_recycler_item;
            }

            @Override
            protected ViewHolder<FinishedTest> onCreateViewHolder(View root, int viewType) {
                return new FinishedTestFragment.ViewHolder(root);
            }
        };
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_finished;
    }
    /**
     * 初始化一些数据，但要牢记在这个方法中不要new 对象
     //防止replace后重复创建对象，以造成内存吃紧，大的对象最好是直接在构造函数中创建，或者是在业务逻辑中确认不会
     // 重复创建可以在里面创建
     */
    @Override
    protected void initData() {
        mPresenter.getInitData(mUserManager.getStudent().getUserid());
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        //为NestedScrollView添加滑动监听器
        //mNestedScrollView.setOnScrollChangeListener(new FinishedTestFragment.VerticalOnScrollListener());
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


    class ViewHolder extends RecyclerAdapter.ViewHolder<FinishedTest> {
        //考试主题
        @BindView(R.id.test_item_test_name)
        TextView testNameTv;
        //成绩
        @BindView(R.id.test_item_result_text2Id)
        TextView resultTv;
        //提交时间
        @BindView(R.id.test_item_create_date_text2Id)
        TextView createDateTv;
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
        protected void onBind(final FinishedTest finishedTest) {
            testNameTv.setText(finishedTest.getTesttopic());
            ImageManager.getInstance().loadImage(mContext,
                    finishedTest.getTestpicture(),
                    testImg);
            resultTv.setText(""+finishedTest.getTestresult());
            createDateTv.setText(finishedTest.getCompletetime());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳至考试详情界面
                    Intent intent=new Intent(getActivity(), FinishedTestDetailActivity.class);
                    intent.putExtra("finishedTest",finishedTest);
                    getActivity().startActivity(intent);

                }
            });
        }
    }
}
