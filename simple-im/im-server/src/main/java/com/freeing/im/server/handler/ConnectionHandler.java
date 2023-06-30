package com.freeing.im.server.handler;

import com.alibaba.fastjson.JSON;
import com.freeing.im.server.BootstrapServer;
import com.freeing.im.server.domain.R;
import com.freeing.im.server.session.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * 连接处理-保存会话的映射关系
 *
 * @author yanggy
 */
public class ConnectionHandler {

    /*
{
    code: 10001,
    nickname: 'kato'
}

{
    code: 10001,
    nickname: 'alice'
}
     */

    public static void execute(ChannelHandlerContext ctx, Command command) {
        if (BootstrapServer.USER_SESSION.containsKey(command.getNickname())) {
            ctx.channel().writeAndFlush(R.fail("该用户已经上线，请切换用户后~"));
            ctx.channel().disconnect();
            return;
        }
        BootstrapServer.USER_SESSION.put(command.getNickname(), ctx.channel());
        ctx.channel().writeAndFlush(R.success("与服务端连接建立成功"));
        ctx.channel().writeAndFlush(R.success(JSON.toJSONString(BootstrapServer.USER_SESSION.keySet())));
    }
}
