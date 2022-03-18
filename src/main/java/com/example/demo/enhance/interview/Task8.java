package com.example.demo.enhance.interview;

import java.util.Arrays;

/**
 * <p>
 * Write a function solution that, given a string S consisting of N characters,
 * returns the alphabetically smallest string that can ben obtained by removing
 * exactly one letter from S
 * </p>
 *
 * Write an efficient algorithm for the following assumptions:
 * <p>- N is an integer within the range [2...100,000].</p>
 * <p>- string S is made only of lowercase letter (a-z).</p>
 *
 */
public class Task8 {
    public static void main(String[] args) {
        System.out.println(solution("abc")); // expect: ab
        System.out.println(solution("hot")); // expect: ho
        System.out.println(solution("codility")); // expect: codilit
        System.out.println(solution("aaaa")); // expect: aaa
        System.out.println(solution("ab")); // expect: a
        System.out.println(solution("ba")); // expect: a
        System.out.println(solution("aaxybch")); // expect: aaxbch
        System.out.println(solution("aaxyzbch")); // expect: aaxybch
    }

    public static String solution(String S) {
        char[] chars = S.toCharArray();

        Arrays.sort(chars);

        char biggestChar = chars[chars.length - 1];
        int removeIndex = S.indexOf(biggestChar);

        return S.substring(0, removeIndex).concat(S.substring(removeIndex + 1));
    }
}
