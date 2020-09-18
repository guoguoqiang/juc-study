```
ReentrantLock lock = new ReentrantLock(true);
```

lock.lock()解析

tryAcquire：尝试获取锁



```
  final void lock() {
      // 获取锁
      acquire(1);
  }
```



```
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

拆分：1、tryAcquire() 尝试获取锁

```
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    // 获取当前同步器状态
    int c = getState();
    if (c == 0) {
        // 是否有线程在排队
        if (!hasQueuedPredecessors() &&
            // cas修改状态 0 --> 1
            compareAndSetState(0, acquires)) {
            // 设置当前线程的引用
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    // 是否是当前线程，重入
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

拆分2、 获取锁失败的入队

```
// 获取锁失败的入队
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

