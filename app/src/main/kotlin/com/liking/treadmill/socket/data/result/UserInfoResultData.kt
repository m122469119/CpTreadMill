package com.liking.treadmill.socket.data.result

import com.google.gson.annotations.SerializedName

/**
 * Created on 2017/10/11
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

class UserInfoResultData : ResultData() {

    /**
     * ErrMsg :
     * UserInfo : {"Role":0,"Username":"","Avatar":"","Gender":0,"BraceletId":0}
     */

    @SerializedName("ErrMsg")
    var errMsg: String? = null
    @SerializedName("UserInfo")
    var user: UserInfoBean? = null

    class UserInfoBean {
        /**
         * Role : 0
         * Username :
         * Avatar :
         * Gender : 0
         * BraceletId : 0
         */

        @SerializedName("Role")
        var role: Int = 0
        @SerializedName("Username")
        var username: String? = null
        @SerializedName("Avatar")
        var avatar: String? = null
        @SerializedName("Gender")
        var gender: Int = 0
        @SerializedName("BraceletId")
        var braceletId: Int = 0
    }
}
