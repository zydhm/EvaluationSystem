package com.example.evaluation_system.test.model;

import android.os.Parcel;
import android.os.Parcelable;


public class UnFinishedTest implements Parcelable {
    private int testId;
    private int teacherId;
    private int courseId;
    private String testTopic;
    private String testPicture;
    private String begin;
    private String end;
    private String create_date;
    private String version;



    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTestTopic() {
        return testTopic;
    }

    public void setTestTopic(String testTopic) {
        this.testTopic = testTopic;
    }

    public String getTestPicture() {
        return testPicture;
    }

    public void setTestPicture(String testPicture) {
        this.testPicture = testPicture;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    protected UnFinishedTest(Parcel in) {
        testId = in.readInt();
        teacherId = in.readInt();
        courseId = in.readInt();
        testTopic = in.readString();
        testPicture = in.readString();
        begin = in.readString();
        end = in.readString();
        create_date = in.readString();
        version = in.readString();
    }

    public static final Creator<UnFinishedTest> CREATOR = new Creator<UnFinishedTest>() {
        @Override
        public UnFinishedTest createFromParcel(Parcel in) {
            return new UnFinishedTest(in);
        }

        @Override
        public UnFinishedTest[] newArray(int size) {
            return new UnFinishedTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(testId);
        parcel.writeInt(teacherId);
        parcel.writeInt(courseId);
        parcel.writeString(testTopic);
        parcel.writeString(testPicture);
        parcel.writeString(begin);
        parcel.writeString(end);
        parcel.writeString(create_date);
        parcel.writeString(version);
    }
}
