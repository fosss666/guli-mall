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






















