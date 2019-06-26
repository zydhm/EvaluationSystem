package com.example.evaluation_system.questionupload;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseActivity;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.base.model.Question;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.login.UserManager;
import com.example.evaluation_system.test.knowledge.QueryKnowledgeActivity;
import com.example.evaluation_system.utils.ImageUtil;
import com.example.evaluation_system.utils.ParseUtil;
import com.example.evaluation_system.utils.ToastUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class QuestionUploadActivity extends BaseActivity {

    private static final String TAG = "QuestionUploadActivity";

    @BindView(R.id.et_questionUpload_questionContent)
    EditText questionContentEt;
    @BindView(R.id.iv_questionUpload_questionPic)
    ImageView questionPicIv;
    @BindView(R.id.et_questionUpload_answerA)
    EditText answerAEt;
    @BindView(R.id.et_questionUpload_answerB)
    EditText answerBEt;
    @BindView(R.id.et_questionUpload_answerC)
    EditText answerCEt;
    @BindView(R.id.et_questionUpload_answerD)
    EditText answerDEt;
    @BindView(R.id.cb_questionUpload_answerA)
    CheckBox answerACb;
    @BindView(R.id.cb_questionUpload_answerB)
    CheckBox answerBCb;
    @BindView(R.id.cb_questionUpload_answerC)
    CheckBox answerCCb;
    @BindView(R.id.cb_questionUpload_answerD)
    CheckBox answerDCb;
    @BindView(R.id.s_questionUpload_courseId)
    Spinner courseIdS;
    @BindView(R.id.rl_questionUpload_knowledge)
    RelativeLayout knowledgeRl;
    @BindView(R.id.tv_questionUpload_knowledge)
    TextView knowledgeTv;
    @BindView(R.id.img_questionUpload_knowledge)
    ImageView knowledgeImg;
    @BindView(R.id.s_questionUpload_questionType)
    Spinner questionTypeS;
    @BindView(R.id.s_questionUpload_level)
    Spinner levelS;
    @BindView(R.id.et_questionUpload_limited)
    EditText limitedEt;
    @BindView(R.id.bt_questionUpload_save)
    Button saveBtn;


    String questionType;

    File file;//存储选中的图片文件
    HashMap<String, RequestBody> questionParams;//储存问题参数
    QuestionUploadPresenter mQuestionUploadPresenter;
    UserManager mUserManager;
    RequestBody questionPic;
    MultipartBody.Part body;
    Knowledge mKnowledge;
    String level;
    Integer requestCode;
    Question mQuestion;


    @Override
    protected View initContentView() {
        return getLayoutInflater().inflate(R.layout.activity_question_upload, null);
    }

    @Override
    protected void initOptions() {
        super.initOptions();
        questionParams = new HashMap<>();
        //TODO:此处课程Id用作接口测试，待实现
        questionParams.put("courseid", ParseUtil.parseRequestBody("1"));
        mQuestionUploadPresenter = new QuestionUploadPresenter(this);
        mUserManager = new UserManager(this);
        requestCode = getIntent().getIntExtra("requestCode", -1);
        if (requestCode == Constant.UPDATEQUESITON) {
            mQuestion = getIntent().getParcelableExtra("question");
            mKnowledge = getIntent().getParcelableExtra("knowledge");
            initView(mQuestion);
        }

//        选择图片
        questionPicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromAlbum();
            }
        });
//        选择知识点
        knowledgeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionUploadActivity.this, QueryKnowledgeActivity.class);
                intent.putExtra("requestCode", Constant.REQUESTKNOWLEDGEFROMQUESTION);
                startActivityForResult(intent, Constant.REQUESTKNOWLEDGEFROMQUESTION);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isParamCorrect()) {
                    String correctAnswer = "";
                    questionParams.put("teacherid", ParseUtil.parseRequestBody("" + mUserManager.getTeacher().getUserid()));
                    questionParams.put("answera", ParseUtil.parseRequestBody(answerAEt.getText().toString()));
                    questionParams.put("answerb", ParseUtil.parseRequestBody(answerBEt.getText().toString()));
                    if (answerCEt.getText() != null && !answerCEt.getText().toString().equals("")) {
                        questionParams.put("answerc", ParseUtil.parseRequestBody(answerCEt.getText().toString()));
                    }
                    if (answerDEt.getText() != null && !answerDEt.getText().toString().equals("")) {
                        questionParams.put("answerd", ParseUtil.parseRequestBody(answerDEt.getText().toString()));
                    }
                    if (answerACb.isChecked()) {
                        correctAnswer += "A,";
                    }
                    if (answerBCb.isChecked()) {
                        correctAnswer += "B,";
                    }
                    if (answerCCb.isChecked()) {
                        correctAnswer += "C,";
                    }
                    if (answerDCb.isChecked()) {
                        correctAnswer += "D,";
                    }
                    questionParams.put("correctanswer", ParseUtil.parseRequestBody(correctAnswer.substring(0, correctAnswer.length() - 1)));

                    level = (String) levelS.getSelectedItem();
                    if (level.equals("简单")) {
                        level = "1";
                    } else if (level.equals("中等")) {
                        level = "2";
                    } else {
                        level = "3";
                    }
                    questionParams.put("level", ParseUtil.parseRequestBody(level));
                    questionParams.put("questiontype", ParseUtil.parseRequestBody(questionType));
                    if (limitedEt.getText() != null && !limitedEt.getText().toString().equals("")) {
                        questionParams.put("limitedtime", ParseUtil.parseRequestBody(limitedEt.getText().toString()));
                    }
                    questionParams.put("questioncontent", ParseUtil.parseRequestBody(questionContentEt.getText().toString()));
                    if (file != null) {
                        questionPic = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        body = MultipartBody.Part.createFormData("question_pic", file.getName(), questionPic);
                    }
                    if (requestCode == Constant.UPDATEQUESITON) {
                        questionParams.put("questionid", ParseUtil.parseRequestBody("" + mQuestion.getQuestionid()));
                        mQuestionUploadPresenter.updateQuestion(questionParams, body);
                    } else {
                        mQuestionUploadPresenter.uploadQuestion(questionParams, body);
                    }
                }
            }
        });
    }

    public void initView(Question question) {
        Log.d(TAG, "initView: "+question.getQuestionpicture());
        questionContentEt.setText(question.getQuestioncontent());
        if (question.getQuestionpicture() != null) {
            Glide.with(this).load(question.getQuestionpicture())
                    .into(questionPicIv);
        }
        answerAEt.setText(question.getAnswera());
        answerBEt.setText(question.getAnswerb());
        if (question.getAnswerc() != null) {
            answerCEt.setText(question.getAnswerc());
        }
        if (question.getAnswerd() != null) {
            answerDEt.setText(question.getAnswerd());
        }

        if (question.getCorrectanswer().contains("A")) {
            answerACb.setChecked(true);
        }
        if (question.getCorrectanswer().contains("B")) {
            answerBCb.setChecked(true);
        }
        if (question.getCorrectanswer().contains("C")) {
            answerCCb.setChecked(true);
        }
        if (question.getCorrectanswer().contains("D")) {
            answerDCb.setChecked(true);
        }

        courseIdS.setSelection(0);

        knowledgeTv.setText(mKnowledge.getKnowledgecontent());
        questionParams.put("knowledgeid", ParseUtil.parseRequestBody("" + mKnowledge.getKnowledgeid()));

        if (question.getQuestiontype().equals("单选")) {
            questionTypeS.setSelection(0);
        } else if (question.getQuestiontype().equals("多选")) {
            questionTypeS.setSelection(1);
        } else {
            questionTypeS.setSelection(2);
        }

        if (question.getLevel().equals(1)) {
            levelS.setSelection(0);
        } else if (question.getLevel().equals(2)) {
            levelS.setSelection(1);
        } else {
            levelS.setSelection(2);
        }

        if (question.getLimitedtime() != null && !question.getLimitedtime().equals("")) {
            limitedEt.setText("" + question.getLimitedtime());
        }

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
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, Constant.REQUESTPIC);
    }

    /**
     * 获取从相册得到的图片
     *
     * @param requestCode 之前设置的请求码
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUESTPIC:
                if (resultCode == Constant.RESULTPIC) {
                    if (data != null) {
                        file = new File(ImageUtil.handleImage(data, this));
                        Glide.with(this).load(file)
                                .into(questionPicIv);
                    }
                }
                break;
            case Constant.REQUESTKNOWLEDGEFROMQUESTION:
                if (resultCode == Constant.RESULTKNOWLEDGETOQUESTION) {
                    if (data != null) {
                        List<Knowledge> knowledgeList = data.getParcelableArrayListExtra("result");
                        if (knowledgeList != null && knowledgeList.size() > 0) {
                            mKnowledge = knowledgeList.get(0);
                            questionParams.put("knowledgeid", ParseUtil.parseRequestBody("" + mKnowledge.getKnowledgeid()));
                            knowledgeTv.setText(mKnowledge.getKnowledgecontent());
                        }
                    }
                }
            default:
                break;
        }
    }
//TODO:

    /**
     * 判断参数是否合理
     *
     * @return
     */
    public boolean isParamCorrect() {
        if (questionContentEt.getText() == null || questionContentEt.getText().toString().equals("")) {
            ToastUtil.shortToast("试题内容不能为空");
            return false;
        }
        if (mKnowledge == null) {
            ToastUtil.shortToast("请选择知识点");
            return false;
        }
        if (answerAEt.getText() == null || answerAEt.getText().toString().equals("")) {
            ToastUtil.shortToast("请填入两个答案或以上");
            return false;
        }
        if (answerBEt.getText() == null || answerBEt.getText().toString().equals("")) {
            ToastUtil.shortToast("请填入两个答案或以上");
            return false;
        }

        /**
         * 判断题目设置与答案选项是否合理
         */
        //TODO：
        questionType = (String) questionTypeS.getSelectedItem();
        if (!answerACb.isChecked() && !answerBCb.isChecked() && !answerCCb.isChecked() && !answerDCb.isChecked()) {
            ToastUtil.shortToast("请至少选中一个正确答案");
            return false;
        }
        if ((answerACb.isChecked() && (answerAEt == null || answerAEt.getText().toString().equals(""))) || (answerBCb.isChecked() && (answerBEt == null || answerBEt.getText().toString().equals(""))) || (answerCCb.isChecked() && (answerCEt == null || answerCEt.getText().toString().equals(""))) || (answerDCb.isChecked() && (answerAEt == null || answerDEt.getText().toString().equals("")))) {
            ToastUtil.shortToast("请勿将空白答案设为正确答案");
            return false;
        }
        if (questionType.equals("单选") || questionType.equals("判断")) {
            int choice = 0;
            if (answerACb.isChecked())
                choice++;
            if (answerBCb.isChecked())
                choice++;
            if (answerCCb.isChecked())
                choice++;
            if (answerDCb.isChecked())
                choice++;
            if (choice > 1) {
                ToastUtil.shortToast("单选题或判断题只能勾选一个正确选项");
                return false;
            }
        }
        return true;
    }


}
