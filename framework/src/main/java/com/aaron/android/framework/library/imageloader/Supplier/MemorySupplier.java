package com.aaron.android.framework.library.imageloader.Supplier;

import com.facebook.common.internal.Supplier;
import com.facebook.imagepipeline.cache.MemoryCacheParams;

/**
* Created on 15/6/18.
*
* @author ran.huang
* @version 3.0.1
*/
public class MemorySupplier implements Supplier<MemoryCacheParams> {
    private static final int MAX_CACHE_ENTRIES = 256; //最大缓存实体个数
    private static final int MAX_EVICTION_QUEUE_SIZE = Integer.MAX_VALUE; //最大的不可用但未删除的缓存队列大小
    private static final int MAX_EVICTION_QUEUE_ENTRIES = Integer.MAX_VALUE; //不可用但未删除的
    private static final int MAX_CACHE_ENTRY_SIZE = Integer.MAX_VALUE;
    private long mMaxMemoryCacheSize;

    public MemorySupplier(long maxMemoryCacheSize) {
        mMaxMemoryCacheSize = maxMemoryCacheSize;
    }

    @Override
    public MemoryCacheParams get() {
        return new MemoryCacheParams(
                (int) mMaxMemoryCacheSize,
                MAX_CACHE_ENTRIES,
                MAX_EVICTION_QUEUE_SIZE,
                MAX_EVICTION_QUEUE_ENTRIES,
                MAX_CACHE_ENTRY_SIZE);
    }


}
