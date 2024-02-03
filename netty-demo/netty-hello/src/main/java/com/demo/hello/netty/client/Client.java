package com.demo.hello.netty.client;

import com.demo.hello.netty.CtxContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * Client
 */
public class Client {
    public static void main(String[] args) {
        Thread client = new Thread(() -> start(), "client-thread");
        client.start();
        Scanner scanner = new Scanner(System.in);
        ByteBuf requestBuffer;
        while (true) {
            System.out.println("请输入：");
            String msg = scanner.nextLine();
            byte[] bytes = msg.getBytes();
            requestBuffer = Unpooled.buffer(bytes.length);
            requestBuffer.writeBytes(bytes);
            // @see com.demo.hello.netty.client.NettyClientHandler.channelActive
            CtxContext.CTX_MAP.get("client").writeAndFlush(requestBuffer);
        }
    }

    public static void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 客户端启动类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
            // 发起连接
            ChannelFuture future = bootstrap.connect("127.0.0.1", 50070).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
