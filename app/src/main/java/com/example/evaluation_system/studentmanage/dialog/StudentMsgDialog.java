package com.example.evaluation_system.studentmanage.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evaluation_system.R;

public class StudentMsgDialog extends Dialog {
    public StudentMsgDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private View mLayout;
        private TextView studentNameTv;
        private TextView studentLoginNameTv;
        private TextView telTv;
        private TextView eMailTv;

        private StudentMsgDialog mStudentMsgDialog;
        public Builder(Context context){
            mStudentMsgDialog=new StudentMsgDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout=inflater.inflate(R.layout.student_message_dialog,null,false);
            mStudentMsgDialog.addContentView(mLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            studentNameTv= (TextView) mLayout.findViewById(R.id.tv_studentName);
            studentLoginNameTv= (TextView) mLayout.findViewById(R.id.tv_studentLoginName);
            telTv= (TextView) mLayout.findViewById(R.id.tv_tel);
            eMailTv= (TextView) mLayout.findViewById(R.id.tv_eMail);
        }

        public Builder setStudentNameTv(String studentName){
            studentNameTv.setText(studentName);
            return this;
        }

        public Builder setStudentLoginNameTv(String studentLoginName){
            studentLoginNameTv.setText(studentLoginName);
            return this;
        }

        public Builder setTelTv(String tel){
            telTv.setText(tel);
            return this;
        }

        public Builder setEMailTv(String email){
            eMailTv.setText(email);
            return this;
        }

        public StudentMsgDialog create(){
            mStudentMsgDialog.setCancelable(true);
            mStudentMsgDialog.setCanceledOnTouchOutside(true);
            return mStudentMsgDialog;
        }
    }

}
