package com.aaron.android.framework.library.imageloader;

import android.net.Uri;

import com.aaron.android.codelibrary.imageloader.ImageConfig;
import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;

/**
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class HImageConfig extends ImageConfig {

    private AbstractDraweeControllerBuilder mDraweeControllerBuilder;
    private Uri mUri;

    public HImageConfig(HImageConfigBuilder builder) {
        super(builder);
        mDraweeControllerBuilder = builder.getDraweeControllerBuilder();
        buildUri(builder);
    }

    private void buildUri(HImageConfigBuilder builder) {
        ImageLoader.LoaderType loaderType = builder.getLoaderType();
        if (getLoadPath() instanceof Integer) {
            loaderType = ImageLoader.LoaderType.RESOURCE;
            mUri = Uri.parse("res:///" + getLoadPath());
        }
        if (getLoadPath() instanceof String) {
            String loadPath = (String) getLoadPath();
            if (loaderType == ImageLoader.LoaderType.NETWORK) {
                mUri = Uri.parse(loadPath);
            } else if (loaderType == ImageLoader.LoaderType.ASSET) {
                mUri = Uri.parse("asset:///" + loadPath);
            } else if (loaderType == ImageLoader.LoaderType.FILE) {
                mUri = Uri.parse("file://" + loadPath);
            } else if (loaderType == ImageLoader.LoaderType.CONTENT_PROVIDER) {
                mUri = Uri.parse("content://" + loadPath);
            }
        }
    }

    public Uri getUri() {
        return mUri;
    }

    public AbstractDraweeControllerBuilder getDraweeControllerBuilder() {
        return mDraweeControllerBuilder;
    }

    @Override
    public HImageView getImageView() {
        return (HImageView) super.getImageView();
    }


}
