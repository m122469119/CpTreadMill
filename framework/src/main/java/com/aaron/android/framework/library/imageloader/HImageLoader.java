package com.aaron.android.framework.library.imageloader;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.widget.ImageView;

import com.aaron.android.codelibrary.imageloader.ImageCacheParams;
import com.aaron.android.codelibrary.imageloader.ImageConfig;
import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.android.codelibrary.imageloader.ImageLoaderCallback;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.library.imageloader.Supplier.MemorySupplier;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * 图片加载器，封装Fresco开源框架实现
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class HImageLoader implements ImageLoader {

    private static final String TAG = "HImageLoader";
    private final BaseControllerListener<ImageInfo> mControllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            if (imageInfo == null) {
                return;
            }
            QualityInfo qualityInfo = imageInfo.getQualityInfo();
            LogUtils.d(TAG, "Final image received! " +
                            "Size %d x %d" +
                            " Quality level %d, good enough: %s, full quality: %s, animatable : %s",
                    imageInfo.getWidth(),
                    imageInfo.getHeight(),
                    qualityInfo.getQuality(),
                    qualityInfo.isOfGoodEnoughQuality(),
                    qualityInfo.isOfFullQuality(),
                    animatable);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            LogUtils.e(TAG, "Error loading %s", id);
        }
    };

    private ImagePipelineConfig initImageLoaderConfig(ImageCacheParams params) {
        LogUtils.i("aaron", "imageLoader cache directory: " + BaseApplication.getInstance().getExternalCacheDir());
        return ImagePipelineConfig.newBuilder(BaseApplication.getInstance())
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(BaseApplication.getInstance())
                        .setBaseDirectoryPath(new File(params.getDirectoryPath()))
                        .setBaseDirectoryName(params.getDirectoryName())
                        .setMaxCacheSize(params.getMaxDiskCacheSize())
                        .build())
                .setBitmapMemoryCacheParamsSupplier(new MemorySupplier(params.getMaxMemoryCacheSize()))
                .build();
    }

    @Override
    public void initialize(ImageCacheParams params) {
        Fresco.initialize(params.getContext(), initImageLoaderConfig(params));
    }

    @Override
    public void loadImage(ImageConfig imageConfig) {
        if (imageConfig == null) {
            throw new NullPointerException("request imageConfig argument must not be null");
        }
        if (imageConfig instanceof HImageConfig) {
            setImageDraweeViewController((HImageConfig) imageConfig);
        }
    }

    /**
     * 提供一个简单的加载本地resouce资源图片的方法
     * @param view
     * @param res
     */
    @Override
    public void loadImage(ImageView view, int res) {
        loadImage(view, LoaderType.RESOURCE, res, null);
    }

    /**
     * 加载图片
     *
     * @param view                图片占位图
     * @param url                 请求Url
     * @param imageLoaderCallback 图片加载完成后回调(后处理器)
     */
    @Override
    public void loadImage(ImageView view, String url, ImageLoaderCallback imageLoaderCallback) {
        loadImage(view, null, url, imageLoaderCallback);
    }

    /**
     * 提供一个简单的加载网络图片的方法
     * @param view ImageView
     * @param url 请求地址
     */
    @Override
    public void loadImage(ImageView view, String url) {
        loadImage(view, url, null);
    }

    @Override
    public void loadImage(ImageView view, LoaderType loaderType, String path) {
        loadImage(view, loaderType, path, null);
    }

    @Override
    public void loadImage(ImageView view, LoaderType loaderType, Object path, ImageLoaderCallback imageLoaderCallback) {
        HImageConfigBuilder imageConfigBuilder = new HImageConfigBuilder(view, path);
        imageConfigBuilder.setLoadType(loaderType == null ? LoaderType.NETWORK : loaderType);
        imageConfigBuilder.setImageLoaderCallback(imageLoaderCallback);
        loadImage(imageConfigBuilder.build());
    }

    private void setImageDraweeViewController(final HImageConfig config) {
        HImageView imageView = config.getImageView();
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                .setBackgroundColor(Color.TRANSPARENT)
                .build();
        ImageRequestBuilder request = ImageRequestBuilder
                .newBuilderWithSource(config.getUri())
                .setPostprocessor(config.getImageLoaderCallback() == null ? null : new BasePostprocessor() {
                    @Override
                    public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                        return super.process(sourceBitmap, bitmapFactory);
                    }

                    @Override
                    public void process(Bitmap bitmap) {
                        super.process(bitmap);
                        config.getImageLoaderCallback().finish(bitmap);
                    }
                }) //设置图片请求完成的后处理器
                .setImageDecodeOptions(decodeOptions) //解码相关设置
                .setAutoRotateEnabled(true) //是否支持自动旋转
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH) //允许设置一个最低请求级别
                .setProgressiveRenderingEnabled(true) //是否支持渐进式加载
                ;
        if (config.getDestHeight() != 0 && config.getDestWidth() != 0) {
            request.setResizeOptions(new ResizeOptions(config.getDestWidth(), config.getDestHeight()));
        }
        AbstractDraweeControllerBuilder draweeControllerBuilder = config.getDraweeControllerBuilder();
        draweeControllerBuilder.setImageRequest(request.build());
        draweeControllerBuilder.setControllerListener(mControllerListener);
        draweeControllerBuilder.setOldController(imageView.getController());
        imageView.setController(draweeControllerBuilder.build());
    }

}
