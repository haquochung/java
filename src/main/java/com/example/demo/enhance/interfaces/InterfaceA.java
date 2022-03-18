package com.example.demo.enhance.interfaces;

public interface InterfaceA {
    default void methodA() {
        System.out.println("Interface A");
    }
}
