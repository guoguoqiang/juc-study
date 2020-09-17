# 并发编程之synchronized

## 设计同步器的意义
多线程编程中，有可能会出现多个线程同时访问同一个共享、可变资源的情况，这个资源我们称之其为临界资源；这种资源可能是：对象、变量、文件等。

共享：资源可以由多个线程同时访问
可变：资源可以在其生命周期内被修改

引出的问题：
由于线程执行的过程是不可控的，所以需要采用同步机制来协同对对象可变状态的访问！

## 如何解决线程并发安全问题？

实际上，所有的并发模式在解决线程安全问题时，采用的方案都是序列化访问临界资源。即在同一时刻，只能有一个线程访问临界资源，也称作同步互斥访问。
Java 中，提供了两种方式来实现同步互斥访问：synchronized 和 Lock

synchronized 隐式锁
ReentrantLock 显示锁

同步器的本质就是加锁

加锁目的：序列化访问临界资源，即同一时刻只能有一个线程访问临界资源(同步互斥访问)

不过有一点需要区别的是：当多个线程执行一个方法时，该方法内部的局部变量并不是临界资源，因为这些局部变量是在每个线程的私有栈中，因此不具有共享性，不会导致线程安全问题。


## synchronized 升级过程


| 无线程                                                       | 无锁状态                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 一个线程进来                                                 | 升级偏向锁                                                   |
| 2个或者几个                                                  | 升级轻量级锁                                                 |
| 内部循环(spin)，等待执行完<br/>，如果循环次数多，    <br/>或者等待超过一定时间 | 升级重量级锁（这块看着怎么和ReentrantLock 有点像，c++版实现？） |

锁升级不可逆
                        

## synchronized原理详解
synchronized内置锁是一种对象锁(锁的是对象而非引用)，作用粒度是对象，可以用来实现对临界资源的同步互斥访问，是可重入的。

加锁的方式：

1、同步实例方法，锁是当前实例对象

2、同步类方法，锁是当前类对象，锁的力度广

3、同步代码块，锁是括号里面的对象，锁的力度小

## 代码实战

```
    /**
     * 加锁 加在 LockOnClass.class
     */
    public static synchronized void decrStock() {

        System.out.println(--stock);
    }
```

javap  -v LockOnClass.class

```
  public static synchronized void decrStock();
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
    Code:
      stack=3, locals=0, args_size=0
         0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: getstatic     #3                  // Field stock:I
         6: iconst_1
         7: isub
         8: dup
         9: putstatic     #3                  // Field stock:I
        12: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
        15: return
      LineNumberTable:
        line 17: 0
        line 18: 15

```

**ACC_SYNCHRONIZED**

执行下面的字节码 会加上内置锁

## synchronized 底层原理

​		synchronized是基于JVM内置锁实现，通过内部对象Monitor(监视器锁)实现，基于进入与退出Monitor对象实现方法与代码块同步，监视器锁的实现依赖底层操作系统的Mutex lock（互斥锁）实现，它是一个重量级锁性能较低。当然，JVM内置锁在1.5之后版本做了重大的优化，如锁粗化（Lock Coarsening）、锁消除（Lock Elimination）、轻量级锁（Lightweight Locking）、偏向锁（Biased Locking）、适应性自旋（Adaptive Spinning）等技术来减少锁操作的开销，，内置锁的并发性能已经基本与Lock持平。

​		synchronized关键字被编译成字节码后会被翻译成monitorenter 和 monitorexit 两条指令分别在同步块逻辑代码的起始位置与结束位置。



## **Monitor监视器锁**

 **任何一个对象都有一个Monitor与之关联，当且一个Monitor被持有后，它将处于锁定状态**。Synchronized在JVM里的实现都是 **基于进入和退出Monitor对象来实现方法同步和代码块同步**，虽然具体实现细节不一样，但是都可以通过成对的MonitorEnter和MonitorExit指令来实现。

- **monitorenter**：每个对象都是一个监视器锁（monitor）。当monitor被占用时就会处于锁定状态，线程执行monitorenter指令时尝试获取monitor的所有权，过程如下：

- 1. **如果monitor的进入数为0**，则该线程进入monitor，然后将进入数设置为1，该线程即为monitor的所有者；
  2. **如果线程已经占有该monitor**，只是重新进入，则进入monitor的进入数加1；
  3. **如果其他线程已经占用了monitor**，则该线程进入阻塞状态，直到monitor的进入数为0，再重新尝试获取monitor的所有权；

- **monitorexit**：执行monitorexit的线程必须是objectref所对应的monitor的所有者。**指令执行时，monitor的进入数减1，如果减1后进入数为0，那线程退出monitor，不再是这个monitor的所有者**。其他被这个monitor阻塞的线程可以尝试去获取这个 monitor 的所有权。

**monitorexit，指令出现了两次，第1次为同步正常退出释放锁；第2次为发生异步退出释放锁**；

通过上面两段描述，我们应该能很清楚的看出Synchronized的实现原理，**Synchronized的语义底层是通过一个monitor的对象来完成，其实wait/notify等方法也依赖于monitor对象**，这就是为什么只有在同步的块或者方法中才能调用wait/notify等方法，**否则会抛出java.lang.IllegalMonitorStateException的异常的原因**。

**什么是monitor？**

可以把它理解为 **一个同步工具**，也可以描述为 **一种同步机制**，它通常被 **描述为一个对象**。与一切皆对象一样，所有的Java对象是天生的Monitor，每一个Java对象都有成为Monitor的潜质，**因为在Java的设计中 ，每一个Java对象自打娘胎里出来就带了一把看不见的锁，它叫做内部锁或者Monitor锁**。**也就是通常说Synchronized的对象锁，MarkWord锁标识位为10，其中指针指向的是Monitor对象的起始地址**。在Java虚拟机（HotSpot）中，**Monitor是由ObjectMonitor实现的**，其主要数据结构如下（位于HotSpot虚拟机源码ObjectMonitor.hpp文件，C++实现的）：



## **对象的内存布局**

HotSpot虚拟机中，对象在内存中存储的布局可以分为三块区域：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。

- 对象头：比如 hash码，对象所属的年代，对象锁，锁状态标志，偏向锁（线程）ID，偏向时间，数组长度（数组对象）等。**Java对象头一般占有2个机器码**（在32位虚拟机中，1个机器码等于4字节，也就是32bit，在64位虚拟机中，1个机器码是8个字节，也就是64bit），但是 **如果对象是数组类型，则需要3个机器码，因为JVM虚拟机可以通过Java对象的元数据信息确定Java对象的大小**，但是无法从数组的元数据来确认数组的大小，所以用一块来记录数组长度。

- 实例数据：存放类的属性数据信息，包括父类的属性信息；

- 对齐填充：由于虚拟机要求 **对象起始地址必须是8字节的整数倍**。填充数据不是必须存在的，仅仅是为了字节对齐；

  ![image](https://note.youdao.com/yws/api/personal/file/F0873EAFC1D2460598178E919354A1C2?method=download&shareKey=27fe79fa0d84fe02d370c9b8edd36e1f)

![image](https://note.youdao.com/yws/api/personal/file/120B0C3079BC4491934447CC64977D80?method=download&shareKey=69ebe2d7a2011cb0a80516c6330ba731)