package com.example.evaluation_system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "AppDatabaseHelper";

    private static volatile AppDatabaseHelper sInstance;

    private static final String DB_NAME = "evaluationSystem.db";
    private static final int DB_VERSION = 1;

    public static final int DELETE_ERROR = -1;
    public static final int UPDATE_ERROR = -1;

    private static final String QUESTION_CUSTOM_TABLE_NAME = "questionCustom";

    private static final String ANSWER_TABLE_NAME = "answer";

    private static final String TEST_STATUS_TABLE_NAME = "testStatus";

    private SQLiteDatabase mDb;
    private Context mContext;


    //保存题目信息
    private static final String CREATE_TABLE_QUESTION_CUSTOM = "CREATE TABLE IF NOT EXISTS " +
            QUESTION_CUSTOM_TABLE_NAME + "(question_id INTEGER, " +
            "teacher_id INTEGER, " +
            "knowledge_id INTEGER, " +
            "course_id INTEGER, " +
            "question_content VARCHAR(255), " +
            "answerA VARCHAR(255), " +
            "answerB VARCHAR(255), " +
            "answerC VARCHAR(255), " +
            "answerD VARCHAR(255)," +
            "correct_answer VARCHAR(255), " +
            "limited_time BIGINT(20), " +
            "question_type VARCHAR(255)," +
            "question_picture VARCHAR(255)," +
            "level INTEGER," +
            "correct_rate INTEGER," +
            "testQuestion_id INTEGER )";


    //保存学生答案
    private static final String CREATE_TABLE_ANSWER = "CREATE TABLE IF NOT EXISTS " +
            ANSWER_TABLE_NAME + "(student_id INTEGER , " +
            "test_id INTEGER , " +
            "question_id INTEGER, " +
            "testQuestion_id INTEGER," +
            "result BOOLEAN, " +
            "student_answer VARCHAR(255), " +
            "Primary KEY(student_id,test_id,question_id))";


    //保存当前考试状态信息
    private static final String CREATE_TABLE_TEST_STATUS = "CREATE TABLE IF NOT EXISTS " +
            TEST_STATUS_TABLE_NAME + "(testStatus_id INTEGER PRIMARY KEY, " +
            "time INTEGER , " +
            "test_id INTEGER," +
            "testQuestion_id INTEGER, " +
            "reload_time INTEGER )";

    private AppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDb = getWritableDatabase();
        mContext = context;
    }

    public static AppDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabaseHelper.class) {
                if (sInstance == null) {
                    sInstance = new AppDatabaseHelper(context);
                }
            }
        }
        return sInstance;
    }

    private boolean checkTableName(String tableName) {
        switch (tableName) {
            case QUESTION_CUSTOM_TABLE_NAME:
                return true;
            case ANSWER_TABLE_NAME:
                return true;
            case TEST_STATUS_TABLE_NAME:
                return true;
            default:
                return false;
        }
    }

    public Cursor query(String table, Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION_CUSTOM);
        db.execSQL(CREATE_TABLE_ANSWER);
        db.execSQL(CREATE_TABLE_TEST_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String table, Uri uri, ContentValues values) {
        mDb.insert(table, null, values);
    }

    public int delete(String table, Uri uri, String selection, String[] selectionArgs) {
        return mDb.delete(table, selection, selectionArgs);
    }

    public int update(String table, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDb.update(table, values, selection, selectionArgs);
    }

    public void deleteAllData() {
        mDb.delete("answer", null, null);
        mDb.delete("questionCustom", null, null);
        mDb.delete("testStatus", null, null);
    }

    public Boolean isTestStatusEmpty() {
        Cursor cursor = mDb.query("testStatus", null, null, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}
