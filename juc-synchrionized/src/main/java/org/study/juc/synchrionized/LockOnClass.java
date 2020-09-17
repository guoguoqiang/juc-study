package org.study.juc.synchrionized;

/**
 * 静态方法加锁
 * 同步类方法，锁是当前类对象
 * 加锁力度广
 *
 * @Author ggq
 * @Date 2020/9/16 11:22
 */
public class LockOnClass {
    static int stock;

    /**
     * 加锁 加在 LockOnClass.class
     */
    public static synchronized void decrStock() {

        System.out.println(--stock);
    }

    /**
     * 慎重此写法，加锁的是同一个 class对象上面，影响性能
     */
    /*
    另外值得一提，此写法生产影响性能
    System.out.println(--stock);
     public void println(int x) {
     *         synchronized (this) {
     *             print(x);
     *             newLine();
     *         }
     *     }
     */
//    public static synchronized void test() {
//
//        System.out.println(--stock);
//    }

    public static void main(String[] args) {
        LockOnClass.decrStock();
    }
}
