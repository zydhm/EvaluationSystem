package com.example.evaluation_system.base.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Knowledge implements Parcelable{
    private Integer knowledgeid;
    private Integer chapterid;
    private String knowledgecontent;

    public Knowledge(){

    }


    protected Knowledge(Parcel in) {
        if (in.readByte() == 0) {
            knowledgeid = null;
        } else {
            knowledgeid = in.readInt();
        }
        if (in.readByte() == 0) {
            chapterid = null;
        } else {
            chapterid = in.readInt();
        }
        knowledgecontent = in.readString();
    }

    public static final Creator<Knowledge> CREATOR = new Creator<Knowledge>() {
        @Override
        public Knowledge createFromParcel(Parcel in) {
            return new Knowledge(in);
        }

        @Override
        public Knowledge[] newArray(int size) {
            return new Knowledge[size];
        }
    };

    public Integer getKnowledgeid() {
        return knowledgeid;
    }

    public void setKnowledgeid(Integer knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    public Integer getChapterid() {
        return chapterid;
    }

    public void setChapterid(Integer chapterid) {
        this.chapterid = chapterid;
    }

    public String getKnowledgecontent() {
        return knowledgecontent;
    }

    public void setKnowledgecontent(String knowledgecontent) {
        this.knowledgecontent = knowledgecontent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (knowledgeid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(knowledgeid);
        }
        if (chapterid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(chapterid);
        }
        parcel.writeString(knowledgecontent);
    }
}
