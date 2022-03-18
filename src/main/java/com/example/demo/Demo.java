//package com.example.demo;
//
//import java.time.DayOfWeek;
//import java.util.Date;
//import java.util.concurrent.Future;
//
//public class Demo extends MyClass2 {
//    public static void main(String[] args) {
////        Date date = new Date(2004 - 1900, 2 - 1, 29);
////        System.out.println(date.getDay());
////        DayOfWeek dayofWeek = DayOfWeek.of(date.getDay() + 1);
////        System.out.println(dayofWeek.name());
////
////        System.out.println(Demo.reverseString("ab-cd"));
////        System.out.println(Demo.reverseString("a-bC-dEf-ghIj")); //expect: j-Ih-gfE-dCba
////
////
////        System.out.println(func(40));
////
////        int n = 10;
////        Test test = new Test();
////        test.func(n);
////        System.out.println(n);
////
////        Integer x1 = 127;
////        int x2 = 127;
////        Integer x3 = 127;
////        Integer x4 = 0;
////        Integer x5 = 0;
////
////        System.out.println(x1 == x2);
////        System.out.println(x1 == x3);
////        System.out.println(x4 == x5);
//
//
////        int x= 5;
////        int y = x++;
////        System.out.println(y);
////        int z = ++y;
////        System.out.println(z);
////
//        String a = { "a", "b", "c"};
//        try {
//            int[] a = new int[5];
//            String s = "1.0";
//            try {
//                System.out.println(Integer.parseInt(s + a[4]));
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Exception 1" + e.getClass());
//
//            }
//            throw new Exception();
//        } catch (Exception e) {
//            System.out.println("Exception 2");
//        }
//
//    }
//
//    static class Test {
//        void func(int n) {
//           n += 30;
//        }
//    }
//
//    static int func(int n) {
//        if (n <= 0) {
//            return 0;
//        }
//
//        return n + func(n - 1);
//    }
//
//    public static String reverseString(String s) {
//        StringBuilder rslt = new StringBuilder();
//        char special = '-';
//        char c;
//
//        for (int i = s.length() - 1; i >= 0; i--) {
//            c = s.charAt(i);
//            if (c != special) {
//                rslt.append(s.charAt(i));
//            }
//        }
//
//        int index = s.indexOf(special);
//        while (index >= 0) {
//            rslt.insert(index, special);
//            index = s.indexOf(special, index + 1);
//        }
//
//        return rslt.toString();
//    }
//}
//
//@FunctionalInterface
//interface MyInterface {
//    void foo();
//}
//
//class MyClass implements MyInterface {
//    @Override
//    public void foo() {
//        System.out.println("Hello");
//    }
//}
//
//class MyClass2 extends MyClass implements MyInterface {
//    @Override
//    public void foo() {
//        System.out.println("Hello 2");
//    }
//}