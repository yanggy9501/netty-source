package com.demo.hello.nio.nio;


import static com.demo.hello.nio.nio.Const.DEFAULT_PORT;

public class NioServer {
    private static NioServerHandle nioServerHandle;

    public static void main(String[] args){
        nioServerHandle = new NioServerHandle(DEFAULT_PORT);
        new Thread(nioServerHandle,"Server").start();
    }
}
