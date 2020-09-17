package org.study.juc.synchrionized;

import org.openjdk.jol.info.ClassLayout;

/**
 * Synchrionized 锁升级过程-升级轻量级锁
 *
 * @Author ggq
 * @Date 2020/9/16 14:51
 */
public class SynchrionizedMarkWord {
    public static void main(String[] args) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object o = new Object();
        System.out.println("无锁：" + ClassLayout.parseInstance(o).toPrintable());

        new Thread(() -> {
            synchronized (o) {
                System.out.println("第一个线程：" + ClassLayout.parseInstance(o).toPrintable());
            }
        }).start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (o) {
                System.out.println("第二个线程:" + ClassLayout.parseInstance(o).toPrintable());
            }
        }).start();
    }

}
