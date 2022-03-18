package com.example.demo.enhance.interview;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * A group of friends is going on holiday together. They have come to a meeting point (the start of the journey) using N
 * cars. There are P[K] people and S[K] seats in the K-th car for K in range [0...N-1]. Some of the seats in the cars
 * may be free, so it is possible for some of the friends to change the car they are in. The friends have decided that,
 * in order to be ecological, they will leave some cars parked at the meeting point and travel with as few as possible
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * <p>- N is an integer within the range [1...100,000].</p>
 * <p>- each element of arrays P and S is an integer within the range [1...9].</p>
 * <p>- every friend had a seat in the car they came inl that is, P[K] <= S[K] for each K within the range
 * [0...N-1]</p>
 */
public class Task2 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 4, 1}, new int[]{1, 5, 1})); // expect: 2
        System.out.println(solution(new int[]{4, 4, 2, 4}, new int[]{5, 5, 2, 5})); // expect: 3
        System.out.println(solution(new int[]{2, 3, 4, 2}, new int[]{2, 5, 7, 2})); // expect: 2
        System.out.println(solution(new int[]{1, 1, 1}, new int[]{1, 1, 3})); // expect: 1
        System.out.println(solution(new int[]{1}, new int[]{2})); // expect: 1
        System.out.println(solution(new int[]{1, 1}, new int[]{1, 3})); // expect: 1
    }

    public static int solution(int[] P, int[] S) {

        int totalPeople = IntStream.of(P).sum();
        Arrays.sort(S);

        int totalCars = 0;
        int carIndex = S.length - 1;
        while (totalPeople > 0) {
            totalPeople -= S[carIndex];
            totalCars++;
            carIndex--;
        }

        return totalCars;
    }
}
