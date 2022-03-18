package com.example.demo.enhance.interview;

/**
 * We are given a string S of length N consisting only of letter 'A' and/or 'B'. Our goal is to obtain a string in the
 * format "A..AB..B" (all letters 'A' occur before all letters 'B') by deleting some letters from S. In particular,
 * strings consisting only of letters 'A' or only of letters 'B' fit this format.
 * <p/>
 * Find the minimum number of letters that need to be deleted from S in order obtain a string in the above format.
 * <p/>
 * Write an efficient algorithm for the following assumptions:
 * <p>- N is an integer within the range [1...100,000].</p>
 * <p>- string S is made only of the characters 'A' and/or 'B'</p>
 */
public class Task7 {
    public static void main(String[] args) {
        System.out.println(solution("BAAABAB")); // expect: 2 - AAABB
        System.out.println(solution("BBABAA")); // expect: 3 - AAA or AB
        System.out.println(solution("AABBBB")); // expect: 0 - AABBBB
        System.out.println(solution("BAAAAAAAAAABABBBBBBBAB"));
    }

    public static int solution(String S) {
        if (S.matches("^A+B+")) {
            return 0;
        }

        int deletedLetters = Math.min(
                S.replaceAll("A", "").length(),
                S.replaceAll("B", "").length()
                                     );

        int deletedBCharacters = S.indexOf("A");
        String sub = S.substring(deletedBCharacters);
        int startIndexB = sub.indexOf("B");
        int deletedACharacters = sub.substring(startIndexB)
                                    .replaceAll("B", "").length()
                ;

        deletedLetters = Math.min(deletedACharacters + deletedBCharacters, deletedLetters);

        return deletedLetters;
    }
}
