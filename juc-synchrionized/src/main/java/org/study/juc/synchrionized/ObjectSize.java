package org.study.juc.synchrionized;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 模拟synchronized锁升级-升级偏向锁
 *
 * @Author ggq
 * @Date 2020/9/16 14:34
 */
public class ObjectSize {

    /**
     * 00000001 00000000 00000000 00000000
     * 存在大小端
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 延迟5秒 模拟JVM 内部的延迟
        TimeUnit.SECONDS.sleep(5);
        Object o = new Object();
        // 无锁    TimeUnit.SECONDS.sleep(5); 00000101 00000000 00000000 00000000
//        00000001 00000000 00000000 00000000
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        synchronized (o) {
            // TimeUnit.SECONDS.sleep(5) 00000101 11011000 10100001 00000000
            //     00001000 11110110 11011110 00000010
            // 一个线程执行 升级偏向锁        JVM内部，减少锁升级带来的开销  会延迟启动 偏向锁
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

    }
}
