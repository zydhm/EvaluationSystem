package com.example.evaluation_system.test.teacher.testMain.testdetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.model.TestDetailCustom;
import com.example.evaluation_system.images.ImageManager;


public class QuestionAdapter extends BaseExpandableListAdapter {

    TestDetailCustom mTestDetailCustom;

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return mTestDetailCustom.getQuestionList().size();
    }

    @Override
    public Object getGroup(int i) {
        return mTestDetailCustom;
    }

    @Override
    public Object getChild(int i, int i1) {
        return mTestDetailCustom.getQuestionList().get(i1);
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
            view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_item_test_detail,null);
            parentViewHolder.mTextView= (TextView) view.findViewById(R.id.tv_parent_item_test_detail);
            view.setTag(parentViewHolder);
        } else{
            parentViewHolder= (ParentViewHolder) view.getTag();
        }
        parentViewHolder.mTextView.setText("题目详情(共"+mTestDetailCustom.getQuestionnumber()+"题)");
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        QuestionAdapter.ChildViewHolder childViewHolder;
        if (view == null) {
            childViewHolder = new QuestionAdapter.ChildViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item_test_detail, null);
            childViewHolder.mQuestionTypeTv = (TextView) view.findViewById(R.id.tv_selfTestResult_questionType);
            childViewHolder.mQuestionContentTv = (TextView) view.findViewById(R.id.tv_selfTestResult_questionContent);
            childViewHolder.mQuestionIv = (ImageView) view.findViewById(R.id.iv_selfTestResult_questionPic);
            childViewHolder.mAnswerACb = (CheckBox) view.findViewById(R.id.cb_selfTestResult_answerA);
            childViewHolder.mAnswerATv = (TextView) view.findViewById(R.id.tv_selfTestResult_answerA);
            childViewHolder.mAnswerBCb = (CheckBox) view.findViewById(R.id.cb_selfTestResult_answerB);
            childViewHolder.mAnswerBTv = (TextView) view.findViewById(R.id.tv_selfTestResult_answerB);
            childViewHolder.mAnswerCCb = (CheckBox) view.findViewById(R.id.cb_selfTestResult_answerC);
            childViewHolder.mAnswerCTv = (TextView) view.findViewById(R.id.tv_selfTestResult_answerC);
            childViewHolder.mAnswerDCb = (CheckBox) view.findViewById(R.id.cb_selfTestResult_answerD);
            childViewHolder.mAnswerDTv = (TextView) view.findViewById(R.id.tv_selfTestResult_answerD);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (QuestionAdapter.ChildViewHolder) view.getTag();
        }

        childViewHolder.mQuestionTypeTv.setText(mTestDetailCustom.getQuestionList().get(i1).getQuestiontype());
        childViewHolder.mQuestionContentTv.setText(mTestDetailCustom.getQuestionList().get(i1).getQuestioncontent());
        if (mTestDetailCustom.getQuestionList().get(i1).getQuestionpicture() != null && mTestDetailCustom.getQuestionList().get(i1).getQuestionpicture() != "") {
            childViewHolder.mQuestionIv.setVisibility(View.VISIBLE);
            ImageManager.getInstance().loadImage(viewGroup.getContext(),
                    mTestDetailCustom.getQuestionList().get(i1).getQuestionpicture(), childViewHolder.mQuestionIv);
        } else {
            childViewHolder.mQuestionIv.setVisibility(View.GONE);
        }

        if(mTestDetailCustom.getQuestionList().get(i1).getCorrectanswer().contains("A")){
            childViewHolder.mAnswerACb.setChecked(true);
        }else {
            childViewHolder.mAnswerACb.setChecked(false);
        }
        childViewHolder.mAnswerATv.setText(mTestDetailCustom.getQuestionList().get(i1).getAnswera());

        if(mTestDetailCustom.getQuestionList().get(i1).getCorrectanswer().contains("B")){
            childViewHolder.mAnswerBCb.setChecked(true);
        }else {
            childViewHolder.mAnswerBCb.setChecked(false);
        }
        childViewHolder.mAnswerBTv.setText(mTestDetailCustom.getQuestionList().get(i1).getAnswerb());

        if(mTestDetailCustom.getQuestionList().get(i1).getAnswerc()!=null&&!mTestDetailCustom.getQuestionList().get(i1).getAnswerc().equals(""))
        {
            childViewHolder.mAnswerCCb.setVisibility(View.VISIBLE);
            childViewHolder.mAnswerCTv.setVisibility(View.VISIBLE);
            if (mTestDetailCustom.getQuestionList().get(i1).getCorrectanswer().contains("C")) {
                childViewHolder.mAnswerCCb.setChecked(true);
            } else {
                childViewHolder.mAnswerCCb.setChecked(false);
            }
            childViewHolder.mAnswerCTv.setText(mTestDetailCustom.getQuestionList().get(i1).getAnswerc());
        }else {
            childViewHolder.mAnswerCCb.setVisibility(View.GONE);
            childViewHolder.mAnswerCTv.setVisibility(View.GONE);
        }
        if(mTestDetailCustom.getQuestionList().get(i1).getAnswerd()!=null&&!mTestDetailCustom.getQuestionList().get(i1).equals("")) {
            childViewHolder.mAnswerDCb.setVisibility(View.VISIBLE);
            childViewHolder.mAnswerDTv.setVisibility(View.VISIBLE);
            if (mTestDetailCustom.getQuestionList().get(i1).getCorrectanswer().contains("D")) {
                childViewHolder.mAnswerDCb.setChecked(true);
            } else {
                childViewHolder.mAnswerDCb.setChecked(false);
            }
            childViewHolder.mAnswerDTv.setText(mTestDetailCustom.getQuestionList().get(i1).getAnswerd());
        }else{
            childViewHolder.mAnswerDCb.setVisibility(View.GONE);
            childViewHolder.mAnswerDTv.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    public class ParentViewHolder{
        TextView mTextView;
    }

    public class ChildViewHolder{
        TextView mQuestionTypeTv;
        TextView mQuestionContentTv;
        ImageView mQuestionIv;
        CheckBox mAnswerACb;
        TextView mAnswerATv;
        CheckBox mAnswerBCb;
        TextView mAnswerBTv;
        CheckBox mAnswerCCb;
        TextView mAnswerCTv;
        CheckBox mAnswerDCb;
        TextView mAnswerDTv;
    }

    public void setTestDetailCustom(TestDetailCustom testDetailCustom){
        mTestDetailCustom=testDetailCustom;
        notifyDataSetChanged();;
    }
}
