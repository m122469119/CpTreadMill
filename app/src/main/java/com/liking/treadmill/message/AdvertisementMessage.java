package com.liking.treadmill.message;

import com.liking.treadmill.socket.result.AdvertisementResult;

import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:33
 */

public class AdvertisementMessage {

    public List<AdvertisementResult.AdvUrlResource.Resource> mResources;

    public  AdvertisementMessage(List<AdvertisementResult.AdvUrlResource.Resource> resources) {
        this.mResources = resources;
    }
}
