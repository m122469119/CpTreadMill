package com.liking.treadmill.socket

import com.aaron.android.framework.utils.ResourceUtils
import com.liking.socket.SocketIO
import com.liking.socket.model.message.MessageData
import com.liking.treadmill.R
import com.liking.treadmill.socket.data.BaseData
import com.liking.treadmill.widget.IToast

/**
 * Created on 2017/09/29
 * desc:
 * @author: chenlei
 * @version:1.0
 */

class CmdRequest private constructor(val socket: SocketIO?,
                                     val cmd: Byte,
                                     val feedback: Boolean,
                                     val data: String,
                                     val call: Call?,
                                     val showToast: Boolean) : MessageData() {

    var TAG: String = CmdRequest::class.java.javaClass.simpleName

    override fun cmd(): Byte = cmd

    override fun needFeedback(): Boolean = feedback

    override fun getData(): ByteArray = data.toByteArray()

    private constructor(builder: Builder) : this(
            builder.socketio,
            builder.cmd,
            builder.feedback,
            MessageHandlerHelper.toJson(builder.data),
            builder.call,
            builder.showFailToast)

    fun send() {
        send(socket)
    }

    fun send(socket: SocketIO?) {
        socket?.send(this)
    }

    override fun callBack(isSuccess: Boolean, message: String?) {
        super.callBack(isSuccess, message)
        call?.callBack(isSuccess, message)
        System.out.println("callBack result[".plus("sendState:").plus(isSuccess).plus("|message:").plus(message+"]"))
        if (!dispatchCallBackEvent(isSuccess, message)) {
            if (!message.isNullOrEmpty() && isSuccess) {
                MessageHandlerHelper.handlerReceive(cmd(), message!!)
            } else {
                if (showToast) {
                    IToast.show(ResourceUtils.getString(R.string.network_contact_fail))
                }
            }
        }
    }

    fun dispatchCallBackEvent(isSuccess: Boolean, message: String?): Boolean {
        return false
    }

    interface Call {
       fun callBack(success: Boolean, message: String?)
    }

    class Builder {

        var socketio: SocketIO? = null
        var cmd: Byte = CmdConstant.CMD_NORMAL
        var feedback: Boolean = false
        var data: BaseData? = null
        var call: Call? = null
        var showFailToast: Boolean = false


        fun socket(socket: SocketIO): Builder {
            this.socketio = socketio
            return this
        }

        fun cmd(cmd: Byte): Builder {
            this.cmd = cmd
            return this
        }

        fun feedback(feedback: Boolean): Builder {
            this.feedback = feedback
            return this
        }

        fun data(data: BaseData): Builder {
            this.data = data
            return this
        }

        fun addCallBack(call: Call): Builder {
            this.call = call
            return this
        }

        fun showFailToast(showFailToast: Boolean): Builder {
            this.showFailToast = showFailToast
            return this
        }

        fun build(): CmdRequest = CmdRequest(this)
    }
}