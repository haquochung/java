package com.example.demo.enhance.thread;

import java.util.concurrent.RecursiveTask;

public class MultiTask extends RecursiveTask<Integer> {

    int start;
    int end;

    public MultiTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= 4) {
            int totalSum = 0;
            for (int i = start; i <= end; i++) {
                totalSum += i;
            }
            return totalSum;
        } else {
            int mid = (start + end) / 2;
            MultiTask leftTask = new MultiTask(start, mid);
            MultiTask rightTask = new MultiTask(mid + 1, end);
            leftTask.fork();
            rightTask.fork();

            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            return  leftResult + rightResult;
        }
    }
}
