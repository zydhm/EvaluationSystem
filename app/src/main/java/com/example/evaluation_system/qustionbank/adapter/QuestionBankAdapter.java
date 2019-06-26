package com.example.evaluation_system.qustionbank.adapter;

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

public class QuestionBankAdapter extends BaseExpandableListAdapter {

    ArrayList<String> mChapterContentArrayList;
    ArrayList<ArrayList<Knowledge>> mKnowledgeArrayLists;

    public QuestionBankAdapter(){
        mChapterContentArrayList=new ArrayList<String>();
        mKnowledgeArrayLists=new ArrayList<ArrayList<Knowledge>>();
    }


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
        return mKnowledgeArrayLists.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ParentViewHolder parentViewHolder;
        if(view==null){
            parentViewHolder=new ParentViewHolder();
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_item_knowledge_select,viewGroup,false);
            parentViewHolder.chapterTv= (TextView) view.findViewById(R.id.tv_parentItem);
            parentViewHolder.parentItemRl= (RelativeLayout) view.findViewById(R.id.rl_parentItem);
            view.setTag(parentViewHolder);
        }else{
            parentViewHolder= (ParentViewHolder) view.getTag();
        }
        parentViewHolder.chapterTv.setText(mChapterContentArrayList.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if(view==null){
            childViewHolder=new ChildViewHolder();
            view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item_knowledge_select,viewGroup,false);
            childViewHolder.knowledgeTv= (TextView) view.findViewById(R.id.tv_childItem);
            childViewHolder.childItemRl= (RelativeLayout) view.findViewById(R.id.rl_childItem);
            childViewHolder.knowledgeCb=(CheckBox)view.findViewById(R.id.cb_childItem);
            view.setTag(childViewHolder);
        }else{
            childViewHolder= (ChildViewHolder) view.getTag();
        }
        childViewHolder.knowledgeTv.setText(mKnowledgeArrayLists.get(i).get(i1).getKnowledgecontent());
//        childViewHolder.knowledgeCb.setClickable(false);
        childViewHolder.knowledgeCb.setVisibility(View.GONE);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setChapterContentArrayList(ArrayList<String> chapterContentArrayList){
        mChapterContentArrayList=chapterContentArrayList;
        notifyDataSetChanged();
    }

    public void setKnowledgeArrayLists(ArrayList<ArrayList<Knowledge>> knowledgeArrayLists){
        mKnowledgeArrayLists=knowledgeArrayLists;
        notifyDataSetChanged();
    }

    public class ParentViewHolder{
        RelativeLayout parentItemRl;
        TextView chapterTv;
    }

    public class ChildViewHolder{
        RelativeLayout childItemRl;
        TextView knowledgeTv;
        CheckBox knowledgeCb;
    }
}
