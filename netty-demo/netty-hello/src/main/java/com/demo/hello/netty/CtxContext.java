package com.demo.hello.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CtxContext {
    public static Map<String, ChannelHandlerContext> CTX_MAP = new ConcurrentHashMap<>();
}
