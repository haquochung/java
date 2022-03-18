package com.example.demo.enhance.interview;

/**
 * There are N players standing in a row, one player on field. They are numbered from 0 to N-1 from left to right.
 * Players perform moves one by one from left to right, that is, in ascending order of numbers.
 * Each player presses an arrow key in one of the four cardinal directions: left('<'), right('>'), up('^'), down('v').
 * A key press in the given direction means that player attempts to move onto the closest field in the direction specified.
 * A move can be performed only if there is no other player already standing on the target field.
 * Moves are represented as a string S of length N, where S[K] (for K within the range 0...N-1) is the direction of the K-th player's move.
 * How many players will actually perform a move successfully?
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * - N is an integer within the range [1...100,000].
 * - each element of array D and C is an integer within the range [1...1,000,000,000].
 * - P is an integer within the range [0...1,000,000,000].
 */
public class Task5 {
    public static void main(String[] args) {
        System.out.println(solution("><^v")); // expect: 2
        System.out.println(solution("<<^<v>>")); // expect: 6
    }

    public static int solution(String S) {

        int totalPlayers = S.length();
        boolean moveSuccess = false;
        int movedPlayers = 0;
        for (int index = 0; index < totalPlayers; index++) {
            char move = S.charAt(index);
            switch (move) {
                case '<':
                    if (index == 0) {
                        movedPlayers++;
                        moveSuccess = true;
                    } else {
                        if (moveSuccess) {
                            movedPlayers++;
                        }
                    }
                    break;
                case '>':
                    if (index == totalPlayers - 1) {
                        movedPlayers++;
                    } else {
                        moveSuccess = false;
                    }
                    break;
                case '^':
                case 'v':
                    movedPlayers++;
                    moveSuccess = true;
                    break;
                default:
                    break;
            }

        }

        return movedPlayers;
    }
}
