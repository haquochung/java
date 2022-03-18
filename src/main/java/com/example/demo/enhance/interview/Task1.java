package com.example.demo.enhance.interview;

import java.util.HashMap;
import java.util.Map;

/**
 * There is an array A of N integers sorted in non-decreasing order. In one move, you can either remove an integer from
 * A or insert an integer before or after any element of A. The goal is achieved an array in which all values X that are
 * present in the array occur exactly X times.
 * What is the minimum number of moves after which every value X in the array occurs exactly X times?
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * <p>- N is an integer within the range [1...100,000].</p>
 * <p>- each element of array A is an integer within the range [1...100,000,000].</p>
 * <p>- elements of array A are stored in non-decreasing order.</p>
 */
public class Task1 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 1, 3, 4, 4, 4})); // expect: 3
        System.out.println(solution(new int[]{1, 2, 2, 2, 5, 5, 5, 8})); // expect: 4
        System.out.println(solution(new int[]{1, 1, 1, 1, 3, 3, 4, 4, 4, 4, 4})); // expect: 5
        System.out.println(solution(new int[]{10, 10, 10})); // expect: 3
    }

    public static int solution(int[] A) {
        Map<Integer, Integer> occurTimesMap = new HashMap<>();
        for (int num : A) {
            occurTimesMap.merge(num, 1, (a, b) -> a + 1);
        }

        int result = 0;
        int key;
        int value;

        for (Map.Entry<Integer, Integer> entry : occurTimesMap.entrySet()) {
            key   = entry.getKey();
            value = entry.getValue();

            if (key < value) {
                result = result + (value - key);
            } else if (key > value) {
                result = result + Math.min(key - value, value);
            }
        }

        return result;
    }
}
