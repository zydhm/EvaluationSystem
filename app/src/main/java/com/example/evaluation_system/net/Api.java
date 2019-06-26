package com.example.evaluation_system.net;

import com.example.evaluation_system.base.model.Answer;
import com.example.evaluation_system.base.model.ClassMsg;
import com.example.evaluation_system.base.model.FinishedTestQuestionDetailVo;
import com.example.evaluation_system.base.model.KnowledgeQueryVo;
import com.example.evaluation_system.base.model.Question;
import com.example.evaluation_system.base.model.QuestionCustom;
import com.example.evaluation_system.base.model.SelfTest;
import com.example.evaluation_system.base.model.SelfTestResultVo;
import com.example.evaluation_system.base.model.Sq;
import com.example.evaluation_system.base.model.Student;
import com.example.evaluation_system.base.model.Teacher;
import com.example.evaluation_system.base.model.Test;
import com.example.evaluation_system.base.model.TestDetailCustom;
import com.example.evaluation_system.test.model.FinishedTest;
import com.example.evaluation_system.test.model.TestMessage;
import com.example.evaluation_system.test.model.UnFinishedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;


public interface Api {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //获取数据操作
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 通过userId得到此用户的分享，无需要用户登录
     *
     * @param userId        用户Id
     * @param courseId      课程Id
     * @param startPosition 起始位置
     * @param endPosition   末位置
     * @return Rxjava的observable对象
     */
    @GET("moment/list/{userId}/{courseId}/{startPosition}/{endPosition}")
    Observable<List<UnFinishedTest>> selectTestByUserId(@Path("userId") String userId,
                                                        @Path("courseId") String courseId,
                                                        @Path("startPosition") int startPosition,
                                                        @Path("endPosition") int endPosition);

    /**
     * 得到所有已完成的测试
     *
     * @param studentId
     * @return
     */
    @FormUrlEncoded
    @POST("queryFinishedTestByStudentId")
    Observable<List<FinishedTest>> selectFinishedTest(@Field("studentId") Integer studentId);

    /**
     * 得到所有未完成的测试
     *
     * @param studentId
     * @return
     */
    @FormUrlEncoded
    @POST("queryUnFinishedTestByStudentId")
    Observable<List<Test>> selectUnFinishedTest(@Field("studentId") Integer studentId);


    /////////////////////////////////////////////////////////////////////////////////////////////////
    //用户数据操作
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 学生登录
     *
     * @param loginName 用户名
     * @param password  用户密码
     * @return Rxjava的observable对象
     */
    @GET("studentLogin/{loginName}/{password}")
    Observable<Student> studentLogin(@Path("loginName") String loginName,
                                     @Path("password") String password);

    /**
     * 老师登录
     *
     * @param loginName 用户名
     * @param password  用户密码
     * @return Rxjava的observable对象
     */
    @GET("teacherLogin/{loginName}/{password}")
    Observable<Teacher> teacherLogin(@Path("loginName") String loginName,
                                     @Path("password") String password);

    /**
     * 更新学生信息
     *
     * @param userId 用户Id
     * @return Rxjava的observable对象
     */
    @POST("{userId}")
    Observable<Student> updateStudent(@Path("userId") int userId,
                                      @Part("userPhone") String tel,
                                      @Part("userEmail") String email);


    /**
     * @param questionParams 问题参数
     * @param questionPic    图片文件
     * @return
     */
    @Multipart
    @POST("insertAQuestion")
    Observable<Question> uploadAQuestion(@PartMap Map<String, RequestBody> questionParams,
                                         @Part MultipartBody.Part questionPic);

    /**
     * 通过课程Id获取知识点
     *
     * @param courseId
     * @return
     */
    @FormUrlEncoded
    @POST("selectKnowledgeByCourseId")
    Observable<List<KnowledgeQueryVo>> queryKnowledge(@Field("courseId") String courseId);

    /**
     * 通过课程Id获取班级
     *
     * @param courseId
     * @return
     */
    @FormUrlEncoded
    @POST("selectClassByCourseId")
    Observable<List<ClassMsg>> selectClassByCourseId(@Field("courseId") Integer courseId);

    /**
     * 新建一个考试信息
     *
     * @param paramsMap
     * @param testPic
     * @return
     */
    @Multipart
    @POST("insertATest")
    Observable<Test> insertATest(@PartMap Map<String, RequestBody> paramsMap,
                                 @Part MultipartBody.Part testPic);

    /**
     * 根据试卷Id获取试卷包含的考试题
     *
     * @param testId
     * @param studentId
     * @return
     */
    @FormUrlEncoded
    @POST("selectQuestionByTestId")
    Observable<List<QuestionCustom>> selectQuestionByTestId(@Field("testId") Integer testId,
                                                            @Field("studentId") Integer studentId);

    /**
     * 插入一条答题信息
     *
     * @param paramsMap
     * @return
     */
    @Multipart
    @POST("insertAnAnswer")
    Observable<Answer> insertAnAnswer(@PartMap Map<String, RequestBody> paramsMap);


    /**
     * 插入一条学生完成考试的信息
     *
     * @param paramsMap
     * @return
     */
    @Multipart
    @POST("updateATestMessage")
    Observable<TestMessage> updateATestMessage(@PartMap Map<String, RequestBody> paramsMap);

    /**
     * 插入学生所有答案
     *
     * @param answers
     * @return
     */
    @POST("insertAnswers")
    Observable<List<Answer>> insertAnswers(@Body RequestBody answers);

    /**
     * 获取服务器时间
     *
     * @return
     */
    @POST("getServerTime")
    Observable<String> getServerTime();

    /**
     * 插入一条自测信息
     *
     * @param studentId
     * @param knowledges
     * @return
     */
    @FormUrlEncoded
    @POST("insertASelfTest")
    Observable<SelfTest> insertASelfTest(@Field("studentid") Integer studentId,
                                         @Field("knowledges") String knowledges);

    /**
     * 选择一条自测题目
     *
     * @param preQuestionIdList
     * @param knowledgeId
     * @param level
     * @return
     */
    @FormUrlEncoded
    @POST("selectASelfTestQuestion")
    Observable<Question> selectASelfTestQuestion(@Field("[]preQuestionIds") List<Integer> preQuestionIdList,
                                                 @Field("knowledgeId") Integer knowledgeId,
                                                 @Field("level") Integer level);

    /**
     * 插入一条自测题目
     *
     * @param paramsMap
     * @return
     */
    @Multipart
    @POST("insertASq")
    Observable<Sq> insertASq(@PartMap Map<String, RequestBody> paramsMap);


    /**
     * 查看自测结果
     *
     * @param selfTestId
     * @return
     */
    @FormUrlEncoded
    @POST("selectSelfTestResult")
    Observable<List<SelfTestResultVo>> selectSelfTestResult(@Field("selfTestId") Integer selfTestId);

    /**
     * 根据教师Id查找试卷
     *
     * @param teacherId
     * @return
     */
    @FormUrlEncoded
    @POST("selectTestByTeacherId")
    Observable<ArrayList<Test>> selectTestByTeacherId(@Field("teacherId") Integer teacherId);

    /**
     * 根据试卷Id获取考试详细信息
     *
     * @param testId
     * @return
     */
    @FormUrlEncoded
    @POST("selectATestDetailCustomByTestId")
    Observable<TestDetailCustom> selectATestDetailCustomByTestId(@Field("testId") Integer testId);

    @FormUrlEncoded
    @POST("selectFinishedTestQuestionDetailVo")
    Observable<ArrayList<FinishedTestQuestionDetailVo>> selectFinishedTestQuestionDetailVo(@Field("testId") Integer testId,
                                                                                @Field("studentId") Integer studentId);

    @FormUrlEncoded
    @POST("selectQuestionsByKnowledgeId")
    Observable<ArrayList<Question>> selectQuestionsByKnowledgeId(@Field("knowledgeId") Integer knowledgeId);

    @Multipart
    @POST("updateAQuestion")
    Observable<Question> updateAQuestion(@PartMap Map<String, RequestBody> questionParams,
                                         @Part MultipartBody.Part questionPic);


    @FormUrlEncoded
    @POST("selectStudentByTeacherId")
    Observable<ArrayList<Student>> selectStudentByTeacherId(@Field("teacherId") Integer teacherId);
}
