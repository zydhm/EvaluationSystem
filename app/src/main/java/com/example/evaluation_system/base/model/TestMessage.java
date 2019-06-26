package com.example.evaluation_system.base.model;

public class TestMessage {
    private Integer studentid;

    private Integer testid;

    private Boolean completion;

    private Integer testresult;

    private String completetime;


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

    public Boolean getCompletion() {
        return completion;
    }

    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    public Integer getTestresult() {
        return testresult;
    }

    public void setTestresult(Integer testresult) {
        this.testresult = testresult;
    }

    public String getCompletetime() {
        return completetime;
    }

    public void setCompletetime(String completetime) {
        this.completetime = completetime;
    }

}
