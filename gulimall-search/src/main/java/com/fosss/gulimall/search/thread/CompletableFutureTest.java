package com.fosss.gulimall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: fosss
 * Date: 2023/3/27
 * Time: 20:43
 * Description: CompletableFuture实现异步请求
 */
public class CompletableFutureTest {

    public static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //启动线程任务-runAsync   无返回值
        //CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getId());
        //}, service);

        //启动线程任务-supplyAsync   有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int res = 666 / 0;
            return res;
        }, service).whenCompleteAsync((res, exception) -> {
            //该方法能够获得结果和感知异常
            System.out.println("打印结果：" + res + " 抛出异常：" + exception);
        }, service).exceptionally((e) -> {
            //该方法能够感知异常并返回默认结果
            System.out.println("异常：" + e);
            return 6;
        }).handleAsync((res, exception) -> {
            //该方法能够获得结果、感知异常、返回结果，相当于whenCompleteAsync + exceptionally
            System.out.println("结果：" + res + " 异常：" + exception);
            return 66;
        }, service);

        System.out.println("返回值" + future.get());
    }
}



























