package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午8:38
 * version 1.0.0
 */

public class MemberListResult extends BaseSocketResult {

    /**
     * data : {"bracelet_id":[123456,654321]}
     */

    @SerializedName("data")
    private MemberData data;

    public MemberData getData() {
        return data;
    }

    public void setData(MemberData data) {
        this.data = data;
    }

    public static class MemberData {
        @SerializedName("bracelet_id")
        private List<String> braceletId;

        public List<String> getBraceletId() {
            return braceletId;
        }

        public void setBraceletId(List<String> braceletId) {
            this.braceletId = braceletId;
        }
    }
}
