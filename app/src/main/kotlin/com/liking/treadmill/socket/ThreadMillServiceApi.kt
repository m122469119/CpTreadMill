package com.liking.treadmill.socket

import com.liking.treadmill.socket.internal.ThreadMillServiceApiImpl

/**
 * Created on 2017/09/28
 * desc:
 * @author: chenlei
 * @version:1.0
 */
interface ThreadMillServiceApi {

    fun initialization()
    fun bind()
    fun unBind()
    fun checkUpdates()
    fun reportDevices()
    fun userLogin(cardNo: String)
    fun userLogOut(cardNo: String)
    fun reportExerciseData(type: Int, aimType: Int, aim: Float, achieve: Int)

    companion object {
        fun INSTANCE() : ThreadMillServiceApiImpl {
            return ThreadMillServiceApiImpl()
        }
    }
}
