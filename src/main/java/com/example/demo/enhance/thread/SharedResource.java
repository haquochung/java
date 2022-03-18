package com.example.demo.enhance.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

public class SharedResource {

    boolean isAvailable = false;

    StampedLock stampedLock = new StampedLock();
    ReentrantLock lock = new ReentrantLock();
    Semaphore semaphore = new Semaphore(2);
    Condition condition = lock.newCondition();

    public void producer() {
        long stamp = stampedLock.tryOptimisticRead();
        System.out.println("Stamp lock" + stamp);
        try {
            lock.lock();
            System.out.println("Lock acquired");
            isAvailable = true;
            if (stampedLock.validate(stamp)) {
                System.out.println("update success");
            } else {
                System.out.println("rollback of work");
            }
            Thread.sleep(5_000);
        } catch (Exception ignored) {

        } finally {
            lock.unlock();
            System.out.println("Lock released");
        }
    }

    public void consumer() {
        long stamp = stampedLock.writeLock();
        try {
            System.out.println("Write Lock acquired");
            lock.lock();
            isAvailable = true;
            Thread.sleep(5_000);
        } catch (Exception ignored) {

        } finally {
            stampedLock.unlockWrite(stamp);
            lock.unlock();
            System.out.println("Write Lock released");
        }
    }
}
