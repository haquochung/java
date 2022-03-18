package com.example.demo.enhance.interview;


import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A technology company announced that a new supply of P monitors would soon be available at their store.
 * There were N orders (numbered from 0 to N-1) placed by customers who wanted to buy those monitors.
 * The K-th order has to be delivered to a location at distance D[K] from the store and is for exactly C[K] monitors.
 * Now the time has come for the monitors to be delivered. The orders will be fulfilled one by one.
 * To minimize the shipping time, it has been decided that the delivers will be made in order of increasing distance,
 * they can be processed in any order. Monitors to more distant customers will be delivered only once all orders to
 * customers closer to the store have already fulfilled.
 * What is the maximum total number of orders that can be fulfilled?
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * - N is an integer within the range [1...100,000].
 * - each element of array D and C is an integer within the range [1...1,000,000,000].
 * - P is an integer within the range [0...1,000,000,000].
 */
public class Task4 {
    public static void main(String[] args) {
        System.out.println(solution(new int[] {5,11,1,3}, new int[] {6,1,3,2}, 7)); // expect: 2
        System.out.println(solution(new int[] {10,15,1}, new int[] {10,1,2}, 3)); // expect: 1
        System.out.println(solution(new int[] {11,18,1}, new int[] {9,18,8}, 7)); // expect: 0
        System.out.println(solution(new int[] {1,4,2,5}, new int[] {4,9,2,3}, 19)); // expect: 4
    }

    public static int solution(int[] D, int[] C, int P) {
        int N = D.length;
        Map<Integer, Integer> orders = new TreeMap<>
                (IntStream.range(0, N).boxed().collect(Collectors.toMap(i -> D[i], i-> C[i])));

        int totalOrders = 0;
        int fulfilled = P;
        for (int value : orders.values()) {
            fulfilled -= value;
            if (fulfilled < 0) {
                break;
            }
            totalOrders++;
        }

        return totalOrders;
    }
}
