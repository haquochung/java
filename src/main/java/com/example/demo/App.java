package com.example.demo;

import java.util.Arrays;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        String binary = Integer.toBinaryString(101_2 );

        binary.split("1(0*\\B)1");

        Arrays.stream(binary.split("1(0*\\B)1")).filter(t -> !t.isEmpty()).collect(Collectors.toList());
        int a = 130;
        byte b = (byte) a;
        System.out.println(b);
//        byte c = (byte) (a + b);
//
//        Person person = new Person();
//        person.sayHow(getHow());
//        Person employee = new Employee();
//        employee.sayHow(getHow());
//        User user = employee;
//        user.sayHow(getHow());
    }

    public static String getHow() {
        return null;
    }

    /**
     * InnerApp
     */
    public interface How {
        public default void sayHow(String how) throws RuntimeException {
            sayHow(how, 1);
            try {
                if (null == how) throw new RuntimeException("again");
            } catch (RuntimeException rte) {

            }
        }

        public void sayHow(String how, int n);
        
    }

    public static class User implements How {
        @Override
        public void sayHow(String how, int n) {
            try {
                if (null == how) throw new RuntimeException("again user");
            } catch (RuntimeException rte) {
                System.out.println("user");
            }
        }
    }

    public static class Person extends User {
        public void start() {
            sayHow(null);
        }

        @Override
        public void sayHow(String how, int n) {
            try {
                if (null == how) throw new RuntimeException("again person");
            } catch (RuntimeException rte) {
                System.out.println("person");
            }
        }
    }

    public static class Employee extends Person {
        public void start() {
            sayHow(null);
        }

        @Override
        public void sayHow(String how, int n) {
            try {
                if (null == how) throw new RuntimeException("again emploee");
            } catch (RuntimeException rte) {
                System.out.println("emploee");
            }
        }
    }
}