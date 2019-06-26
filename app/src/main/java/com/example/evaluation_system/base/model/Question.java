package com.example.evaluation_system.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Question implements Parcelable{
    private Integer questionid;

    private Integer teacherid;

    private Integer knowledgeid;

    private Integer courseid;

    private String questioncontent;

    private String answera;

    private String answerb;

    private String answerc;

    private String answerd;

    private String correctanswer;

    private Integer limitedtime;

    private String questiontype;

    private String questionpicture;

    private Integer level;

    private Integer correctrate;

    public Question(){};


    protected Question(Parcel in) {
        if (in.readByte() == 0) {
            questionid = null;
        } else {
            questionid = in.readInt();
        }
        if (in.readByte() == 0) {
            teacherid = null;
        } else {
            teacherid = in.readInt();
        }
        if (in.readByte() == 0) {
            knowledgeid = null;
        } else {
            knowledgeid = in.readInt();
        }
        if (in.readByte() == 0) {
            courseid = null;
        } else {
            courseid = in.readInt();
        }
        questioncontent = in.readString();
        answera = in.readString();
        answerb = in.readString();
        answerc = in.readString();
        answerd = in.readString();
        correctanswer = in.readString();
        if (in.readByte() == 0) {
            limitedtime = null;
        } else {
            limitedtime = in.readInt();
        }
        questiontype = in.readString();
        questionpicture = in.readString();
        if (in.readByte() == 0) {
            level = null;
        } else {
            level = in.readInt();
        }
        if (in.readByte() == 0) {
            correctrate = null;
        } else {
            correctrate = in.readInt();
        }
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public Integer getKnowledgeid() {
        return knowledgeid;
    }

    public void setKnowledgeid(Integer knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public String getQuestioncontent() {
        return questioncontent;
    }

    public void setQuestioncontent(String questioncontent) {
        this.questioncontent = questioncontent;
    }

    public String getAnswera() {
        return answera;
    }

    public void setAnswera(String answera) {
        this.answera = answera;
    }

    public String getAnswerb() {
        return answerb;
    }

    public void setAnswerb(String answerb) {
        this.answerb = answerb;
    }

    public String getAnswerc() {
        return answerc;
    }

    public void setAnswerc(String answerc) {
        this.answerc = answerc;
    }

    public String getAnswerd() {
        return answerd;
    }

    public void setAnswerd(String answerd) {
        this.answerd = answerd;
    }

    public String getCorrectanswer() {
        return correctanswer;
    }

    public void setCorrectanswer(String correctanswer) {
        this.correctanswer = correctanswer;
    }

    public Integer getLimitedtime() {
        return limitedtime;
    }

    public void setLimitedtime(Integer limitedtime) {
        this.limitedtime = limitedtime;
    }

    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }

    public String getQuestionpicture() {
        return questionpicture;
    }

    public void setQuestionpicture(String questionpicture) {
        this.questionpicture = questionpicture;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCorrectrate() {
        return correctrate;
    }

    public void setCorrectrate(Integer correctrate) {
        this.correctrate = correctrate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (questionid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(questionid);
        }
        if (teacherid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(teacherid);
        }
        if (knowledgeid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(knowledgeid);
        }
        if (courseid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(courseid);
        }
        parcel.writeString(questioncontent);
        parcel.writeString(answera);
        parcel.writeString(answerb);
        parcel.writeString(answerc);
        parcel.writeString(answerd);
        parcel.writeString(correctanswer);
        if (limitedtime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(limitedtime);
        }
        parcel.writeString(questiontype);
        parcel.writeString(questionpicture);
        if (level == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(level);
        }
        if (correctrate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(correctrate);
        }
    }
}
