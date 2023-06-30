package com.freeing.im.server.handler;

import com.freeing.im.server.BootstrapServer;
import com.freeing.im.server.domain.R;
import io.netty.channel.ChannelHandlerContext;

/**
 * 群聊
 *
 * @author yanggy
 */
public class JoinGroupChatHandler {
    public static void execute(ChannelHandlerContext ctx) {
        BootstrapServer.GROUP.add(ctx.channel());
        ctx.channel().writeAndFlush(R.success("加入系统默认群聊"));
    }
}
