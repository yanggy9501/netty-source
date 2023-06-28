package com.freeing.im.server.domain;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author yanggy
 */
public class R {
    private String name;

    private String message;

    private LocalDateTime time;

    public R() {
    }

    public R(String name, String message, LocalDateTime time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public static TextWebSocketFrame fail(String msg) {
        return new TextWebSocketFrame(JSON.toJSONString(new R("系统消息", msg, LocalDateTime.now())));
    }

    public static TextWebSocketFrame success(String msg) {
        return new TextWebSocketFrame(JSON.toJSONString(new R("系统消息", msg, LocalDateTime.now())));
    }

    public static TextWebSocketFrame success(String user, String msg) {
        return new TextWebSocketFrame(JSON.toJSONString(new R(user, msg, LocalDateTime.now())));
    }
}
