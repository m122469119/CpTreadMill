package com.aaron.android.codelibrary.imageloader;

import android.widget.ImageView;

/**
 * Created on 16/8/19.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class ImageConfigBuilder<BUILDER extends ImageConfigBuilder, CONFIG extends ImageConfig> {
    private Object mLoadPath = null;
    private ImageView mImageView = null;
    private ImageLoader.LoaderType mLoaderType = ImageLoader.LoaderType.NETWORK;
    private ImageLoaderCallback mImageLoaderCallback = null;
    private int mDestWidth;
    private int mDestHeight;

    public ImageConfigBuilder(ImageView imageView, Object loadPath) {
        if (imageView == null || loadPath == null) {
            throw new IllegalArgumentException("ImageView and loadPath must be not null");
        }
        mImageView = imageView;
        mLoadPath = loadPath;
    }
    protected abstract BUILDER getThis();

    protected abstract CONFIG obtainImageConfig();

    public CONFIG build() {
        validate();
        return buildImageConfig();
    }

    private CONFIG buildImageConfig() {
        return obtainImageConfig();
    }

    private void validate() {
        if (mImageView == null || mLoadPath == null) {
            throw new IllegalStateException("load image must have ImageView and Uri");
        }
    }

    public BUILDER setLoadType(ImageLoader.LoaderType loadType) {
        mLoaderType = loadType;
        return getThis();
    }

    public ImageLoader.LoaderType getLoaderType() {
        return mLoaderType;
    }

    public BUILDER setImageLoaderCallback(ImageLoaderCallback imageLoaderCallback) {
        mImageLoaderCallback = imageLoaderCallback;
        return getThis();
    }

    public ImageLoaderCallback getImageLoaderCallback() {
        return mImageLoaderCallback;
    }

    public Object getLoadPath() {
        return mLoadPath;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public int getDestWidth() {
        return mDestWidth;
    }

    public int getDestHeight() {
        return mDestHeight;
    }

    public BUILDER resize(int width, int height) {
        mDestWidth = width;
        mDestHeight = height;
        return getThis();
    }
}
