# **AQS应用之Lock**

Java并发编程核心在于java.util.concurrent包而juc当中的大多数同步器实现都是围绕着共同的基础行为，比如等待队列、条件队列、独占获取、共享获取等，而这个行为的抽象就是基于AbstractQueuedSynchronizer简称AQS，AQS定义了一套多线程访问共享资源的同步器框架，是一个依赖状态(state)的同步器。

**ReentrantLock**

ReentrantLock是一种基于AQS框架的应用实现，是JDK中的一种线程并发访问的同步手段，它的功能类似于synchronized是一种互斥锁，可以保证线程安全。而且它具有比synchronized更多的特性，比如它支持手动加锁与解锁，支持加锁的公平性。

```
使用ReentrantLock进行同步
ReentrantLock lock = new ReentrantLock(false);//false为非公平锁，true为公平锁

大致猜想实现内部
T1  T2  T3
lock.lock() //加锁
	
	while（true）{
		if（CAS加锁成功）{ // cas 比较与交换
			break; 跳出循环
		}
		// Thread.yeild(); 让出CPU使用权
		// Thread.sleep(1);  无法控制业务时间
		HashSet  LikedQueue
		HashSet.add(Thread)
		LikedQueue.pull()
        线程阻塞，不会占用CPU资源
        LockSupport.park();	
       
	}
T1   xxx 业务
	 xxx 业务
lock.unlock() //解锁
Thread t = HashSet.get()
Thread t = LikedQueue.take();
// 唤醒
LockSupport.unpark(t);

```

推算锁四大核心

**自旋，LockSupport,  cas 加锁 ，queue队列**



ReentrantLock如何实现synchronized不具备的公平与非公平性呢？

在ReentrantLock内部定义了一个Sync的内部类，该类继承AbstractQueuedSynchronized，对该抽象类的部分方法做了实现；并且还定义了两个子类：

1、FairSync 公平锁的实现

2、NonfairSync 非公平锁的实现

这两个类都继承自Sync，也就是间接继承了AbstractQueuedSynchronizer，所以这一个ReentrantLock同时具备公平与非公平特性。

上面主要涉及的设计模式：模板模式-子类根据需要做具体业务实现

**AQS具备特性**

- 阻塞等待队列
- 共享/独占
- 公平/非公平
- 可重入
- 允许中断

除了Lock外，Java.util.concurrent当中同步器的实现如Latch,Barrier,BlockingQueue等，都是基于AQS框架实现

- 一般通过定义内部类Sync继承AQS
- 将同步器所有调用都映射到Sync对应的方法

AQS内部维护属性**volatile** **int state (32位)**

- state表示资源的可用状态

State三种访问方式

getState()、setState()、compareAndSetState()

AQS定义两种资源共享方式

- Exclusive-独占，只有一个线程能执行，如ReentrantLock
- Share-共享，多个线程可以同时执行，如Semaphore/CountDownLatch

AQS定义两种队列

- 同步等待队列
- 条件等待队列

不同的自定义同步器争用共享资源的方式也不同。自定义同步器在实现时只需要实现共享资源state的获取与释放方式即可，至于具体线程等待队列的维护（如获取资源失败入队/唤醒出队等），AQS已经在顶层实现好了。自定义同步器实现时主要实现以下几种方法：

- isHeldExclusively()：该线程是否正在独占资源。只有用到condition才需要去实现它。
- tryAcquire(int)：独占方式。尝试获取资源，成功则返回true，失败则返回false。
- tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败则返回false。
- tryAcquireShared(int)：共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
- tryReleaseShared(int)：共享方式。尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false。

**同步等待队列**

AQS当中的同步等待队列也称CLH队列，CLH队列是Craig、Landin、Hagersten三人发明的一种基于双向链表数据结构的队列，是FIFO先入先出线程等待队列，Java中的CLH队列是原CLH队列的一个变种,线程由原自旋机制改为阻塞机制。



**条件等待队列**

Condition是一个多线程间协调通信的工具类，使得某个，或者某些线程一起等待某个条件（Condition）,只有当该条件具备时，这些等待线程才会被唤醒，从而重新争夺锁

