package com.aaron.android.framework.library.imageloader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.aaron.android.codelibrary.imageloader.ImageConfigBuilder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;

/**
 * Created on 16/8/19.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class HImageConfigBuilder extends ImageConfigBuilder<HImageConfigBuilder, HImageConfig> {
    private static final ScalingUtils.ScaleType DEFAULT_SCALE_TYPE = ScalingUtils.ScaleType.CENTER_CROP;
    private AbstractDraweeControllerBuilder mDraweeControllerBuilder;
    private GenericDraweeHierarchy mDraweeHierarchy;

    public HImageConfigBuilder(ImageView imageView, Object loadPath) {
        super(imageView, loadPath);
        mDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        mDraweeHierarchy = getImageView().getHierarchy();
    }

    @Override
    public HImageView getImageView() {
        ImageView imageView = super.getImageView();
        if (!(imageView instanceof HImageView)) {
            throw new IllegalArgumentException("imageView need extends HImageView");
        }
        return (HImageView) imageView;
    }

    @Override
    public HImageConfigBuilder getThis() {
        return this;
    }

    @Override
    public HImageConfig obtainImageConfig() {
        return new HImageConfig(getThis());
    }


    /**
     * 设置默认图片
     *
     * @param defaultDrawable 默认图片
     */
    public HImageConfigBuilder setDefaultImage(Drawable defaultDrawable) {
        mDraweeHierarchy.setPlaceholderImage(defaultDrawable, DEFAULT_SCALE_TYPE);
        return getThis();
    }

    /**
     * 设置默认图片
     *
     * @param defaultDrawable 默认图片
     */
    public HImageConfigBuilder setDefaultImage(Drawable defaultDrawable, ScalingUtils.ScaleType scaleType) {
        mDraweeHierarchy.setPlaceholderImage(defaultDrawable, scaleType);
        return getThis();
    }

    public HImageConfigBuilder setDefaultImage(int resId) {
        mDraweeHierarchy.setPlaceholderImage(resId);
        return getThis();
    }

    public HImageConfigBuilder setDefaultImage(int resId, ScalingUtils.ScaleType scaleType) {
        mDraweeHierarchy.setPlaceholderImage(resId, scaleType);
        return getThis();
    }

    /**
     * 设置失败时显示的图片
     *
     * @param failDrawable 失败时显示的图片
     */
    public HImageConfigBuilder setFailImage(Drawable failDrawable) {
        mDraweeHierarchy.setFailureImage(failDrawable, DEFAULT_SCALE_TYPE);
        return getThis();
    }

    /**
     * 设置失败时显示的图片
     *
     * @param failDrawable 失败时显示的图片
     */
    public HImageConfigBuilder setFailImage(Drawable failDrawable, ScalingUtils.ScaleType scaleType) {
        mDraweeHierarchy.setFailureImage(failDrawable, scaleType);
        return getThis();
    }

    /**
     * 设置加载重试的图片
     *
     * @param retryDrawable
     */
    public HImageConfigBuilder setRetryImage(Drawable retryDrawable) {
        mDraweeHierarchy.setRetryImage(retryDrawable, DEFAULT_SCALE_TYPE);
        return getThis();
    }

    /**
     * 设置加载重试的图片
     *
     * @param retryDrawable
     */
    public HImageConfigBuilder setRetryImage(Drawable retryDrawable, ScalingUtils.ScaleType scaleType) {
        mDraweeHierarchy.setRetryImage(retryDrawable, scaleType);
        return getThis();
    }

    /**
     * 设置图片加载进度图片
     *
     * @param progressDrawable 图片加载进度图片
     */
    public HImageConfigBuilder setProgressImage(Drawable progressDrawable) {
        mDraweeHierarchy.setProgressBarImage(progressDrawable, DEFAULT_SCALE_TYPE);
        return getThis();
    }

    /**
     * 设置图片加载进度图片
     *
     * @param progressDrawable 图片加载进度图片
     */
    public HImageConfigBuilder setProgressImage(Drawable progressDrawable, ScalingUtils.ScaleType scaleType) {
        mDraweeHierarchy.setProgressBarImage(progressDrawable, scaleType);
        return getThis();
    }


    /**
     * 设置图片ScaleType
     *
     * @param scaleType ScalingUtils.ScaleType
     */
    public HImageConfigBuilder setScaleType(ScalingUtils.ScaleType scaleType) {
        mDraweeHierarchy.setActualImageScaleType(scaleType);
        return getThis();
    }

    /**
     * @param fadeDuration
     */
    public HImageConfigBuilder setFadeDuration(int fadeDuration) {
        mDraweeHierarchy.setFadeDuration(fadeDuration);
        return getThis();
    }

    /**
     * 设置是否支持重试
     *
     * @param retryEnable
     */
    public HImageConfigBuilder setRetryEnable(boolean retryEnable) {
        mDraweeControllerBuilder.setTapToRetryEnabled(retryEnable);
        return getThis();
    }

    /**
     * 自动执行gif动画
     *
     * @param autoPlayAnimations
     */
    public HImageConfigBuilder setAutoPlayAnimations(boolean autoPlayAnimations) {
        mDraweeControllerBuilder.setAutoPlayAnimations(autoPlayAnimations);
        return getThis();
    }

    public HImageConfigBuilder setRoundingParams(RoundingParams roundingParams) {
        mDraweeHierarchy.setRoundingParams(roundingParams);
        return getThis();
    }

    public AbstractDraweeControllerBuilder getDraweeControllerBuilder() {
        return mDraweeControllerBuilder;
    }
}
