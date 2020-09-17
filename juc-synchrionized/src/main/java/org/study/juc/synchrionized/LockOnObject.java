package org.study.juc.synchrionized;

/**
 * 同步代码块 锁是括号里面的对象
 * 加锁力度小，只是一部分代码
 * @Author ggq
 * @Date 2020/9/16 13:09
 */
public class LockOnObject {

    public static Object object = new Object();

    private Integer stock = 10;

    public void decrStock() {
        // 同步块
        synchronized (object) {
            --stock;
            if (stock <= 0) {
                System.out.println("over");
                return;
            }
        }
    }
}
