package com.example.demo.enhance.interfaces;

public interface InterfaceB {
    default void methodA() {
        System.out.println("Interface B");
    }
}
