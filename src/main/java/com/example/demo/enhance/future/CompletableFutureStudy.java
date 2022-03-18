package com.example.demo.enhance.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureStudy {

    public static void main(String[] args) {


//        CompletableFuture.supplyAsync(() -> supply(5))
//                .thenApply(CompletableFutureStudy::convert)
//                .thenApply(CompletableFutureStudy::convert)
//                .thenAccept(System.out::println)
//                .thenRun(CompletableFutureStudy::longProcess)
//                .thenRun(CompletableFutureStudy::longProcess);
//
//        sleepABit();
//        sleepABit();

        int val = 7;

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> supply(val));
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> supply2(val));
//        createFuture(completableFuture);

        feature1(val).thenCombine(feature2(val), Integer::sum)
                         .thenAccept(System.out::println);

        feature1(val).thenCompose(CompletableFutureStudy::feature2)
                         .thenAccept(System.out::println);

//        completableFuture.complete(8);

        sleepABit();
        sleepABit();
        sleepABit();
    }

    static CompletableFuture<Integer> feature1(Integer val) {
        return CompletableFuture.supplyAsync(() -> supply(val));
    }

    static CompletableFuture<Integer> feature2(Integer val) {
        return CompletableFuture.supplyAsync(() -> supply2(val));
    }

    static void createFuture(CompletableFuture<Integer> completableFuture) {
        completableFuture.thenApply(CompletableFutureStudy::convert)
                .thenApply(integer -> integer + 20)
                .thenAccept(System.out::println);
    }

    static Integer supply(int value) {
        sleepABit();
        System.out.println("supply " + Thread.currentThread());
        return value * 10;
    }

    static Integer supply2(int value) {

        System.out.println("supply2 " + Thread.currentThread());

        return value * 5;
    }

    static Integer convert(int value) {

        System.out.println("convert " + value + " " + Thread.currentThread());
        sleepABit();
        if (value % 2 == 0) {
            return value + 1;
        }
        return value + 3;
    }

    static void longProcess() {
        System.out.println("Sleep long");
        sleepABit();
    }

    public static void sleepABit() {
        System.out.println(Thread.currentThread());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
