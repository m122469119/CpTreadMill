package com.liking.treadmill.socket

import com.liking.socket.SocketIO
import com.liking.socket.model.message.MessageData
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/09/29
 * desc:
 * @author: chenlei
 * @version:1.0
 */

class CmdRequest private constructor(val socket: SocketIO?,
                                     val cmd: Byte,
                                     val data: String) : MessageData() {

    var TAG: String = CmdRequest::class.java.javaClass.simpleName

    override fun cmd(): Byte = cmd

    override fun getData(): ByteArray = data.toByteArray()

    private constructor(builder: Builder) : this(
            builder.socketIo,
            builder.cmd,
            MessageHandlerHelper.toJson(builder.data))

    fun send() {
        socket?.send(this)
    }

    class Builder {

        var socketIo: SocketIO? = null
        var cmd: Byte = CmdConstant.CMD_NORMAL
        var data: BaseData? = null

        fun socket(socket: SocketIO?): Builder {
            this.socketIo = socket
            return this
        }

        fun cmd(cmd: Byte): Builder {
            this.cmd = cmd
            return this
        }

        fun data(data: BaseData): Builder {
            this.data = data
            return this
        }

        fun build(): CmdRequest = CmdRequest(this)
    }
}