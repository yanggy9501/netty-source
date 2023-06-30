package com.freeing.im.server.handler;

import com.alibaba.fastjson.JSON;
import com.freeing.im.server.domain.R;
import com.freeing.im.server.session.Command;
import com.freeing.im.server.cosntant.CommandType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author yanggy
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
         try {
             // 消息解析
             String message = frame.text();
             Command command = JSON.parseObject(message, Command.class);

             switch (CommandType.match(command.getCode())) {
                case CONNECTION: {
                    ConnectionHandler.execute(ctx, command);
                    break;
                }
                 case CHAT: {
                     ChatHandler.execute(ctx, frame);
                     break;
                 }
                 case JOIN_GROUP: {
                     JoinGroupChatHandler.execute(ctx);
                     break;
                 }
                default : {
                    ctx.channel().writeAndFlush(R.fail("不支持的 code"));
                }
            }
        } catch (Exception ex) {
            ctx.channel().writeAndFlush(R.fail(ex.getMessage()));
        }

    }

}
