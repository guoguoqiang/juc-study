package org.study.juc.aqs;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport demo
 *
 * @Author ggq
 * @Date 2020/9/16 16:29
 */
public class ThreadLockSupport {
    public static void main(String[] args) {

        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                final Thread currentThread = Thread.currentThread();
                System.out.println(currentThread.getName() + " 线程开始执行");
                for (; ; ) {
                    System.out.println("准备park住当前线程：" + currentThread.getName());
                    LockSupport.park();
                    System.out.println("当前线程：" + currentThread.getName() + " 已经被唤醒");
                }
            }

        }, "t0");
        t0.start();
        try {
            Thread.sleep(5000);
            System.out.println("准备唤醒线程：" + t0.getName());
            LockSupport.unpark(t0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
