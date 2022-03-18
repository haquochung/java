package com.example.demo.enhance.interview;

import java.util.Arrays;

/**
 * There is an array A consisting of N integers. Divide them into three non-empty groups. In each group we calculate the
 * differences between the largest and smallest integer. Our goal is to make the maximum of these differences as small
 * as possible.
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * <p>- N is an integer within the range [3...100,000].</p>
 * <p>- each element of array A is an integer within the range [1...1,000,000,000].</p>
 */
public class Task3 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{11, 5, 3, 12, 6, 8, 1, 7, 4})); // expect: 3
        System.out.println(solution(new int[]{10, 14, 12, 1_000, 11, 15, 13, 1})); // expect: 5
        System.out.println(solution(new int[]{4, 5, 7, 10, 10, 12, 12, 12})); // expect: 2
        System.out.println(solution(new int[]{5, 10, 10, 5, 5})); // expect: 0
    }

    public static int solution(int[] A) {
        Arrays.sort(A);

        int N = A.length;
        int result = 1_000_000_000; // range max

        int group1Difference;
        int group2Difference;
        int group3Difference;
        for (int i = 0; i < N - 2; i++) { // 1 3 4 5 6 7 8 11 12
            group1Difference = A[i] - A[0]; // 0
            for (int j = i + 1; j < N - 1; j++) {
                group2Difference = A[j] - A[i + 1]; // 0
                group3Difference = A[N - 1] - A[j + 1]; // 1
                result           = Math.min(
                        result, Math.max(Math.max(group1Difference, group2Difference), group3Difference));
            }
        }

        return result;
    }
}
