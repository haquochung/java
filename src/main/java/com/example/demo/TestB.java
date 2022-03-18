package com.example.demo;

public class TestB {
/**
 * Given an input string s, reverse the order of the words.
 * A word is defined as a sequence of non-space characters. The words in s will be separated by at least one space.
 * Return a string of the words in reverse order concatenated by a single space.
 * Note that s may contain leading or trailing spaces or multiple spaces between two words. The returned string should only have a single space separating the words. Do not include any extra spaces.
 *
 * Example 1:
 * Input: s = "the sky is blue"Output: "blue is sky the"
 * Example 2:
 * Input: s = "  hello world  "Output: "world hello"
 * Explanation: Your reversed string should not contain leading or trailing spaces.
 *
 * Example 3:
 * Input: s = "a good   example"Output: "example good a"Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
  */

public static void main(String[] args) {

    String s1 = "the sky is blue";
    String s2 = "  hello world  ";
    String s3 = "a good   example";

    System.out.println(reverseWords(s1));
    System.out.println(reverseWords(s2));
    System.out.println(reverseWords(s3));

}

public static String reverseWords(String s) {
    StringBuilder rslt = new StringBuilder();
    // "  hello world  " -> arr: "hello", "world"
    String[] words = s.trim().split(" ");

    for (int index = words.length - 1; index >= 0; index--) {
        if (words[index].trim().isEmpty()) {
            continue;
        }

        rslt.append(words[index]).append(" ");
    }


    return rslt.toString().trim();
}
}
