package com.example.evaluation_system.base.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sq implements Parcelable{
    private Integer selftestid;

    private Integer questionid;

    private Integer testQuestionid;

    private String studentanswer;

    private Boolean result;

    public Sq(){};


    protected Sq(Parcel in) {
        if (in.readByte() == 0) {
            selftestid = null;
        } else {
            selftestid = in.readInt();
        }
        if (in.readByte() == 0) {
            questionid = null;
        } else {
            questionid = in.readInt();
        }
        if (in.readByte() == 0) {
            testQuestionid = null;
        } else {
            testQuestionid = in.readInt();
        }
        studentanswer = in.readString();
        byte tmpResult = in.readByte();
        result = tmpResult == 0 ? null : tmpResult == 1;
    }

    public static final Creator<Sq> CREATOR = new Creator<Sq>() {
        @Override
        public Sq createFromParcel(Parcel in) {
            return new Sq(in);
        }

        @Override
        public Sq[] newArray(int size) {
            return new Sq[size];
        }
    };

    public Integer getSelftestid() {
        return selftestid;
    }

    public void setSelftestid(Integer selftestid) {
        this.selftestid = selftestid;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (selftestid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(selftestid);
        }
        if (questionid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(questionid);
        }
        if (testQuestionid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(testQuestionid);
        }
        parcel.writeString(studentanswer);
        parcel.writeByte((byte) (result == null ? 0 : result ? 1 : 2));
    }

}
