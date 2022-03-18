package com.example.demo.enhance.interview;

/**
 * There are N blocks, numbered from 0 to N-1, arranged in a row. A couple of frogs were sitting together on one block
 * when they had a terrible quarrel. Now they want to jump away from one another so that the distance between them will
 * be as large as possible. The distance between blocks numbered J and K, where J <= K, is computed as K - J + 1. The
 * frogs can only jump up, meaning that they can move from one block to another only if the two blocks are adjacent and
 * the second block is of the same or greater height as the first. What is the longest distance that they can possibly
 * create between each other, if they also chose to sit on the optimal starting block initially?
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * <p>- N is an integer within the range [2...200,000].</p>
 * <p>- each element of array blocks is an integer within the range [1...1,000,000,000].</p>
 */
public class Task6 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{2, 6, 8, 5})); //expect: 3
        System.out.println(solution(new int[]{1, 5, 5, 2, 6})); //expect: 4
        System.out.println(solution(new int[]{1, 1})); //expect: 2
    }

    public static int solution(int[] blocks) {
        int distance = 0;

        int startBlock = 0;

        int totalBlocks = blocks.length;

        int firstFrogBlockIndex = startBlock;
        int secondFrogBlockIndex = startBlock;
        int before;
        int after;
        for (; startBlock < totalBlocks; startBlock++) {
            firstFrogBlockIndex = startBlock;
            secondFrogBlockIndex = startBlock;
            for (before = startBlock - 1; before > 0; before--) {
                if (blocks[before] >= blocks[firstFrogBlockIndex]) {
                    firstFrogBlockIndex = before;
                } else {
                    break;
                }
            }

            for (after = startBlock+1; after < totalBlocks; after++) {
                if (blocks[after] >= blocks[secondFrogBlockIndex]) {
                    secondFrogBlockIndex = after;
                } else {
                    break;
                }
            }

            distance = Math.max(distance, secondFrogBlockIndex - firstFrogBlockIndex + 1);
        }

        return distance;
    }
}
