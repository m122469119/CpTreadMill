# Liking Socket

[TOC]

## 未完成

- 发送接收流程 √
- 失败重发 √
- 失败保存 
- 心跳超时
- 添加Log 
- 单元测试
- 整理代码


## 介绍

Liking的Socket基础库

### 协议格式

|    4B    |    4B    |     2B     |     3B     |     8B     |     1B     |     20B     |     65535 - x    |
|    :-:     |   :-:    |   :-:    |    :-:     |   :-:    |   :-:    |   :-:    |   :-:    |   :-:    |
|    Length   |     ProtocolVer     |    AppID  |    AppVer  |    MsgID |   Cmd  |    Sign  |    Data  |

- 所有命令字为整型，范围：151~255
- 大端（BigEndian），Java默认为大端，无需处理
- Sign字段：sha1(拼接(升序(cmd,msgID,data)))

### 其他说明

暂无

## 使用

- 命令字

    默认库中定义了所有的命令字，若未定义，使用者自行配置异构命令字

- 发送数据

``` java
// 初始化
SocketIO.Builder builder = new SocketIO.Builder();
builder.connect("120.24.177.134", 17919);
builder.headerAssemble(new HeaderAssemble()); // 组装包头
builder.headerResolver(new HeaderResolver()); // 解析包头
builder.addDefaultParse(new PingPong());      // 同构命令，多个
builder.addPingPongMsg(new PingPongMsg());    // 心跳包
builder.addDefaultSend(getDeviceInfo());      // 连接后默认发送
mClient = builder.build();
    
// 发送数据
mClient.send(new MessageData() {
    @Override
    public byte cmd() {
        return CMD_LOGIN; // 命令字
    }

    @Override
    public byte[] getData() { // 要发送的数据
        JSONObject object = new JSONObject();
        try {
            object.put("gym_id", "a0000001");
            object.put("device_id", "ttdevs");
            object.put("bracelet_id", "ttdevs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString().getBytes();
    }

    @Override
    public boolean needFeedback() {
        return true; // 是否需要回调
    }

    @Override
    public void callBack(boolean isSuccess, String message) { // 回调
        System.out.println(">>>>>" + message);
    }
});
```

- 接收数据
  
  通过广播接收数据


## 备注

// TODO


