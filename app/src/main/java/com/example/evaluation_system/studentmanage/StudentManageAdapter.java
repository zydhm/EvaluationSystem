package com.example.evaluation_system.studentmanage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.model.Student;

import java.util.ArrayList;

public class StudentManageAdapter extends BaseExpandableListAdapter {

    ArrayList<ArrayList<Student>> mStudentArrayList;
    ArrayList<String> mClassNameArrayList;

    public StudentManageAdapter(){
        mStudentArrayList=new ArrayList<>();
        mClassNameArrayList=new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return mClassNameArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mStudentArrayList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mClassNameArrayList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mStudentArrayList.get(i).get(i1);
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
            parentViewHolder.classNameTv= (TextView) view.findViewById(R.id.tv_parentItem);
            view.setTag(parentViewHolder);
        }else{
            parentViewHolder= (ParentViewHolder) view.getTag();
        }
        parentViewHolder.classNameTv.setText(mClassNameArrayList.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if(view==null){
            childViewHolder=new ChildViewHolder();
            view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item_knowledge_select,viewGroup,false);
            childViewHolder.studentNameTv= (TextView) view.findViewById(R.id.tv_childItem);
            childViewHolder.studentNameCb=(CheckBox)view.findViewById(R.id.cb_childItem);
            view.setTag(childViewHolder);
        }else{
            childViewHolder= (ChildViewHolder) view.getTag();
        }
        childViewHolder.studentNameTv.setText(mStudentArrayList.get(i).get(i1).getUsername());
        childViewHolder.studentNameCb.setVisibility(View.GONE);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class ParentViewHolder{
        TextView classNameTv;
    }

    public class ChildViewHolder{
        TextView studentNameTv;
        CheckBox studentNameCb;
    }

    public void setStudentArrayList(ArrayList<ArrayList<Student>>studentArrayList){
        mStudentArrayList=studentArrayList;
        notifyDataSetChanged();
    }

    public void setClassNameArrayList(ArrayList<String> classNameArrayList){
        mClassNameArrayList=classNameArrayList;
        notifyDataSetChanged();
    }
}
