package com.example.demo.enhance.datatype;


public class StringType {
    public static void main(String[] args) {
        String s = "This " + 20 + " is " + Boolean.valueOf(true);
        System.out.println(s);
        String longS = "This 20 is true";
        System.out.println(s == longS);
//        String s1 = "Hello"; // cached in string pool
//        String s2 = "Hello"; // now s2 will also point to the same string object in string pool
//        System.out.println(s1 == s2); // will be true
//
//        String s3 = new String("Hello"); // this string is not interned
//        System.out.println(s1 == s3); // will be false
//
//        String s4 = new String("Hello").intern(); // this string is interned
//        System.out.println(s1 == s4); // will be true
//
//        String s5 = s1.substring(1,3);
//        System.out.println(s5);

    }
}
