package com.example.demo.interview;

import java.util.*;
import java.util.stream.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    public static void main(String[] args) {
        System.out.println(solution(5));
    }

    public static int solution(int N) {
        String binary = Long.toBinaryString(N);
        if (!binary.contains("0")) {
            return 0;
        }

        List<String> gaps = Arrays.stream(binary.split("1")).filter(g -> !g.isEmpty())
                                  .collect(Collectors.toList());

        if (gaps.size() <= 1 && binary.lastIndexOf("1") < binary.length() - 1) {
            return 0;
        }
        String longestGap = gaps.stream().max(Comparator.comparingInt(String::length)).get();
        return longestGap.length();
    }
}