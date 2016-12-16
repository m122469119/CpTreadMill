package com.liking.treadmill.message;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:33
 * version 1.0.0
 */
public class WifiMessage {
   private boolean isHaveWifi;

    public WifiMessage(boolean isHaveWifi) {
        this.isHaveWifi = isHaveWifi;
    }

    public boolean isHaveWifi() {
        return isHaveWifi;
    }
}
