package org.study.juc.aqs;

import sun.misc.Unsafe;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * cas 算法
 * 确保并发，每次只有一个线程抢锁成功
 *
 * @Author ggq
 * @Date 2020/9/16 16:54
 */
public class ThreadCas {

    private volatile int state = 0;

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    private static ThreadCas cas = new ThreadCas();

    public static void main(String[] args) {
        new Thread(new Worker(), "t-0").start();
        new Thread(new Worker(), "t-1").start();
        new Thread(new Worker(), "t-2").start();
        new Thread(new Worker(), "t-3").start();
        new Thread(new Worker(), "t-4").start();
    }

    static class Worker implements Runnable {

        @Override
        public void run() {
            System.out.println("请求线程：" + Thread.currentThread().getName() + "到达预定点，准备开始抢state");

            try {
                cyclicBarrier.await();
                if (cas.compareAndSwapState(0, 1)) {
                    System.out.println("线程：" + Thread.currentThread().getName() + "抢到了锁");
                } else {
                    System.out.println("线程：" + Thread.currentThread().getName() + "抢锁失败");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


    public final Boolean compareAndSwapState(int oldValue, int newValue) {
        return unsafe.compareAndSwapInt(this, stateOffset, oldValue, newValue);
    }

    private static final Unsafe unsafe = UnsafeInstance.reflectGetUnsafe();

    private static long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(ThreadCas.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
