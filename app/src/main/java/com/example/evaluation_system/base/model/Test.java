package com.example.evaluation_system.base.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Test implements Parcelable{
    private Integer testid;

    private Integer teacherid;

    private Integer courseid;

    private String knowledges;

    private String testtopic;

    private String testpicture;

    private String begin;

    private String end;

    private Integer questionnumber;

    private Integer singlechoiceratio;

    private Integer multiplechoiceratio;

    private Integer judgeratio;

    private Integer easyratio;

    private Integer normalratio;

    private Integer hardratio;

    private String classes;

    public Test(){};

    protected Test(Parcel in) {
        if (in.readByte() == 0) {
            testid = null;
        } else {
            testid = in.readInt();
        }
        if (in.readByte() == 0) {
            teacherid = null;
        } else {
            teacherid = in.readInt();
        }
        if (in.readByte() == 0) {
            courseid = null;
        } else {
            courseid = in.readInt();
        }
        knowledges = in.readString();
        testtopic = in.readString();
        testpicture = in.readString();
        begin = in.readString();
        end = in.readString();
        if (in.readByte() == 0) {
            questionnumber = null;
        } else {
            questionnumber = in.readInt();
        }
        if (in.readByte() == 0) {
            singlechoiceratio = null;
        } else {
            singlechoiceratio = in.readInt();
        }
        if (in.readByte() == 0) {
            multiplechoiceratio = null;
        } else {
            multiplechoiceratio = in.readInt();
        }
        if (in.readByte() == 0) {
            judgeratio = null;
        } else {
            judgeratio = in.readInt();
        }
        if (in.readByte() == 0) {
            easyratio = null;
        } else {
            easyratio = in.readInt();
        }
        if (in.readByte() == 0) {
            normalratio = null;
        } else {
            normalratio = in.readInt();
        }
        if (in.readByte() == 0) {
            hardratio = null;
        } else {
            hardratio = in.readInt();
        }
        classes = in.readString();
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public Integer getTestid() {
        return testid;
    }

    public void setTestid(Integer testid) {
        this.testid = testid;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public String getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(String knowledges) {
        this.knowledges = knowledges;
    }

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

    public Integer getQuestionnumber() {
        return questionnumber;
    }

    public void setQuestionnumber(Integer questionnumber) {
        this.questionnumber = questionnumber;
    }

    public Integer getSinglechoiceratio() {
        return singlechoiceratio;
    }

    public void setSinglechoiceratio(Integer singlechoiceratio) {
        this.singlechoiceratio = singlechoiceratio;
    }

    public Integer getMultiplechoiceratio() {
        return multiplechoiceratio;
    }

    public void setMultiplechoiceratio(Integer multiplechoiceratio) {
        this.multiplechoiceratio = multiplechoiceratio;
    }

    public Integer getJudgeratio() {
        return judgeratio;
    }

    public void setJudgeratio(Integer judgeratio) {
        this.judgeratio = judgeratio;
    }

    public Integer getEasyratio() {
        return easyratio;
    }

    public void setEasyratio(Integer easyratio) {
        this.easyratio = easyratio;
    }

    public Integer getNormalratio() {
        return normalratio;
    }

    public void setNormalratio(Integer normalratio) {
        this.normalratio = normalratio;
    }

    public Integer getHardratio() {
        return hardratio;
    }

    public void setHardratio(Integer hardratio) {
        this.hardratio = hardratio;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (testid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(testid);
        }
        if (teacherid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(teacherid);
        }
        if (courseid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(courseid);
        }
        parcel.writeString(knowledges);
        parcel.writeString(testtopic);
        parcel.writeString(testpicture);
        parcel.writeString(begin);
        parcel.writeString(end);
        if (questionnumber == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(questionnumber);
        }
        if (singlechoiceratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(singlechoiceratio);
        }
        if (multiplechoiceratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(multiplechoiceratio);
        }
        if (judgeratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(judgeratio);
        }
        if (easyratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(easyratio);
        }
        if (normalratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(normalratio);
        }
        if (hardratio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(hardratio);
        }
        parcel.writeString(classes);
    }
}
