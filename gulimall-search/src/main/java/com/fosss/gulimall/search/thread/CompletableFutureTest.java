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
            return 666;
        }, service);
        System.out.println("返回值" + future.get());
    }
}



























