package com.example.evaluation_system.test.teacher.testUpload;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.knowledge.QueryKnowledgeActivity;
import com.example.evaluation_system.test.selectclass.QueryClassActivity;
import com.example.evaluation_system.utils.ImageUtil;
import com.example.evaluation_system.utils.ParseUtil;
import com.example.evaluation_system.utils.TimeUtil;
import com.example.evaluation_system.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TestUploadActivity extends BaseActivity {
    private static final String TAG = "TestUploadActivity";
    @BindView(R.id.et_testUpload_testTopic)
    EditText testTopicEt;
    @BindView(R.id.iv_testUpload_testPic)
    ImageView testPicIv;
    @BindView(R.id.s_testUpload_courseId)
    Spinner courseIdS;
    @BindView(R.id.rl_testUpload_knowledges)
    RelativeLayout knowledgesRl;
    @BindView(R.id.tv_testUpload_knowledges)
    TextView knowledgesTv;
    @BindView(R.id.img_testUpload_knowledges)
    ImageView knowledgesIv;
    @BindView(R.id.rl_testUpload_date)
    RelativeLayout dateRl;
    @BindView(R.id.tv_testUpload_date)
    TextView dateTv;
    @BindView(R.id.rl_testUpload_begin)
    RelativeLayout beginRl;
    @BindView(R.id.tv_testUpload_begin)
    TextView beginTv;
    @BindView(R.id.rl_testUpload_end)
    RelativeLayout endRl;
    @BindView(R.id.tv_testUpload_end)
    TextView endTv;
    @BindView(R.id.rl_testUpload_more)
    RelativeLayout moreRl;
    @BindView(R.id.et_testUpload_questionNumber)
    EditText questionNumberEt;
    @BindView(R.id.rl_testUpload_classes)
    RelativeLayout classesRl;
    @BindView(R.id.tv_testUpload_classes)
    TextView classesTv;
    @BindView(R.id.img_testUpload_classes)
    ImageView classesIv;
    @BindView(R.id.bt_questionUpload_save)
    Button saveBtn;


    String knowledges="";
    String classes="";
    File file;
    Map<String, RequestBody> paramsMap;
    UserManager mUserManager;
    RequestBody testPic;
    MultipartBody.Part body;
    TestUploadPresenter mTestUploadPresenter;

    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_test_upload, null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        paramsMap=new HashMap<String,RequestBody>();
        mUserManager=new UserManager(this);
        mTestUploadPresenter=new TestUploadPresenter(this);

        /**
         * 选择知识点
         */
        knowledgesRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestUploadActivity.this, QueryKnowledgeActivity.class);
                intent.putExtra("requestCode", Constant.REQUESTKNOWLEDGEFROMTEST);
                startActivityForResult(intent, Constant.REQUESTKNOWLEDGEFROMTEST);
            }
        });

        /**
         * 高级设置，功能暂未开放
         */
        moreRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.shortToast("功能暂未开放");
            }
        });


        /**
         * 选择照片
         */
        testPicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromAlbum();
            }
        });

        /**
         * 设置时间
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        final Calendar c = Calendar.getInstance();
        String now = sdf.format(new Date());
//        设置初始时间
        dateTv.setText(now.split(" ")[0]);
        beginTv.setText(now.split(" ")[1]);

        dateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TestUploadActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Log.d(TAG, "onDateSet: " + year);
                                dateTv.setText(TimeUtil.getDate(year, monthOfYear, dayOfMonth));
                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        beginRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个TimePickerDialog实例，并把它显示出来
                new TimePickerDialog(TestUploadActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                beginTv.setText(TimeUtil.getTime(hourOfDay, minute));
                            }
                        }
                        // 设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        // true表示采用24小时制
                        true)
                        .show();
            }
        });
        endRl.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(TestUploadActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {

                                endTv.setText(TimeUtil.getTime(hourOfDay, minute));
                            }
                        }
                        // 设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        // true表示采用24小时制
                        true)
                        .show();
            }
        });

        /**
         * 选择考试班级
         */
        classesRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestUploadActivity.this, QueryClassActivity.class);
                intent.putExtra("requestCode", Constant.REQUESTCLASSFROMTEACHER);
                startActivityForResult(intent, Constant.REQUESTCLASSFROMTEACHER);
            }
        });

        /**
         * 按下提交按钮
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isParamsCorrect()){
                    paramsMap.put("testtopic",ParseUtil.parseRequestBody(testTopicEt.getText().toString()));
                    paramsMap.put("teacherid", ParseUtil.parseRequestBody(""+mUserManager.getTeacher().getUserid()));
                    paramsMap.put("courseid",ParseUtil.parseRequestBody("1"));
                    paramsMap.put("knowledges",ParseUtil.parseRequestBody(knowledges));
                    paramsMap.put("begin",ParseUtil.parseRequestBody(dateTv.getText().toString()+" "+beginTv.getText().toString()+":00"));
                    paramsMap.put("end",ParseUtil.parseRequestBody(dateTv.getText().toString()+" "+endTv.getText().toString()+":00"));
                    paramsMap.put("questionnumber",ParseUtil.parseRequestBody(questionNumberEt.getText().toString()));
                    paramsMap.put("classes",ParseUtil.parseRequestBody(classes));
                    if(file!=null){
                        testPic = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        body = MultipartBody.Part.createFormData("test_pic", file.getName(), testPic);
                    }
                    mTestUploadPresenter.uploadATest(paramsMap,body);
                }
            }
        });

    }


    /**
     * 图片选择
     */
    public void choosePhotoFromAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUESTPIC);
        } else {
            openAlbum();
        }
    }

    public void openAlbum() {
        Log.d(TAG, "openAlbum: ");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, Constant.REQUESTPIC);
    }

    /**
     * 判断输入条件是否有误
     */
    public Boolean isParamsCorrect(){
        if(testTopicEt.getText()==null||testTopicEt.getText().toString().equals("")){
            ToastUtil.shortToast("主题不能为空！");
            return false;
        }
        if(knowledges==null||knowledges.equals("")){
            ToastUtil.shortToast("知识点不能为空！");
            return false;
        }
        if(endTv.getText()==null||endTv.getText().toString().equals("")){
            ToastUtil.shortToast("结束时间不能为空！");
            return false;
        }
        if(questionNumberEt.getText()==null||questionNumberEt.getText().toString().equals("")){
            ToastUtil.shortToast("总题数不能为空！");
            return false;
        }
        if(classes==null||classes.equals("")){
            ToastUtil.shortToast("参加考试的班级不能为空！");
            return false;
        }
        return true;
    }


    /**
     * 回调函数，接收传回来的知识点
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUESTKNOWLEDGEFROMTEST:
                if(resultCode==Constant.RESULTKNOWLEDGETOTEST) {
                    if (data!=null) {
                        List<Knowledge> knowledgeList = data.getParcelableArrayListExtra("result");
                        if(knowledgeList!=null&&knowledgeList.size()>0) {
                            knowledges="";
                            for(int i=0;i<knowledgeList.size();i++) {
                                knowledges=knowledges+knowledgeList.get(i).getKnowledgecontent()+",";
                            }
                            if(knowledges!=""){
                                knowledges=knowledges.substring(0,knowledges.length()-1);
                            }
                            knowledgesTv.setText(knowledges);
                            knowledgesIv.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case Constant.REQUESTPIC:
                if(resultCode==Constant.RESULTPIC) {
                    if (data != null) {
                        file = new File(ImageUtil.handleImage(data, this));
                        Glide.with(this).load(file)
                                .into(testPicIv);
                    }
                }
                break;
            case Constant.REQUESTCLASSFROMTEACHER:
                if(resultCode==Constant.RESULTCLASSTOTEACHER){
                    if (data!=null&&data.getStringExtra("result") != null && !data.getStringExtra("result").equals("")) {
                        classes=data.getStringExtra("result");
                        classesTv.setText(data.getStringExtra("result"));
                        classesIv.setVisibility(View.GONE);
                    }
                }
            default:
                break;
        }

    }
}
