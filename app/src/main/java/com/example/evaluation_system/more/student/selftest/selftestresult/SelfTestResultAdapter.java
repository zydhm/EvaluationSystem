package com.example.evaluation_system.more.student.selftest.selftestresult;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evaluation_system.R;
import com.example.evaluation_system.base.model.SelfTestResultVo;
import com.example.evaluation_system.base.model.Knowledge;
import com.example.evaluation_system.images.ImageManager;

import java.util.List;

public class SelfTestResultAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "SelfTestResultAdapter";
    List<Knowledge> mKnowledgeList;
    List<List<SelfTestResultVo>> mSelfTestResultVoLists;
    List<String> mMasteryList;

    @Override
    public int getGroupCount() {
        return mKnowledgeList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mSelfTestResultVoLists.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mKnowledgeList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mSelfTestResultVoLists.get(i).get(i1);
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
        GroupViewHolder groupViewHolder;
        if (view == null) {
            groupViewHolder = new GroupViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_item_self_test_result, null);
            groupViewHolder.mKnowledgeContentTv = (TextView) view.findViewById(R.id.tv_selfTestResult_knowledgeContent);
            groupViewHolder.mMasteryTv = (TextView) view.findViewById(R.id.tv_selfTestResult_mastery);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.mKnowledgeContentTv.setText(mKnowledgeList.get(i).getKnowledgecontent());
        groupViewHolder.mMasteryTv.setText(mMasteryList.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            childViewHolder = new ChildViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item_self_test_result, null);
            childViewHolder.mResultIv = (ImageView) view.findViewById(R.id.iv_selfTestResult_result);
            childViewHolder.mQuestionTypeTv = (TextView) view.findViewById(R.id.tv_selfTestResult_questionType);
            childViewHolder.mTestQuestionIdTv = (TextView) view.findViewById(R.id.tv_selfTestResult_testQuestionId);
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
            childViewHolder.mCorrectAnswerTv = (TextView) view.findViewById(R.id.tv_selfTestResult_correctAnswer);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }

        if (mSelfTestResultVoLists.get(i).get(i1).getResult()) {
            childViewHolder.mResultIv.setImageResource(R.drawable.ic_correct);
            childViewHolder.mCorrectAnswerTv.setVisibility(View.GONE);
        } else {
            childViewHolder.mResultIv.setImageResource(R.drawable.ic_wrong);
            childViewHolder.mCorrectAnswerTv.setVisibility(View.VISIBLE);
            childViewHolder.mCorrectAnswerTv.setText("正确答案:"+mSelfTestResultVoLists.get(i).get(i1).getQuestion().getCorrectanswer());
        }

        childViewHolder.mQuestionTypeTv.setText(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getQuestiontype());
        childViewHolder.mTestQuestionIdTv.setText(""+mSelfTestResultVoLists.get(i).get(i1).getTestQuestionid());
        childViewHolder.mQuestionContentTv.setText(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getQuestioncontent());
        if (mSelfTestResultVoLists.get(i).get(i1).getQuestion().getQuestionpicture() != null && mSelfTestResultVoLists.get(i).get(i1).getQuestion().getQuestionpicture() != "") {
            childViewHolder.mQuestionIv.setVisibility(View.VISIBLE);
            ImageManager.getInstance().loadImage(viewGroup.getContext(),
                    mSelfTestResultVoLists.get(i).get(i1).getQuestion().getQuestionpicture(), childViewHolder.mQuestionIv);
        } else {
            childViewHolder.mQuestionIv.setVisibility(View.GONE);
        }

        if(mSelfTestResultVoLists.get(i).get(i1).getStudentanswer().contains("A")){
            childViewHolder.mAnswerACb.setChecked(true);
        }else {
            childViewHolder.mAnswerACb.setChecked(false);
        }
        childViewHolder.mAnswerATv.setText(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswera());

        if(mSelfTestResultVoLists.get(i).get(i1).getStudentanswer().contains("B")){
            childViewHolder.mAnswerBCb.setChecked(true);
        }else {
            childViewHolder.mAnswerBCb.setChecked(false);
        }
        childViewHolder.mAnswerBTv.setText(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerb());

        if(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerc()!=null&&!mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerc().equals(""))
        {
            childViewHolder.mAnswerCCb.setVisibility(View.VISIBLE);
            childViewHolder.mAnswerCTv.setVisibility(View.VISIBLE);
            if (mSelfTestResultVoLists.get(i).get(i1).getStudentanswer().contains("C")) {
                childViewHolder.mAnswerCCb.setChecked(true);
            } else {
                childViewHolder.mAnswerCCb.setChecked(false);
            }
            childViewHolder.mAnswerCTv.setText(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerc());
        }else {
            childViewHolder.mAnswerCCb.setVisibility(View.GONE);
            childViewHolder.mAnswerCTv.setVisibility(View.GONE);
        }
        if(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerd()!=null&&!mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerd().equals("")) {
            childViewHolder.mAnswerDCb.setVisibility(View.VISIBLE);
            childViewHolder.mAnswerDTv.setVisibility(View.VISIBLE);
            if (mSelfTestResultVoLists.get(i).get(i1).getStudentanswer().contains("D")) {
                childViewHolder.mAnswerDCb.setChecked(true);
            } else {
                childViewHolder.mAnswerDCb.setChecked(false);
            }
            childViewHolder.mAnswerDTv.setText(mSelfTestResultVoLists.get(i).get(i1).getQuestion().getAnswerd());
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

    public void setKnowledgeList(List<Knowledge> knowledgeList) {
        mKnowledgeList = knowledgeList;
        notifyDataSetChanged();
    }

    public void setSelfTestResultVoLists(List<List<SelfTestResultVo>> selfTestResultVoLists) {
        mSelfTestResultVoLists = selfTestResultVoLists;
        notifyDataSetChanged();
    }

    public void setMasteryList(List<String> masteryList) {
        mMasteryList = masteryList;
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        TextView mKnowledgeContentTv;
        TextView mMasteryTv;
    }

    static class ChildViewHolder {
        ImageView mResultIv;
        TextView mQuestionTypeTv;
        TextView mTestQuestionIdTv;
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
        TextView mCorrectAnswerTv;
    }
}
