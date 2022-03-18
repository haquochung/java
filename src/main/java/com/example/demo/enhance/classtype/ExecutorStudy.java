package com.example.demo.enhance.classtype;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorStudy {
    public static void main(String[] args) {
        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(10),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy())) {
            Future<?> futureObj1 = executor.submit(() -> System.out.println("This is the task 1"));
            try {
                Object o = futureObj1.get();
                System.out.println(o == null);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            Future<?> futureObj2 = executor.submit(() -> {
                System.out.println("This is the task 2");
                return 45;
            });
            try {
                Object o = futureObj2.get();
                System.out.println(o == null);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            List<Integer> output = new ArrayList<>();
            String result = "SUCCESS";
            Future<String> futureObj3 = executor.submit(new CustomRunnable(output), result);
            try {
                String o = futureObj3.get();
                System.out.println(o);

                System.out.println(output);
                System.out.println(result);

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("Thread supplyAsync name: " + Thread.currentThread().getName());
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                return "Test ";
            }, executor).thenApply((String r) -> {
                System.out.println("Thread thenApply name: " + Thread.currentThread().getName());
                return r + "haha ";
            }).thenApplyAsync((String r) -> {
                System.out.println("Thread thenApplyAsync name: " + Thread.currentThread().getName());
                return r + "haha";
            }, executor).thenCompose((String r) -> {
                System.out.println("Thread thenCompose name: " + Thread.currentThread().getName());
                return CompletableFuture.supplyAsync(() -> r + "haha", executor);
            }).thenComposeAsync((String r) -> {
                System.out.println("Thread thenComposeAsync name: " + Thread.currentThread().getName());
                return CompletableFuture.supplyAsync(() -> r + "haha", executor);
            }, executor);
            System.out.println(completableFuture.get());
            completableFuture.thenAccept(System.out::println);

            CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
                System.out.println("Thread supplyAsync name: " + Thread.currentThread().getName());
                //                try {
                //                    Thread.sleep(5000);
                //                } catch (InterruptedException e) {
                //                    throw new RuntimeException(e);
                //                }
                return 1;
            }, executor);

            CompletableFuture<String> completableFuture3 = completableFuture.thenCombineAsync(completableFuture2,
                    (val1, val2) -> val1 + val2, executor);
            System.out.println(completableFuture3.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        class A {

        }

        ExecutorStudy test2 = new ExecutorStudy();
    }

    private TestA test = new TestA();

    ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

    class TestA extends ExecutorStudy {
        public TestA() {
            Future<?> future = service.submit(() -> System.out.println("haha123123123123"));

            try {
                System.out.println(future.get());
            } catch (Exception ingored) {
                System.out.println(ingored);
            }
        }
    }
}

class CustomRunnable implements Runnable {

    private final List<Integer> list;

    CustomRunnable(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        list.add(1);
        list.add(2);
        list.add(3);
    }
}
