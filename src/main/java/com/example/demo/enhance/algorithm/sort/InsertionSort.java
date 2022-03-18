package com.example.demo.enhance.algorithm.sort;

/**
 * Insertion sort is a simple sorting algorithm that works by iteratively inserting each element of an unsorted list into its correct position in a sorted portion of the list. It is like sorting playing cards in your hands. You split the cards into two groups: the sorted cards and the unsorted cards. Then, you pick a card from the unsorted group and put it in the right place in the sorted group.
 * <p>
 * - We start with second element of the array as first element in the array is assumed to be sorted.
 * - Compare second element with the first element and check if the second element is smaller then swap them.
 * - Move to the third element and compare it with the first two elements and put at its correct position
 * - Repeat until the entire array is sorted.
 * <p>
 * Time Complexity of Insertion Sort
 * - Best case: O(n) , If the list is already sorted, where n is the number of elements in the list.
 * - Average case: O(n^2 ) , If the list is randomly ordered
 * - Worst case: O(n^2 ) , If the list is in reverse order
 * Space Complexity of Insertion Sort
 * - Auxiliary Space: O(1), Insertion sort requires O(1) additional space, making it a space-efficient sorting
 * algorithm.
 * Advantages of Insertion Sort:
 * - Simple and easy to implement.
 * - Stable sorting algorithm.
 * - Efficient for small lists and nearly sorted lists.
 * - Space-efficient as it is an in-place algorithm.
 * - Adoptive. the number of inversions is directly proportional to number of swaps.
 * For example, no swapping happens for a sorted array, and it takes O(n) time only.
 */
public class InsertionSort {
    // Driver method
    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6};

        InsertionSort ob = new InsertionSort();
        ob.sort(arr);

        printArray(arr);
    }

    /* Function to sort array using insertion sort */
    void sort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            /* Move elements of arr[0...i-1], that are greater than key,
            to one position ahead of their current position */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    /* A utility function to print array of size n */
    static void printArray(int[] arr) {
        int n = arr.length;
        for (int j : arr) {
            System.out.print(j + " ");
        }

        System.out.println();
    }
}
