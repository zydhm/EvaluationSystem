package com.example.evaluation_system.common;

public class Constant {

    //http网络基址
    public static final String BASE_URL = "http://10.0.2.2:8080/ssmtest2/";

    /**
     * 教师相关接口
     */
    public static final String BASE_TEACHER_URL=BASE_URL+"teacher/";
    /***********************************************************************************************************/

    /**
     * 学生相关接口
     */
    //学生登录URL
    public static final String BASE_STUDENT_URL = BASE_URL+"student/";
    /***********************************************************************************************************/

    /**
     * 题目相关接口
     */
    public static final String BASE_QUESTION_URL=BASE_URL+"question/";
    //添加题目
    public static final String BASE_UPlOAD_QUESTION_URL=BASE_QUESTION_URL+"insertAQuestion/";
    /***********************************************************************************************************/

    /**
     * 知识点相关接口
     */
    public static final String BASE_KNOWLEDGE_URL=BASE_URL+"knowledge/";

    /**
     * 班级相关接口
     */
    public static final String BASE_CLASS_URL=BASE_URL+"class/";

    /**
     * 考试相关接口
     */
    public static final String BASE_TEST_URL=BASE_URL+"test/";

    public static final int NOREQUEST=-1,REQUESTKNOWLEDGEFROMTEST=1,RESULTKNOWLEDGETOTEST=1,REQUESTPIC=2,RESULTPIC=-1,REQUESTKNOWLEDGEFROMQUESTION=3,RESULTKNOWLEDGETOQUESTION=3,REQUESTKNOWLEDGEFROMSELFTEST=5;

    public static final int REQUESTCLASSFROMTEACHER=4,RESULTCLASSTOTEACHER=4;

    public static final int UPDATEQUESITON=6;


    public static final int BASEQUESTIONNUM=5;
    public static final int SELFTESTQUESTIONNUM=5;//自测出题阈值
    public  static final int EASYWEIGHT=2,NORMALWEIGHT=3,HARDWEIGHT=5;
    public static final float BASELEVEL1=6,BASELEVEL2=10,BASELEVEL3=15;
    public static final int RELOADTIMELIMIT=2;//自测连续答题次数阈值

}
