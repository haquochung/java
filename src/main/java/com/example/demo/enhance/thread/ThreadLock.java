package com.example.demo.enhance.thread;


import java.lang.reflect.Method;

public class ThreadLock {
    public static void main(String[] args) {
//        SharedResource sharedResource = new SharedResource();
//
//        Thread t1 = new Thread(sharedResource::producer);
//        Thread t2 = new Thread(sharedResource::consumer);
//
//        t1.start();
//        t2.start();

        Class th = ThreadLock.class;
        for (Method m : th.getMethods()) {
            System.out.println(m.getName());
            System.out.println(m.getDeclaringClass());
        }
    }
}
