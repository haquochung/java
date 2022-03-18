package com.example.demo.enhance.array;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MergeAndSort {

    public static void main(String[] args) throws ParseException, FileNotFoundException {
        try (Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream("data.txt"))) {
            List<String> unsorted = new ArrayList<>();
            while (s.hasNext()) {
                unsorted.add(s.next());
            }
            unsorted.sort((a, b) -> {
                if (a.length() > b.length()) {
                    return 1;
                } else if (a.length() < b.length()) {
                    return -1;
                }
                return new BigInteger(a).compareTo(new BigInteger(b));
            });
            System.out.println(unsorted);
        }
        try (Scanner sc=new Scanner(System.in)) {
            sc.hasNextLine();
            sc.next();
        }
        int[] nums1 = new int[] {1,2,3,0,0,0};
        int m = 3;
        int[] nums = new int[] {2,2,1,1,1,2,2,3,3};

        int count = 0;
        int candidate = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                candidate = nums[i];
            }

            if (nums[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }
        


        System.out.println(candidate);

        Arrays.sort(nums);
        System.out.println(nums[nums.length / 2]);
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] nums = new int[m];
        System.arraycopy(nums1, 0, nums, 0, m);


    }
}
