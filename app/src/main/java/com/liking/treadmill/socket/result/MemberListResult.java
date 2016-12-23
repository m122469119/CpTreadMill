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
        private MemberListData braceletId;

        public MemberListData getBraceletId() {
            return braceletId;
        }

        public void setBraceletId(MemberListData braceletId) {
            this.braceletId = braceletId;
        }

        public static class MemberListData {
            @SerializedName("member")
            private List<String> mMember;
            @SerializedName("manger")
            private List<String> mManger;

            public List<String> getMember() {
                return mMember;
            }

            public void setMember(List<String> member) {
                mMember = member;
            }

            public List<String> getManger() {
                return mManger;
            }

            public void setManger(List<String> manger) {
                mManger = manger;
            }
        }
    }
}
