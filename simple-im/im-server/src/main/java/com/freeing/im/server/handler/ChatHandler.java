package com.freeing.im.server.handler;

import com.alibaba.fastjson.JSON;
import com.freeing.im.server.BootstrapServer;
import com.freeing.im.server.cosntant.MessageType;
import com.freeing.im.server.domain.R;
import com.freeing.im.server.session.ChatMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;

/**
 * 聊天 handler
 *
 * @author yanggy
 */
public class ChatHandler {
    /*
{
code: 10002,
type: 1,
target: 'kato',
content: '你好啊，kato'
}
     */

    public static void execute(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        try {
            String text = frame.text();
            ChatMessage chatMessage = JSON.parseObject(text, ChatMessage.class);
            switch (MessageType.match(chatMessage.getType())) {
                case PRIVATE: {
                    if (StringUtil.isNullOrEmpty(chatMessage.getTarget())) {
                        ctx.channel().writeAndFlush(R.fail("消息发送失败，该消息没有接收者"));
                        return;
                    }
                    Channel channel = BootstrapServer.USER_SESSION.get(chatMessage.getTarget());
                    if (channel == null || !channel.isActive()) {
                        ctx.channel().writeAndFlush(R.fail("消息发送失败，" + chatMessage.getTarget() + "不在线"));
                        return;
                    }
                    channel.writeAndFlush(R.success("私聊消息(" + chatMessage.getNickname() + ")",
                        chatMessage.getContent()));
                    break;
                }
                case GROUP: {
                    BootstrapServer.GROUP.writeAndFlush(R.success("群消息(" + chatMessage.getNickname() + ")",
                        chatMessage.getContent()));
                    break;
                }
                default: {
                    ctx.channel().writeAndFlush(R.fail("不支持的消息类型"));
                }
            }
        } catch (Exception e) {
            ctx.channel().writeAndFlush(R.fail("发送消息格式不正确，请确认后在发送"));
        }

    }
}
