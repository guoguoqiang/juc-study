package org.study.juc.synchrionized;

import org.openjdk.jol.info.ClassLayout;

/**
 * 非静态方法
 * 同步实例方法，锁是当前实例对象
 *
 * @Author ggq
 * @Date 2020/9/16 11:26
 */
public class LockOnThisObject {

    private static Integer stock = 10;

    /**
     * 相当于
     * synchronized（this）
     */
    public synchronized void decrStock() {
        --stock;
        System.out.println(ClassLayout.parseInstance(this).toPrintable());
    }

    public static void main(String[] args) {
        final LockOnThisObject lockOnThisObject = new LockOnThisObject();
        lockOnThisObject.decrStock();
        System.out.println(stock);

    }
}
