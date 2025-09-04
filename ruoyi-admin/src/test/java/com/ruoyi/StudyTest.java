package com.ruoyi;

import cn.hutool.core.thread.ThreadUtil;
import com.ruoyi.bean.Dog;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName StudyTest
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-05-20 20:44
 */
@Slf4j
public class StudyTest {

    @Test
    public void test1() {
//        Collections.shuffle();
//        int[] arr = new int[10];
//        System.out.println(arr[1]);
//        System.out.println(Math.pow(2, 31));
//        Stack<Integer> stack = new Stack<>();
//        stack.push(1);
//        stack.push(2);
//        stack.push(3);
//        while (!stack.isEmpty()) {
//            System.out.println(stack.pop());
//        }

//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(1);
//        queue.add(2);
//        queue.add(3);
//        while (!queue.isEmpty()) {
//            System.out.println(queue.poll());
//        }

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            list.add(new String("12312321321312"));
//        }
//
//        ThreadUtil.sleep(1000 * 1000);

//        List<Dog> dogs = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Dog dog = Dog.builder()
//                    .name("dog" + i)
//                    .age(i == 1 ? null : i)
//                    .build();
//            dogs.add(dog);
//        }
//        // 流式操作的Collectors.toMap的value不能为空，否则会报NullPointException
//        Map<String, Integer> dogMap = dogs.stream()
//                .filter(dog -> dog.getAge() != null)
//                .collect(Collectors.toMap(Dog::getName, Dog::getAge));
//        System.out.println(dogMap);

//        String s = null;
//        System.out.println(Optional.of(s).get());
//        System.out.println(Optional.ofNullable(s).orElse("11"));



    }

    @Test
    public void test2() {
        // 集合增强for循环，实际是语法糖，编译后会使用迭代器方式遍历，迭代器的修改会校验修改次数，如果修改次数与预期不符，会抛出异常
        // fail-fast:https://www.cnblogs.com/54chensongxia/p/12470446.html
        // java.util下面的集合都有fail-fast 机制，即集合在迭代过程中不允许修改，否则会抛出ConcurrentModificationException异常
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
//        for (int i = 0; i < list.size(); i++) {
//            Integer remove = list.remove(i);
//            log.info("remove:{},list:{}", remove, list);
//        }
//        System.out.println(list);
        for (Integer item : list) {
            log.info("item:{}, list:{}", item, list);
//            list.remove(item.intValue());
            list.remove(item);
        }

    }


    @Test
    public void test3() {
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i);
//        }
//        Integer[] arr = list.toArray(new Integer[0]);
//        System.out.println(Arrays.toString(arr));

        // 使用工具类 Arrays.asList() 把数组转换成集合时，不能使用其修改集合相关的方法， 它的 add/remove/clear 方法会
        // 抛出 UnsupportedOperationException 异常。
        // 实际返回的是一个Arrays的内部类ArrayList，而不是java.util.ArrayList。该类没有重写父类的add/remove/clear方法，所以会抛出异常。
        String[] ss = {"1", "2"};
        List<String> list = Arrays.asList(ss);
        list.remove("1");

    }

    @Test
    public void test4() {
//        HashMap<Object, Object> map = new HashMap<>();

        // 优先级队列堆。
        int[] arr = { 1, 3, 2, 6, 4, 7, 7, 10, 8};
        PriorityQueue priorityQueue = new PriorityQueue();
        for (int item : arr) {
            priorityQueue.offer(item);
        }
//        for (Object o : priorityQueue) {
//            System.out.println(o);
//        }
        while (!priorityQueue.isEmpty()) {
            System.out.println(priorityQueue.poll());
        }
    }




    @Test
    public void test5() {
        // 延迟队里
        DelayQueue<DelaydObj> delayQueue = new DelayQueue<>();
        delayQueue.add(new DelaydObj(3000, "1"));
        delayQueue.add(new DelaydObj(2000, "2"));
        delayQueue.add(new DelaydObj(5000, "3"));
        while (!delayQueue.isEmpty()) {
            try {
                DelaydObj delaydObj = delayQueue.take();
                log.info("delaydObj:{}", delaydObj);
            } catch (InterruptedException e) {
                log.error("中断异常", e);
            }
        }
    }

    @Test
    public void test6() {
        // jdk1.7用的分段锁（默认16），将元素hash分配到一个段中。段继承了ReentrantLock,put元素时会调用段的put方法，然后加锁。
        // jdk1.8在初始化表和插入桶（数组Node节点）的第一个元素时使用自旋+cas的锁，如果桶上已有元素，则使用syncronized锁
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        map.put("1", "1");
    }

    @Test
    public void test7() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            log.info("threadId:{}，name:{},  threadState:{}", threadInfo.getThreadId(), threadInfo.getThreadName(),
                    threadInfo.getThreadState());
        }

    }

    @Test
    public void test8() {
        // 模拟死锁
        Object a = new Object();
        Object b = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (a) {
                ThreadUtil.sleep(1000 * 3);
                synchronized (b) {
                    log.info("thread1");
                }
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            synchronized (b) {
                ThreadUtil.sleep(1000 * 3);
                synchronized (a) {
                    log.info("thread1");
                }
            }
        });
        t2.start();

        ThreadUtil.sleep(1000 * 10);

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
//            log.info("threadId:{}，name:{},  threadState:{}", threadInfo.getThreadId(), threadInfo.getThreadName(),
//                    threadInfo.getThreadState());
            log.info("{}", threadInfo);
        }

        ThreadUtil.sleep(1000 * 100);
    }

    @Test
    public void test9() throws ExecutionException, InterruptedException {
        ThreadLocal<Integer> tl = new ThreadLocal<>();
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
        // 是否核心线程超过一定时间没有任务需要回收销毁
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        // 通过prestartCoreThread和prestartAllCoreThreads方法可以提前将核心线程创建好，不用等任务过来了才创建
        // threadPoolExecutor.prestartCoreThread();
        // threadPoolExecutor.prestartAllCoreThreads();
        threadPoolExecutor.execute(() -> {
            tl.set(1);
            log.info("tl1:{}", tl.get());
            countDownLatch.countDown();
        });
        FutureTask futureTask = new FutureTask<>(()-> {
            tl.set(2);
            log.info("tl2:{}", tl.get());
            countDownLatch.countDown();
            return tl.get();
        });
        threadPoolExecutor.execute(futureTask);
        Object o = futureTask.get();
        log.info("futureRes:{}", o);
        countDownLatch.await();

        // 线程池处理任务流程：
        // 一个任务过来了，先看核心线程池满了么，没满的话创建一个核心线程，满的话去看看队列有没有满。队列没满的话，放入队列等待执行，队列满的话
        // 去看看最大线程数满了么。最大线程没满，创建一个线程去执行，满的话执行拒绝策略（默认抛异常，可选的有在主线程执行、抛弃、抛弃最早的，可
        // 自定义拒绝策略）
    }



    @Test
    public void test10() throws InterruptedException {
        // 使用InheritableThreadLocal来将父线程的threadlocal的值传递到子线程。但子线程的修改不会影响父线程
        // 但是如果是线程池会出问题，因为线程池的线程会复用
        InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
        inheritableThreadLocal.set(10);
        Thread thread = new Thread(() -> {
            System.out.println(inheritableThreadLocal.get());
            inheritableThreadLocal.set(11);
        });
        thread.start();
        thread.join();
        System.out.println(inheritableThreadLocal.get());

        // 对于线程池场景，推荐使用阿里巴巴的TransmittableThreadLocal
    }

    @Test
    public void test11() throws ExecutionException, InterruptedException {
        // 任务编排场景非常适合通过CompletableFuture实现。比如一个任务需要依赖另外的其他两个任务执行完
        // CompletableFuture默认使用全局共享的 ForkJoinPool.commonPool() 作为执行器
        // 建议为 CompletableFuture 提供自定义线程池。参数带上线程池对象
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(1000 * 3);
            log.info("{}:任务1", Thread.currentThread().getName());
            int i = 1 / 0;
            return 1;
        });
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            log.info("{}:任务2", Thread.currentThread().getName());
        });
        // 将两个任务组合起来，等待他们都完成
        CompletableFuture<Void> twoTask = CompletableFuture.allOf(task1, task2);
        // completableFuture.join();

        CompletableFuture<Void> lastTask = twoTask.thenRunAsync(() -> {
            try {
                log.info("{}:lastTask, task1结果：{}", Thread.currentThread().getName(), task1.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        lastTask.exceptionally(e -> {
//            log.error("lastTask error:{}", e.getMessage());
//            return null;
//        });
        lastTask.join();
    }


    @Test
    public void test12() throws InterruptedException {
        // 信号量，控制数量3.同时只能3个能获取。可用作单机限流
        Semaphore semaphore = new Semaphore(2);

        Runnable r = () -> {
            try {
                semaphore.acquire();
                log.info("{}获取到了", Thread.currentThread().getName());
                ThreadUtil.sleep(1000 *3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

    @Test
    public void test13() throws InterruptedException {
        // 循环屏障. 等待所有线程执行完毕再继续执行
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Runnable r = () -> {
            try {
                log.info("{}开始执行", Thread.currentThread().getName());
                ThreadUtil.sleep(1000 * 3);
                cyclicBarrier.await();
                log.info("{}执行完毕", Thread.currentThread().getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void test14() throws InterruptedException {
        AtomicInteger num = new AtomicInteger(0);
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        Runnable r = () -> {
            while (true) {

                if (num.get() > 10) {
                    break;
                }
                System.out.println(Thread.currentThread().getName() + ":" +  num.incrementAndGet());
//                int i = num.incrementAndGet();
//                try {
//                    cyclicBarrier.await();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                if (i == 3) {
//                    cyclicBarrier.reset();
//                }

//                log.info("{} {}", Thread.currentThread().getName(), i);
//                ThreadUtil.sleep(300);

            }
        };
        CompletableFuture.allOf(CompletableFuture.runAsync(r), CompletableFuture.runAsync(r),
                        CompletableFuture.runAsync(r))
                .join();
    }

    @Test
    public void test15() {
        // jdk1.8前使用分段锁，每个段加锁
        // jdk1.8的Node使用cas操作保证原子性，链表使用synchcronized加锁
//        ConcurrentHashMap<Object, Object> hashMap = new ConcurrentHashMap<>();
        // 在读多写少的场合性能非常好，远远好于 Vector。他的写操作加锁，读不加锁，写操作是复制了一份原始数组，然后写完替换老的数组
        // 写时复制策略
//        CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();
//        ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();

        // 将集合变成同步的集合
//        Map<Object, Object> map = Collections.synchronizedMap(new HashMap<>());
//        Collection<Object> list = Collections.synchronizedCollection(new ArrayList<>());

        // 跳表.通哟多级索引，空间换时间
//        ConcurrentSkipListMap<Object, Object> skipMap = new ConcurrentSkipListMap<>();

        // 1.cas，CompareAndSwap,通过Unsafe类（普通开发没法通过常规方法使用）下的compareAndSwapXX，通过对比内存中的值和预期的值是否一样
        // 来判断是否有其他线程在操作该对象，这是一个原子操作（native方法，c++实现，底层硬件提供的指令）。可通过自旋+cas实现锁，但竞争激烈的
        // 时候会长期占用cpu资源。乐观锁实现方式，类似版本号的方式
        // 2.synchronized锁升级：先是无锁，单线程多次进入了，偏向锁；多个线程竞争，变成轻量级锁（自旋+cas）；自旋次数达到一定数量，变成重量
        // 级锁（操作系统级别的锁，阻塞线程，让出cpu，但会产生上下问切换）
        // 3.AQS,AbstractQueueSynchronizer,抽象队列同步器，ReentrantLocak，Semaphorer，CountDownLatch等都是基于AQS实现，实现AQS
        // 自定义自己的同步器。通过cas+阻塞方式实现同步
        // --如果线程获取锁失败，会先短暂自旋尝试获取锁；
        // --如果仍然失败，则线程会进入阻塞状态，等待被唤醒，从而减少 CPU 的浪费。
    }

    @Test
    public void test16() throws ExecutionException, InterruptedException {
        try {
            CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
                throw new RuntimeException("f1 error");
            });
            f1.join();
        } catch (Exception e) {
            log.error("errror:", e);
        }

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 1);
        f2.join();
        log.info("{}", f2.get());

        try {
            CompletableFuture<Integer> f3 = CompletableFuture.supplyAsync(() -> {
                throw new RuntimeException("f3 error");
            });
            f3.exceptionally(t -> {
                log.info("123:{}", t.getMessage());
                return 0;
            });
            f3.join();
        } catch (Exception e) {
            log.error("f3 excption:", e);
        }



    }

    @Data
    class DelaydObj implements Delayed {

        /**
         * 执行时间。单位毫秒
         */
        private long executeMilliseconds;

        private String name;

        public DelaydObj(long milliseconds, String name) {
            this.executeMilliseconds = milliseconds + System.currentTimeMillis();
            this.name = name;
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            return unit.convert(executeMilliseconds - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@NotNull Delayed o) {
            return Long.compare(this.executeMilliseconds, ((DelaydObj) o).getExecuteMilliseconds());
        }
    }

}
