package com.example.evaluation_system.base.model;

import java.util.ArrayList;

public class KnowledgeQueryVo {
    private String chaptername;

    private ArrayList<Knowledge> knowledgeList;

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    public ArrayList<Knowledge> getKnowledgeList() {
        return knowledgeList;
    }

    public void setKnowledgeList(ArrayList<Knowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }
}
