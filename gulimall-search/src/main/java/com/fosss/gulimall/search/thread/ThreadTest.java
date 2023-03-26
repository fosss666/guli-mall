package com.fosss.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @author: fosss
 * Date: 2023/3/26
 * Time: 18:48
 * Description:  异步操作
 */
public class ThreadTest {
    /**
     * 初始化线程的四种方式
     * 1）、继承 Thread
     * 2）、实现 Runnable 接口
     * 3）、实现 Callable 接口 + FutureTask （可以拿到返回结果，可以处理异常）
     * 4）、线程池
     * 创建方式：1 Executors工具类  2.原生的ThreadPoolExecutor
     *
     * * ThreadPoolExecutor 的七大参数
     * * @param corePoolSize the number of threads to keep in the pool, even
     * *        if they are idle, unless {@code allowCoreThreadTimeOut} is set  核心线程数
     * * @param maximumPoolSize the maximum number of threads to allow in the
     * *        pool     最大线程数
     * * @param keepAliveTime when the number of threads is greater than
     * *        the core, this is the maximum time that excess idle threads
     * *        will wait for new tasks before terminating.  线程存活时间
     * * @param unit the time unit for the {@code keepAliveTime} argument  时间单位
     * * @param workQueue the queue to use for holding tasks before they are
     * *        executed.  This queue will hold only the {@code Runnable}
     * *        tasks submitted by the {@code execute} method.  阻塞队列
     * * @param threadFactory the factory to use when the executor
     * *        creates a new thread   线程工厂
     * * @param handler  the handler to use when execution is blocked
     * *        because the thread bounds and queue capacities are reached  拒绝策略，如果线程满了，线程池就会使用拒绝策略
     * <p>
     * 线程池运行流程：
     * 1、线程池创建，准备好 core 数量的核心线程，准备接受任务
     * 2、新的任务进来，用 core 准备好的空闲线程执行。
     * (1) 、core 满了，就将再进来的任务放入阻塞队列中。空闲的 core 就会自己去阻塞队 列获取任务执行
     * (2) 、阻塞队列满了，就直接开新线程执行，最大只能开到 max 指定的数量
     * (3) 、max 都执行好了。Max-core 数量空闲的线程会在 keepAliveTime 指定的时间后自 动销毁。最终保持到 core 大小
     * (4) 、如果线程数开到了 max 的数量，还有新任务进来，就会使用 reject 指定的拒绝策 略进行处理
     * 3、所有的线程创建都是由指定的 factory 创建的。
     */

    //4
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1
        Thread01 thread01 = new Thread01();
        thread01.start();

        //2
        Runnable01 runnable01 = new Runnable01();
        new Thread(runnable01).start();

        //3
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        futureTask.run();
        System.out.println(futureTask.get());

        //4
        executorService.execute(new Runnable01());
    }

}

class Callable01 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("实现 Callable 接口 + FutureTask方式启动线程，线程号为：" + Thread.currentThread().getId());
        return 666;
    }
}

class Runnable01 implements Runnable {

    @Override
    public void run() {
        System.out.println("实现 Runnable 接口方式启动线程，线程号为：" + Thread.currentThread().getId());
    }
}

class Thread01 extends Thread {
    @Override
    public void run() {
        System.out.println("继承 Thread 方式启动线程，线程号为：" + Thread.currentThread().getId());
    }
}






















