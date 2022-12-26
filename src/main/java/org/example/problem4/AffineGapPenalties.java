package org.example.problem4;

import org.example.problem2.Problem2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AffineGapPenalties {

    static String seq1;
    static String seq2;

    final static int inf = 1000000;
    final static int sigma = 11;
    final static int epsilon = 1;

    AffineGapPenalties()
    {

    }

    static class Results
    {
        public int[][][] backtrack;
        public int maxScore;
        public int i;
        public int j;

        public Results(int maxScore, int[][][] backtrack,
                       int i, int j)
        {
            this.maxScore = maxScore;
            this.backtrack = backtrack;
            this.i = i;
            this.j = j;
        }
    }

    private static void printResult(Results res)
    {
        var backtrack = res.backtrack;
        var i = res.i;
        var j = res.j;
        var maxScore = res.maxScore;
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        var level = 1;
        while (i > 0 || j > 0)
        {
            if (level == 0)
            {
                if (1 == backtrack[0][i][j])
                    level = 1;
                i--;
                s1.insert(0, seq1.charAt(i));
                s2.insert(0, '-');
            }
            else if (level == 2)
            {
                if (backtrack[2][i][j] == 1)
                    level = 1;
                j--;
                s1.insert(0, '-');
                s2.insert(0, seq2.charAt(j));
            } else
            {
                if (backtrack[1][i][j] == 1)
                {
                    s1.insert(0, seq1.charAt(--i));
                    s2.insert(0, seq2.charAt(--j));
                }
                else
                    level = backtrack[1][i][j];
            }
        }
        System.out.println(maxScore);
        System.out.println(s1);
        System.out.println(s2);
    }

    private static void readData(String filename)
    {
        File file = new File(filename);
        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            seq1 = br.readLine().trim();
            seq2 = br.readLine().trim();
            inputStream.close();
        } catch (IOException exception)
        {

        }
    }

    private static int[][] createFilledMatrix(int nrow, int ncol, int fillElement)
    {

        int[][] matrix = new int[nrow][ncol];
        for (int i = 0; i < nrow; i++)
        {
            for (int j = 0; j < ncol; j++)
            {
                matrix[i][j] = fillElement;
            }
        }
        return matrix;
    }

    private static Results longestCommonSubstring()
    {
        var n = seq1.length();
        var m = seq2.length();
        var lower = createFilledMatrix(n+1, m+1, -inf);
        var middle = createFilledMatrix(n+1, m+1, -inf);
        var upper = createFilledMatrix(n+1, m+1, -inf);
        var matrices = new int[][][]{ lower, middle, upper};

        var backtrackLower = createFilledMatrix(n+1, m+1, 0);
        var backtrackMiddle = createFilledMatrix(n+1, m+1, 0);
        var backtrackUpper = createFilledMatrix(n+1, m+1, 0);
        var backtrack = new int[][][]{ backtrackLower, backtrackMiddle, backtrackUpper};

        matrices[0][0][0] = 0;
        matrices[1][0][0] = 0;
        matrices[2][0][0] = 0;
        matrices[0][1][0] = -sigma;
        matrices[1][1][0] = -sigma;
        for (int i = 2; i < n+1; i++)
        {
            matrices[0][i][0] = matrices[0][i-1][0] - epsilon;
            matrices[1][i][0] = matrices[0][i][0];
        }
        matrices[2][0][1] = -sigma;
        matrices[1][0][1] = -sigma;
        for (int i = 2; i < m+1; i++)
        {
            matrices[2][0][i] = matrices[2][0][i-1] - epsilon;
            matrices[1][0][i] = matrices[2][0][i];
        }
        for (int i = 1; i < n+1; i++)
        {
            for (int j = 1; j < m+1; j++)
            {
                var score1 = matrices[0][i-1][j] - epsilon;
                var score2 = matrices[1][i-1][j] - sigma;
                var lowerScore = Math.max(score1, score2);
                matrices[0][i][j] = lowerScore;
                if (lowerScore == score2)
                    backtrack[0][i][j] = 1;
                score1 = matrices[2][i][j-1] - epsilon;
                score2 = matrices[1][i][j-1] - sigma;
                var upperScore = Math.max(score1, score2);
                matrices[2][i][j] = upperScore;
                if (upperScore == score2)
                    backtrack[2][i][j] = 1;
                var c1 = seq1.charAt(i-1);
                var c2 = seq2.charAt(j-1);
                var score3 = matrices[1][i-1][j-1] + Blosum62.getDistance(c1, c2);
                var middleScore = Math.max(Math.max(lowerScore, upperScore), score3);
                matrices[1][i][j] = middleScore;
                if (middleScore == score3)
                    backtrack[1][i][j] = 1;
                else if  (middleScore == upperScore)
                    backtrack[1][i][j] = 2;
            }
        }
        return new Results(matrices[1][n][m], backtrack, n, m);
    }

    public static void solve(String filename)
    {
        readData(filename);
        var res = longestCommonSubstring();
        printResult(res);
    }
}
