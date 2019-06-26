package com.example.evaluation_system.test.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TestMessage implements Parcelable{
    private Integer studentid;
    private Integer testid;
    private Boolean completion;
    private Integer testresult;
    private String completetime;
    private String version;


    public TestMessage(){};

    protected TestMessage(Parcel in) {
        if (in.readByte() == 0) {
            studentid = null;
        } else {
            studentid = in.readInt();
        }
        if (in.readByte() == 0) {
            testid = null;
        } else {
            testid = in.readInt();
        }
        byte tmpCompletion = in.readByte();
        completion = tmpCompletion == 0 ? null : tmpCompletion == 1;
        if (in.readByte() == 0) {
            testresult = null;
        } else {
            testresult = in.readInt();
        }
        completetime = in.readString();
        version = in.readString();
    }

    public static final Creator<TestMessage> CREATOR = new Creator<TestMessage>() {
        @Override
        public TestMessage createFromParcel(Parcel in) {
            return new TestMessage(in);
        }

        @Override
        public TestMessage[] newArray(int size) {
            return new TestMessage[size];
        }
    };

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (studentid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(studentid);
        }
        if (testid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(testid);
        }
        parcel.writeByte((byte) (completion == null ? 0 : completion ? 1 : 2));
        if (testresult == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(testresult);
        }
        parcel.writeString(completetime);
        parcel.writeString(version);
    }
}
