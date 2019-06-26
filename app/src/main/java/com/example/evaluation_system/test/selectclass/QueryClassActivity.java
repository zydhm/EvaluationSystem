package com.example.evaluation_system.test.selectclass;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseToolbarActivity;
import com.example.evaluation_system.base.model.ClassMsg;
import com.example.evaluation_system.base.model.ClassMsgCustom;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.widget.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QueryClassActivity extends BaseToolbarActivity {


    @BindView(R.id.rv_queryClass)
    RecyclerView queryClassRv;
    @BindView(R.id.toolbar_textView)
    TextView saveTv;
    RecyclerAdapter<ClassMsgCustom> mRecyclerAdapter;
    List<ClassMsgCustom> mClassMsgList;
    ClassMsgCustom tempClassMsgCustom;
    Intent mIntent;
    String result="";



    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_class_select,null);
    }

    @Override
    protected String initToolbarTitle() {
        return "班级";
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        saveTv.setVisibility(View.VISIBLE);
        mIntent=getIntent();
        mClassMsgList=new ArrayList<>();
        mRecyclerAdapter=new RecyclerAdapter<ClassMsgCustom>() {
            @Override
            protected int getItemViewType(int position, ClassMsgCustom aClassMsgCustom) {
                return R.layout.class_item;
            }

            @Override
            protected ViewHolder<ClassMsgCustom> onCreateViewHolder(View root, int viewType) {
                return new QueryClassActivity.ViewHolder(root);
            }
        };
        queryClassRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        queryClassRv.setAdapter(mRecyclerAdapter);
        getInitData();
    }


    /**
     * 获取初始数据
     */
    public void getInitData(){
        Api api=  HttpManager.getInstance().getApiService(Constant.BASE_CLASS_URL);
        api.selectClassByCourseId(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ClassMsg>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ClassMsg> classMsgs) {
                        for(int i=0;i<classMsgs.size();i++){
                            tempClassMsgCustom=new ClassMsgCustom();
                            tempClassMsgCustom.setClassid(classMsgs.get(i).getClassid());
                            tempClassMsgCustom.setClassname(classMsgs.get(i).getClassname());
                            tempClassMsgCustom.setCheckFlag(false);
                            mClassMsgList.add(tempClassMsgCustom);
                        }
                        mRecyclerAdapter.add(mClassMsgList);
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

    public void finishInit(){
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<mClassMsgList.size();i++){
                    if(mClassMsgList.get(i).getCheckFlag()){
                        result=result+mClassMsgList.get(i).getClassname()+",";
                    }
                }
                if(!result.equals("")){
                    result=result.substring(0,result.length()-1);
                    mIntent.putExtra("result",result);
                    setResult(Constant.RESULTCLASSTOTEACHER,mIntent);
                }
                finish();
            }
        });
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<ClassMsgCustom> {
        //班级名
        @BindView(R.id.tv_classItem)
        TextView classNameTv;
        //选择框
        @BindView(R.id.cb_classItem)
        CheckBox classItemCb;
        @BindView(R.id.rl_classItem)
        RelativeLayout classItemRl;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final ClassMsgCustom aClassMsgCustom) {
            classNameTv.setText(aClassMsgCustom.getClassname());
            classItemCb.setClickable(false);
            classItemRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aClassMsgCustom.setCheckFlag(!aClassMsgCustom.getCheckFlag());
                    classItemCb.toggle();
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
