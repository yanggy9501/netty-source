package com.netty.demo.tool;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮
 */
public class WheelTimerTest {
    public static void main(String[] args) {
        HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS);
        System.out.println(new Date());
        // 延迟 2 秒执行
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println(new Date());
            }
        }, 2, TimeUnit.SECONDS);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        timer.stop();
    }
}
