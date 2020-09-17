package org.study.juc.synchrionized;

/**
 * JVM优化 synchronized锁- 逃逸分析
 *
 * @Author ggq
 * @Date 2020/9/16 15:44
 */
public class LockEscapse {

    /**
     * 进行两种测试
     * 关闭逃逸分析，同时调大堆空间，避免堆内GC的发生，如果有GC信息将会被打印出来
     * VM运行参数：-Xmx4G -Xms4G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 开启逃逸分析
     * VM运行参数：-Xmx4G -Xms4G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 执行main方法后
     * jps 查看进程
     * jmap -histo 进程ID
     */
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            alloc();
        }
        final long end = System.currentTimeMillis();
        System.out.println("用时：" + (end - start) + " ms");

    }

    private static void alloc() {
        Test lockEscapse = new Test();
    }

    static class Test {
        private String name;
        private String age;
    }
}
