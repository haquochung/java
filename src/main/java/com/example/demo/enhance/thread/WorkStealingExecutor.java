package com.example.demo.enhance.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class WorkStealingExecutor {
    public static void main(String[] args) {

        try (ForkJoinPool pool = (ForkJoinPool) Executors.newWorkStealingPool()) {
            Future<Integer> future = pool.submit(new MultiTask(1, 101));
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
