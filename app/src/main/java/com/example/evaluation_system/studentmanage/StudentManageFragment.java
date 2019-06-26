package com.example.evaluation_system.studentmanage;

import android.view.View;
import android.widget.ExpandableListView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseApp;
import com.example.evaluation_system.base.BaseFragment;
import com.example.evaluation_system.base.model.Student;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;
import com.example.evaluation_system.studentmanage.dialog.StudentMsgDialog;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StudentManageFragment extends BaseFragment {
    @BindView(R.id.elv_studentManage)
    ExpandableListView studentManageElv;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout mSwipeRefreshLayout;


    UserManager mUserManager;
    StudentManageAdapter mStudentManageAdapter;
    ArrayList<ArrayList<Student>> mStudentArrayList;
    ArrayList<String> mClassNameArrayList;
    ArrayList<Student> tempStudentArrayList;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_student_manage;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        mUserManager=new UserManager(BaseApp.getContext());
        mStudentManageAdapter=new StudentManageAdapter();
        studentManageElv.setAdapter(mStudentManageAdapter);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mClassNameArrayList.clear();
//                mStudentArrayList.clear();
//                getDateFromServer(mUserManager.getTeacher().getUserid());
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (mStudentArrayList == null || mStudentArrayList.size() == 0) {
            getDateFromServer(mUserManager.getTeacher().getUserid());
        }
    }

    public void getDateFromServer(Integer teacherId){
        Api api= HttpManager.getInstance().getApiService(Constant.BASE_TEACHER_URL);
        api.selectStudentByTeacherId(teacherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Student>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Student> students) {
                        if(students!=null) {
                            String nowClassName = students.get(0).getClassName();
                            if (mStudentArrayList == null) {
                                mStudentArrayList = new ArrayList<>();
                                mClassNameArrayList = new ArrayList<>();
                            }
                            tempStudentArrayList=new ArrayList<>();
                            for (int i=0;i<students.size();i++){
                                if(nowClassName.equals(students.get(i).getClassName())){
                                    tempStudentArrayList.add(students.get(i));
                                }else{
                                    mClassNameArrayList.add(nowClassName);
                                    mStudentArrayList.add(tempStudentArrayList);
                                    nowClassName=new String(students.get(i).getClassName());
                                    tempStudentArrayList=new ArrayList<>();
                                    i--;
                                }
                            }
                            mClassNameArrayList.add(nowClassName);
                            mStudentArrayList.add(tempStudentArrayList);
                            refreshAdapter();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void refreshAdapter(){
        mStudentManageAdapter.setClassNameArrayList(mClassNameArrayList);
        mStudentManageAdapter.setStudentArrayList(mStudentArrayList);
        studentManageElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                StudentMsgDialog studentMsgDialog=new StudentMsgDialog.Builder(getActivity())
                        .setStudentNameTv(mStudentArrayList.get(i).get(i1).getUsername())
                        .setStudentLoginNameTv(mStudentArrayList.get(i).get(i1).getLoginname())
                        .setTelTv(mStudentArrayList.get(i).get(i1).getTel())
                        .setEMailTv(mStudentArrayList.get(i).get(i1).getEmail())
                        .create();
                studentMsgDialog.show();
                return false;
            }
        });
    }
}
