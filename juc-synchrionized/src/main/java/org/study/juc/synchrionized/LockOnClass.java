package org.study.juc.synchrionized;

/**
 * 静态方法加锁
 *
 * @Author ggq
 * @Date 2020/9/16 11:22
 */
public class LockOnClass {
    static int stock;

    /**
     *  加锁 加在 LockOnClass.class
     */
    public static synchronized void decrStock() {

        System.out.println(--stock);
    }

    public static void main(String[] args) {
        LockOnClass.decrStock();
    }
}
