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
        //CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getId());
        //    int res = 666 / 0;
        //    return res;
        //}, service).whenCompleteAsync((res, exception) -> {
        //    //该方法能够获得结果和感知异常
        //    System.out.println("打印结果：" + res + " 抛出异常：" + exception);
        //}, service).exceptionally((e) -> {
        //    //该方法能够感知异常并返回默认结果
        //    System.out.println("异常：" + e);
        //    return 6;
        //}).handleAsync((res, exception) -> {
        //    //该方法能够获得结果、感知异常、返回结果，相当于whenCompleteAsync + exceptionally
        //    System.out.println("结果：" + res + " 异常：" + exception);
        //    return 66;
        //}, service);
        //
        //System.out.println("返回值" + future.get());

        /**
         * 线程串行化
         */

        //1
        //CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getId());
        //    int res = 10 / 2;
        //    return res;
        //}, service).thenRunAsync(() -> {
        //    //thenRunAsync 无法获得结果，无返回值
        //    System.out.println("线程2启动了");
        //}, service);

        //2
        //CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getId());
        //    int res = 10 / 2;
        //    return res;
        //}, service).thenAcceptAsync((res) -> {
        //    //thenAcceptAsync 可以获得结果，无返回值
        //    System.out.println("线程2启动了,结果为："+res);
        //}, service);

        //3
        //CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getId());
        //    int res = 10 / 2;
        //    return res;
        //}, service).thenApplyAsync((res) -> {
        //    //thenApplyAsync 可以获得结果，有返回值
        //    System.out.println("线程2启动了,结果为：" + res);
        //    return res;
        //}, service);
        //System.out.println("返回：" + future.get());

        /**
         * 两任务组合-都完成后再执行线程3
         */
        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程1:" + Thread.currentThread().getId());
            return 1;
        }, service);

        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程2:" + Thread.currentThread().getId());
            return "呵呵";
        }, service);

        //future01.runAfterBoth(future02,()->{
        //    System.out.println("线程3："+Thread.currentThread().getId());
        //});

        //future01.thenAcceptBothAsync(future02,(f1,f2)->{
        //    System.out.println("线程1返回："+f1+" 线程2返回："+f2);
        //    System.out.println("线程3："+Thread.currentThread().getId());
        //},service);

        //CompletableFuture<String> future03 = future01.thenCombineAsync(future02, (f1, f2) -> {
        //    System.out.println("线程1返回：" + f1 + " 线程2返回：" + f2);
        //    System.out.println("线程3：" + Thread.currentThread().getId());
        //    return f1 + "->" + f2;
        //}, service);
        //System.out.println("线程3返回：" + future03.get());

        /**
         * 两任务组合-一个完成后就能执行线程3
         */
        //future01.runAfterEitherAsync(future02, () -> {
        //    System.out.println("线程3：" + Thread.currentThread().getId());
        //}, service);
        //
        //future01.acceptEitherAsync(future02, (res) -> {
        //    System.out.println("任务3开始之前的返回的结果" + res);
        //    System.out.println("线程3：" + Thread.currentThread().getId());
        //}, service);
        //
        //CompletableFuture<Object> future03 = future01.applyToEitherAsync(future02, (res) -> {
        //    System.out.println("任务3开始之前的返回的结果" + res);
        //    System.out.println("线程3：" + Thread.currentThread().getId());
        //    return res;
        //}, service);
        //System.out.println("线程3返回：" + future03.get());


    }
}



























