package com.example.evaluation_system.base.model;

public class Answer {//this is a test line from local
    //i add a line;
    private Integer studentid;

    private Integer testid;

    private Integer questionid;

    private Integer testQuestionid;

    private Boolean result;

    private String studentanswer;

    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(Integer studentid) {
        this.studentid = studentid;
    }

    public Integer getTestid() {
        return testid;
    }

    public void setTestid(Integer testid) {
        this.testid = testid;
    }

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public Integer getTestQuestionid() {
        return testQuestionid;
    }

    public void setTestQuestionid(Integer testQuestionid) {
        this.testQuestionid = testQuestionid;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getStudentanswer() {
        return studentanswer;
    }

    public void setStudentanswer(String studentanswer) {
        this.studentanswer = studentanswer;
    }
}
