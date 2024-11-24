package com.demo.hello.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Client
 */
public class HttpClient {
    public static void main(String[] args) {
//        Thread client = new Thread(() -> start(), "client-thread");
//        client.start();
        start();
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
                        ch.pipeline()
                            .addLast(new HttpRequestEncoder())          // 发送数据进行编码
                            .addLast(new HttpResponseDecoder())         // 响应数据进行编码
//                            .addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024))//聚合成一个完整报文
                            .addLast(new HttpClientInboundHandler());

                    }
                });
            // 发起连接
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 触发请求
            FullHttpRequest fullHttpRequest = buildGetRequest();
            ctx.writeAndFlush(fullHttpRequest);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                // 接收到 http 服务端的响应
                System.out.println("response -> " + msg);
                System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
            } else if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
                buf.release();
            } else {
                System.out.println(msg);
            }
            // ctx.close(); 是否关闭通道
        }


    }

    private static FullHttpRequest buildGetRequest() {
        try {
            URI uri = new URI("/hy/ok");
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString());
            request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            return request;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
