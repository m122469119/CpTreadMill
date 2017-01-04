package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * 说明:  用户信息
 * Author: chenlei
 * Time: 下午2:36
 */

public class UserInfoResult extends BaseSocketResult{


    @SerializedName("data")
    private UserData mUserData;

    public UserData getUserInfoData() {
        return mUserData;
    }

    public void setUserInfoData(UserData userData) {
        mUserData = userData;
    }


    public static class UserData {

        @SerializedName("err_code")
        private int mErrcode;
        @SerializedName("err_msg")
        private String mErrmsg;
        @SerializedName("user")
        private UserInfoData mUserInfoData;

        public int getErrcode() {
            return mErrcode;
        }

        public void setErrcode(int errcode) {
            mErrcode = errcode;
        }

        public String getErrmsg() {
            return mErrmsg;
        }

        public void setErrmsg(String errmsg) {
            mErrmsg = errmsg;
        }

        public UserInfoData getUserInfoData() {
            return mUserInfoData;
        }

        public void setUserInfoData(UserInfoData userInfoData) {
            mUserInfoData = userInfoData;
        }

        public static class UserInfoData {

            @SerializedName("role")
            private int role; //1管理员 2普通会员

            @SerializedName("username")
            private String mUserName;

            @SerializedName("avatar")
            private String mAvatar;

            @SerializedName("gender")
            private int mGender;

            @SerializedName("bracelet_id")
            private String mBraceletId;

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            public String getUserName() {
                return mUserName;
            }

            public void setUserName(String userName) {
                mUserName = userName;
            }

            public String getAvatar() {
                return mAvatar;
            }

            public void setAvatar(String avatar) {
                mAvatar = avatar;
            }

            public int getGender() {
                return mGender;
            }

            public void setGender(int gender) {
                mGender = gender;
            }

            public String getBraceletId() {
                return mBraceletId;
            }

            public void setBraceletId(String braceletId) {
                mBraceletId = braceletId;
            }
        }
    }


}
