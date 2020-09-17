package org.study.juc.synchrionized;

/**
 * JVM优化 synchronized锁-锁的消除
 *
 * @Author ggq
 * @Date 2020/9/16 15:36
 */
public class LockEliminate {

    /**
     * 锁的消除
     * 方法栈本来就是私有的，没有共享
     */
    private void method() {
        Object o = new Object();
        synchronized (o) {
            System.out.println("锁的消除");
        }
    }
}
