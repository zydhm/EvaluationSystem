package com.example.evaluation_system.base.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SelfTest implements Parcelable{
    private Integer selftestid;

    private String studentanswer;

    private Boolean result;

    private Integer duration;

    public SelfTest(){};

    protected SelfTest(Parcel in) {
        if (in.readByte() == 0) {
            selftestid = null;
        } else {
            selftestid = in.readInt();
        }
        studentanswer = in.readString();
        byte tmpResult = in.readByte();
        result = tmpResult == 0 ? null : tmpResult == 1;
        if (in.readByte() == 0) {
            duration = null;
        } else {
            duration = in.readInt();
        }
    }

    public static final Creator<SelfTest> CREATOR = new Creator<SelfTest>() {
        @Override
        public SelfTest createFromParcel(Parcel in) {
            return new SelfTest(in);
        }

        @Override
        public SelfTest[] newArray(int size) {
            return new SelfTest[size];
        }
    };

    public Integer getSelftestid() {
        return selftestid;
    }

    public void setSelftestid(Integer selftestid) {
        this.selftestid = selftestid;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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
        parcel.writeString(studentanswer);
        parcel.writeByte((byte) (result == null ? 0 : result ? 1 : 2));
        if (duration == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(duration);
        }
    }
}
