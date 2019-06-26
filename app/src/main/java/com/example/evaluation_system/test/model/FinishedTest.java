package com.example.evaluation_system.test.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FinishedTest extends TestMessage implements Parcelable{
    private String testtopic;
    private String testpicture;

    public FinishedTest(){};

    protected FinishedTest(Parcel in) {
        super(in);
        testtopic = in.readString();
        testpicture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(testtopic);
        dest.writeString(testpicture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FinishedTest> CREATOR = new Creator<FinishedTest>() {
        @Override
        public FinishedTest createFromParcel(Parcel in) {
            return new FinishedTest(in);
        }

        @Override
        public FinishedTest[] newArray(int size) {
            return new FinishedTest[size];
        }
    };

    public String getTesttopic() {
        return testtopic;
    }

    public void setTesttopic(String testtopic) {
        this.testtopic = testtopic;
    }

    public String getTestpicture() {
        return testpicture;
    }

    public void setTestpicture(String testpicture) {
        this.testpicture = testpicture;
    }
}
