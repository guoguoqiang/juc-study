package org.study.juc.aqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 获取unsafe这个类的实例的方法是通过反射
 *
 * @Author ggq
 * @Date 2020/9/16 17:17
 */
public class UnsafeInstance {
    public static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
