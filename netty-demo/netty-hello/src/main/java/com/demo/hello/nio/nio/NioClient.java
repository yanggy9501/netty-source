package com.demo.hello.nio.nio;

import java.util.Scanner;

import static com.demo.hello.nio.nio.Const.DEFAULT_PORT;
import static com.demo.hello.nio.nio.Const.DEFAULT_SERVER_IP;

public class NioClient {
    private static NioClientHandle nioClientHandle;

    public static void start(){
        nioClientHandle = new NioClientHandle(DEFAULT_SERVER_IP, DEFAULT_PORT);
        //nioClientHandle = new NioClientHandle(DEFAULT_SERVER_IP,8888);
        new Thread(nioClientHandle,"client").start();
    }
    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception{
        nioClientHandle.sendMsg(msg);
        return true;
    }
    public static void main(String[] args) throws Exception {
        start();
        Scanner scanner = new Scanner(System.in);
        while(NioClient.sendMsg(scanner.next()));

    }

}
