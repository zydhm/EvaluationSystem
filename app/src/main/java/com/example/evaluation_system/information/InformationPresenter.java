package com.example.evaluation_system.information;

import android.app.ProgressDialog;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.evaluation_system.base.model.Student;
import com.example.evaluation_system.common.Constant;
import com.example.evaluation_system.net.Api;
import com.example.evaluation_system.net.HttpManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class InformationPresenter {
    private static final String TAG = "InformationPresenter";
    private InfoActivity mInfoActivity;
    private SharedPreferences pref;
    RequestBody requestUserAvatarPath;
    MultipartBody.Part body;
    private ProgressDialog saving;

    InformationPresenter(InfoActivity infoActivity) {
        mInfoActivity = infoActivity;
    }

    public void updateStudent(int userId, String tel, String email) {

        //TODO:URL接口还未编写完成此处做测试用
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_URL);
        api.updateStudent(userId, tel, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Student>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Student user) {
                        mInfoActivity.mUserManager.setStudent(user);
                        saving.dismiss();
                        Toast.makeText(mInfoActivity,"保存成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        saving.dismiss();
                        Toast.makeText(mInfoActivity,"保存失败",Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "updateOnError: " + e.getMessage());
                        //getSelfListFromServer("02a618bf-0241-11e8-bd05-00163e0ac98c");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 处理图片路径
     * */

//    public String handleImage(Intent data) {
//        String imagePath = null;
//        Uri uri = data.getData();
//        if (DocumentsContract.isDocumentUri(mInfoActivity, uri)) {
//            String docId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                imagePath = getImagePath(contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            imagePath = getImagePath(uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            imagePath = uri.getPath();
//        }
//        return imagePath;
//    }
//
//    public String getImagePath(Uri uri, String selection) {
//        String path = null;
//        Cursor cursor = mInfoActivity.getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }


    /**
     * 用Luban压缩图片
     *
     * */
//    public void updateInfo(final String userId, final RequestBody userName, final long userPhone, final RequestBody userEmail, final int userAge, final File file) {
//        saving = ProgressDialog.show(mInfoActivity, "提示", "正在保存", false,true);
//        if (file != null) {
//            Luban.get(mInfoActivity)
//                    .load(file)
//                    .putGear(Luban.THIRD_GEAR)
//                    .setCompressListener(new OnCompressListener() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onSuccess(File file) {
//                            Log.d(TAG, "upFile: ok");
//                            if (file != null) {
//                                requestUserAvatarPath = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                                body = MultipartBody.Part.createFormData("avatar", file.getName(), requestUserAvatarPath);
//                            } else {
//                                body = null;
//                            }
//                            update(userId, userName, userPhone, userEmail, userAge, body);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//                    }).launch();
//        } else {
//            body = null;
//            update(userId, userName, userPhone, userEmail, userAge, body);
//        }
//    }

}
