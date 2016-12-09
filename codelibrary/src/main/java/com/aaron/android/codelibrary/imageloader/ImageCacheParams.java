package com.aaron.android.codelibrary.imageloader;

import android.content.Context;

/**
 * Created on 16/3/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class ImageCacheParams {
    private int mMaxMemoryCacheSize;
    private long mMaxDiskCacheSize;
    private String mDirectoryPath;
    private Context mContext;
    private String mDirectoryName;

    public ImageCacheParams(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public int getMaxMemoryCacheSize() {
        return mMaxMemoryCacheSize;
    }

    public void setMaxMemoryCacheSize(int maxMemoryCacheSize) {
        mMaxMemoryCacheSize = maxMemoryCacheSize;
    }

    public long getMaxDiskCacheSize() {
        return mMaxDiskCacheSize;
    }

    public void setMaxDiskCacheSize(long maxDiskCacheSize) {
        mMaxDiskCacheSize = maxDiskCacheSize;
    }

    public String getDirectoryPath() {
        return mDirectoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        mDirectoryPath = directoryPath;
    }

    public String getDirectoryName() {
        return mDirectoryName;
    }

    public void setDirectoryName(String directoryName) {
        mDirectoryName = directoryName;
    }
}
