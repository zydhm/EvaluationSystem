package com.example.evaluation_system.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.evaluation_system.R;
import com.example.evaluation_system.base.BaseApp;

import java.util.concurrent.ExecutionException;


public class ImageManager {



    private int mPlaceHolderImgId;

    private ImageManager() {
        mPlaceHolderImgId = R.drawable.ic_portrait_test;
    };

    /**
     * 通过单例模式得到ImageManager实例
     * @return
     */
    private static volatile ImageManager mImageManager;
    public static ImageManager getInstance() {
        if (mImageManager == null) {
            synchronized (ImageManager.class) {
                if (mImageManager == null) {
                    mImageManager = new ImageManager();
                }
            }
        }
        return mImageManager;
    }

    /**
     * 加载图片到目标ImageView，使用默认的Application context
     * @param imageUrl 图片地址
     * @param targetView 目标ImageView
     */
    public void loadImage(String imageUrl, ImageView targetView) {
        this.loadImage(BaseApp.getContext(), imageUrl, targetView);
    }

    /**
     * 加载图片到目标ImageView，使用默认的图片填充，使用具体的Context
     * @param context 上下文环境
     * @param imageUrl 图片地址
     * @param targetView 目标ImageView
     */
    public void loadImage(Context context, String imageUrl, ImageView targetView) {
        this.loadImage(context, imageUrl, targetView, mPlaceHolderImgId);
    }

    public void loadImage(Context context, String imageUrl, ImageView targetView,
                          int placeHolderImgId) {
        Glide.with(context).load(imageUrl)
                .placeholder(placeHolderImgId)
                .into(targetView);
    }

    /**
     * 加载资源文件中的图片，使用具体的context
     * @param context 上下文环境
     * @param imgId 图片地址
     * @param targetView 目标ImageView
     */
    public void loadImage(Context context, int imgId, ImageView targetView) {
        Glide.with(context).load(imgId)
                .into(targetView);
    }

    /**
     * 通过图片地址得到Bitmap
     * @param context 上下文环境
     * @param imageUrl 图片地址
     * @return Bitmap对象
     */
    public Bitmap loadImage(Context context, String imageUrl) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(imageUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
