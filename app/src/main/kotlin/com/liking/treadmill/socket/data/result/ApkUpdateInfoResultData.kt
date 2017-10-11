package com.liking.treadmill.socket.data.result

import com.google.gson.annotations.SerializedName

/**
 * Created on 2017/10/11
 * desc: 更新返回
 * "host_id":"31010000000090",//中心主机ID string
 * "url":"www.likingfit.com", //更新包url string
 * "md5":"959872074132d638c39831a4a70faae16720486a", //软件md5摘要 string
 * "size":256347899, //更新包大小，单位字节 long int
 * @author: chenlei
 * @version:1.0
 */
class ApkUpdateInfoResultData(
        @SerializedName("host_id") var hostId: String, //中心主机ID string
        @SerializedName("version") var version: String,
        @SerializedName("url") var url: String,
        @SerializedName("md5") var md5: String,
        @SerializedName("size") var size: String) : ResultData()