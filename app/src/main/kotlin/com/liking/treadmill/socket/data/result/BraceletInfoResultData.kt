package com.liking.treadmill.socket.data.result

import com.google.gson.annotations.SerializedName

/**
 * Created on 2017/10/11
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class BraceletInfoResultData(@SerializedName("bracelet_id") var braceletId: Long) : ResultData()