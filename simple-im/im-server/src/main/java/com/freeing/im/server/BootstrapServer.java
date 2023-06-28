package com.freeing.im.server;

import com.freeing.im.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanggy
 */
public class BootstrapServer {

    public static final Map<String, Channel> USER_SESSION = new ConcurrentHashMap<>(1024);

    private static final int PORT = 8080;

    public static void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 相当 netty 服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                // 监听端口的 ServerSocketChannel
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 打印日志
                .handler(new LoggingHandler(LogLevel.class))
                // 初始化，处理每个连接的 SocketChannel, ChannelInitializer 在处理完初始化方法后从pipeline中删除
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // http 编码加密器
                        ch.pipeline().addLast(new HttpServerCodec())
                            // 大数据流处理器
                            .addLast(new ChunkedWriteHandler())
                            // 对 http 消息做集合操作，产生两个对象 FullHttpRequest + FullHttpResponse
                            .addLast(new HttpObjectAggregator( 2 >>> 16))
                            // websocket 协议支持
                            .addLast(new WebSocketServerProtocolHandler("/"))
                            .addLast(new WebSocketHandler());
                    }
                });
            // 绑定端口，并且同步等待监听端口
            ChannelFuture future = serverBootstrap.bind(PORT).sync();
            // 同步等待关闭服务器
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
