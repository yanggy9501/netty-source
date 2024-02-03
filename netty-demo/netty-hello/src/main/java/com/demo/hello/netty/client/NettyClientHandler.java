package com.demo.hello.netty.client;

import com.demo.hello.netty.CtxContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author yanggy
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf requestBuffer;

    public NettyClientHandler() {
        byte[] bytes = "你好!".getBytes();
        requestBuffer = Unpooled.buffer(bytes.length);
        requestBuffer.writeBytes(bytes);
    }

    /**
     * 建立连接后回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(requestBuffer);
        System.out.println("client 成功与 sever 建立连接: " + ctx.channel().id().asLongText());
//        CtxContext.CTX_MAP.put(ctx.channel().id().asLongText(), ctx);
        CtxContext.CTX_MAP.put("client", ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取对方响应数据
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            System.out.println("接收到响应数据：" + new String(bytes));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
