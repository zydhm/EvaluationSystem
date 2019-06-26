package com.example.evaluation_system.base.model;

public class FinishedTestQuestionDetailVo {
    private Integer testQuestionid;

    private Boolean result;

    private String studentanswer;

    Question question;

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
