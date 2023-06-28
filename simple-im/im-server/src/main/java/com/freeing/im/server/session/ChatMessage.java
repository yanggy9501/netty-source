package com.freeing.im.server.session;

/**
 * @author yanggy
 */
public class ChatMessage extends Command {
    /**
     * 私聊|群聊
     */
    private Integer type;

    /**
     * 消息接收对象
     */
    private String target;

    /**
     * 消息内容
     */
    private String content;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
