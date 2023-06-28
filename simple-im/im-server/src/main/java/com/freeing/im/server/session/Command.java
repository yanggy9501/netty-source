package com.freeing.im.server.session;

/**
 * 操作指令
 *
 * @author yanggy
 */
public class Command {
    /**
     * 指令代码：发送消息，群聊发送，连接
     */
    private Integer code;

    /**
     * 会话人名称
     */
    private String nickname;

    public Command() {
    }

    public Command(Integer code, String nickname) {
        this.code = code;
        this.nickname = nickname;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
