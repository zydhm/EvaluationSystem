package com.example.evaluation_system.test.knowledge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.model.Knowledge;


import java.util.ArrayList;

public class KnowledgeAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "KnowledgeAdapter";

    private ArrayList<String> mChapterContentArrayList = new ArrayList<>();
    private ArrayList<ArrayList<Knowledge>> mKnowledgeArrayLists = new ArrayList<ArrayList<Knowledge>>();
    private ArrayList<ArrayList<Boolean>> mIsCheckArrayLists = new ArrayList<ArrayList<Boolean>>();


    @Override
    public int getGroupCount() {
        return mChapterContentArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mKnowledgeArrayLists.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mChapterContentArrayList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mKnowledgeArrayLists.get(i).get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     * 分组和子项是否持有稳定id
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_item_knowledge_select, viewGroup, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.parentItemTv = (TextView) view.findViewById(R.id.tv_parentItem);
            groupViewHolder.parentItemRl = (RelativeLayout) view.findViewById(R.id.rl_parentItem);
            groupViewHolder.parentItemCb = (CheckBox) view.findViewById(R.id.cb_parentItem);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.parentItemTv.setText(mChapterContentArrayList.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item_knowledge_select, viewGroup, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.childItemTv = (TextView) view.findViewById(R.id.tv_childItem);
            childViewHolder.childItemRl = (RelativeLayout) view.findViewById(R.id.rl_childItem);
            childViewHolder.childItemCb = (CheckBox) view.findViewById(R.id.cb_childItem);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.childItemCb.setChecked(mIsCheckArrayLists.get(i).get(i1));
        childViewHolder.childItemTv.setText(mKnowledgeArrayLists.get(i).get(i1).getKnowledgecontent());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 传入章节
     *
     * @param chapterContentArrayList
     */
    public void setChapterContentArrayList(ArrayList<String> chapterContentArrayList) {
        mChapterContentArrayList = chapterContentArrayList;
        notifyDataSetChanged();
    }

    /**
     * 传入知识点
     *
     * @param knowledgeArrayLists
     */
    public void setKnowledgeArrayLists(ArrayList<ArrayList<Knowledge>> knowledgeArrayLists) {
        mKnowledgeArrayLists = knowledgeArrayLists;
        notifyDataSetChanged();
    }

    /**
     * 传入知识点选中状态
     */
    public void setIsCheckArrayLists(ArrayList<ArrayList<Boolean>> isCheckArrayLists){
        mIsCheckArrayLists=isCheckArrayLists;
    }


    static class GroupViewHolder {
        RelativeLayout parentItemRl;
        TextView parentItemTv;
        CheckBox parentItemCb;
    }

    static class ChildViewHolder {
        RelativeLayout childItemRl;
        TextView childItemTv;
        CheckBox childItemCb;
    }

    public void onChildViewClicked(View view) {
//        ((ChildViewHolder) view.getTag()).childItemCb.toggle();
        notifyDataSetChanged();
    }

}
