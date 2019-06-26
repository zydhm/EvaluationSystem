package com.example.evaluation_system.base.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SelfTestResultVo{

    private Integer testQuestionid;

    private String studentanswer;

    private Boolean result;

    private Question question;

    public Integer getTestQuestionid() {
        return testQuestionid;
    }

    public void setTestQuestionid(Integer testQuestionid) {
        this.testQuestionid = testQuestionid;
    }

    public String getStudentanswer() {
        return studentanswer;
    }

    public void setStudentanswer(String studentanswer) {
        this.studentanswer = studentanswer;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
