package com.demo.hello.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author yanggy
 */
public class Server {
    public static void main(String[] args) {
        // 线程组，不同的事件用不同的线程组处理
        // 处理连接|监听事件的线程组
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        // 工作线程组，处理Handler
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            // 相当 netty 服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                // 监听端口的 ServerSocketChannel，其他的通信模式 epoll，aio，local（jvm内），embedded（单元测试用）
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 打印日志
                .handler(new LoggingHandler(LogLevel.class))
                // 初始化，处理每个连接的 SocketChannel
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                       // 添加处理网络请求的 ServerHandler
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                });
            // 绑定端口，并且同步等待监听端口
            ChannelFuture future = serverBootstrap.bind(50070).sync();
            // 同步等待关闭服务器
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭线程池
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}

