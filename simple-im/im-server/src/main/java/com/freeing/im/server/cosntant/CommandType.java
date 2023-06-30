package com.freeing.im.server.cosntant;

public enum CommandType {
    CONNECTION(10001),
    CHAT(10002),
    JOIN_GROUP(10003),
    NULL(-1);

    private Integer code;

    CommandType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static CommandType match(Integer code) {
        for (CommandType value : CommandType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NULL;
    }
}
