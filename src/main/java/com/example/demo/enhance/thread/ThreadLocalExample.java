package com.example.demo.enhance.thread;

public class ThreadLocalExample {

    public static void main(String[] args) {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        int value = 5;
        value++;
        Thread t = new Thread(() -> {
            System.out.println("System");
        });
    }
}
