package org.study.juc.synchrionized;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程并发
 *
 * @Author ggq
 * @Date 2020/9/16 10:06
 */
public class MultiThread {

    /**
     * 临界资源
     */
    private static int total = 0;

    private static Object object = new Object();

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        /**
         * 10个线程 循环 1000次累加
         */
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    for (int j = 0; j < 1000; j++) {
//                        synchronized (object) {
//                            total++;
//                        }
                        lock.lock();
                        total++;

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }).start();

            Thread.sleep(1000);

            countDownLatch.countDown();

        }
        System.out.println(total);
    }
}
