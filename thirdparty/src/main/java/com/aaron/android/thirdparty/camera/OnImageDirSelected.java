package com.aaron.android.thirdparty.camera;

/**
 * 说明:从扫描文件选择不同文件夹，（即切换图片文件夹数据源）需要实现该接口
 * Author shaozucheng
 * Time:16/5/31 上午10:57
 */
public interface OnImageDirSelected {
    void selectedImageFolder(ImageFolder folder);
}
