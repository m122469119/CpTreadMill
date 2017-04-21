package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;
import com.liking.treadmill.db.entity.Member;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午8:38
 * version 1.0.0
 */

public class MemberListResult extends BaseSocketResult {


    /**
     * member : [{"id":"1","bracelet_id":"123456","bracelet_operate":"1","member_type":"1"}]
     * next_page : true
     */

    @SerializedName("data")
    private MembersData mData;

    public MembersData getData() {
        return mData;
    }

    public void setData(MembersData data) {
        mData = data;
    }

    public static class MembersData {
        @SerializedName("next_page")
        private boolean mNextPage;
        /**
         * id : 1
         * bracelet_id : 123456
         * bracelet_operate : 1
         * member_type : 1
         */

        @SerializedName("member")
        private List<Member> mMember;

        public boolean isNextPage() {
            return mNextPage;
        }

        public void setNextPage(boolean nextPage) {
            mNextPage = nextPage;
        }

        public List<Member> getMember() {
            return mMember;
        }

        public void setMember(List<Member> member) {
            mMember = member;
        }

    }
}
