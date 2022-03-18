package com.example.demo.enhance.map;

import java.util.HashMap;
import java.util.Map;

public class MyHashMap {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>(80);
        String s = "1";
        int h;
        System.out.println(((h = s.hashCode()) ^ (h >>> 16)) & (16 - 1));
        System.out.println(((h = s.hashCode()) ^ (h >>> 16)) & (32 - 1));
        System.out.println(((h = s.hashCode()) ^ (h >>> 16)) & (48 - 1));
        System.out.println(((h = s.hashCode()) ^ (h >>> 16)) & (64 - 1));
        System.out.println(((h = s.hashCode()) ^ (h >>> 16)) & (80 - 1));
    }
}
