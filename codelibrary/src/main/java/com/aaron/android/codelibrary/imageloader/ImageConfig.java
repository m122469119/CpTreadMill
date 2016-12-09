package com.aaron.android.codelibrary.imageloader;

import android.widget.ImageView;

/**
 * Created on 15/6/16.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public abstract class ImageConfig {
    private Object mLoadPath;
    private ImageLoaderCallback mImageLoaderCallback;
    private ImageView mImageView;
    private ImageLoader.LoaderType mLoaderType;
    private int mDestWidth;
    private int mDestHeight;

    public ImageConfig(ImageConfigBuilder builder) {
        mLoadPath = builder.getLoadPath();
        mImageView = builder.getImageView();
        mImageLoaderCallback = builder.getImageLoaderCallback();
        mLoaderType = builder.getLoaderType();
        mDestHeight = builder.getDestHeight();
        mDestWidth = builder.getDestWidth();
    }

    /**
     * 需要的Bitmap尺寸高度
     * @return destHeight
     */
    public int getDestHeight() {
        return mDestHeight;
    }

    /**
     * 需要的Bitmap尺寸宽度
     * @return destWidth
     */
    public int getDestWidth() {
        return mDestWidth;
    }

    /**
     * 加载图片的ImageView
     * @return ImageView
     */
    public ImageView getImageView() {
        return mImageView;
    }

    /**
     * 图片加载路径,可能是一个string path,也可能是本地resource(R.drawable.xxx) int类型的id
     * @return 图片加载路径
     */
    public Object getLoadPath() {
        return mLoadPath;
    }

    /**
     * 图片加载类型
     * @return 需要从什么位置加载(file, contentProvider, assets, 网络, 本地R.drawable.xxx)
     */
    public ImageLoader.LoaderType getLoaderType() {
        return mLoaderType;
    }

    /**
     * 加载图片完成后的回调
     * @return ImageLoaderCallback
     */
    public ImageLoaderCallback getImageLoaderCallback() {
        return mImageLoaderCallback;
    }

    /**
     * 设置加载图片完成后的回调
     * @param imageLoaderCallback ImageLoaderCallback
     */
    public void setImageLoaderCallback(ImageLoaderCallback imageLoaderCallback) {
        mImageLoaderCallback = imageLoaderCallback;
    }

}
