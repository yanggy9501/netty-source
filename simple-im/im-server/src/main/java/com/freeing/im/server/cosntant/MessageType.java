package com.freeing.im.server.cosntant;

/**
 * 消息类型
 */
public enum MessageType {
    /** 私聊 */
    PRIVATE(1),

    /** 群聊 */
    GROUP(1),

    /** 异常 */
    NULL(-1);

    private Integer type;

    MessageType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static MessageType match(Integer type) {
        for (MessageType value : MessageType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return NULL;
    }
}
