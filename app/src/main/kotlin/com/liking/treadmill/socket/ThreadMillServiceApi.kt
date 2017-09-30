package com.liking.treadmill.socket

import com.liking.treadmill.socket.internal.ThreadMillServiceApiImpl

/**
 * Created on 2017/09/28
 * desc:
 * @author: chenlei
 * @version:1.0
 */
interface ThreadMillServiceApi {

    fun rebind()
    fun init()
    fun bind()
    fun unBind()
    fun confirm()
    fun reportDevices()
    fun userLogin(cardNo: String)
    fun userLogOut(cardNo: String)
    fun reportExerciseCacheData(data: String)
    fun reportExerciseData(type: Int, aimType: Int, aim: Float, achieve: Int)
    fun requestMembersCommand()
    fun membersStateReplyCommand()

    companion object {
        fun getInstance() : ThreadMillServiceApiImpl {
            return ThreadMillServiceApiImpl()
        }
    }
}
