package org.example.problem7;

import java.util.HashMap;
import java.util.Map;

public class LongestRepeatString {
    public static String findLongestString(String sequence)
    {
        var suffixTree = new SuffixTree(sequence);
        Map<String, Integer> occurrences = new HashMap<>();
        for (int i = 2; i < sequence.length(); i++)
        {
            for (int j = 0; j < sequence.length(); j++)
            {
                if (j + i >= sequence.length())
                    break;

                var substr = sequence.substring(j, j + i);
                var count = suffixTree.searchText(substr).size();
                occurrences.put(substr, count);
            }

        }

        int maxLength = Integer.MIN_VALUE;
        String maxSeq = "";
        for (var entry : occurrences.entrySet())
        {
            var key = entry.getKey();
            var value = entry.getValue();
            var length = key.length();

            if (value > 1)
            {
                if (length > maxLength)
                {
                    maxLength = length;
                    maxSeq = key;
                }
            }
        }
        return maxSeq;
    }

    public static void runExample()
    {
        System.out.println(LongestRepeatString.findLongestString("GTGTGTGCTGGACTATTGGAACCCTGGCGCTGGGTCTACATGTCTTCGCTGACCCTTGCGGGCATGGCATATATGCGTTGACAGTTTGGGGTTTCTGCCGGAAGCGTAGTCACCCTGGCAGGGCCCGGAAGTTCCCTCCGGTAACGTTACCAGTAGACCATAGCTGTGCCAGTATAGCAGTCGTCGGTGCCACTTATTGGTGGAAGACCGCTTTCTTAATAGAACCAACAGTACAGTCCGCAGTCTCCGAGTGGCGTTGGTGCGGTGTAGCAATAAAACTAGTTACTTTTGGGCCACTGTTACGGAAAAGCGTTACTGGTATTACGCAAACCATCGGCAATTATGACTCATCACAACAGGAATGGGGCAATCACTAGGGTAAAGTACCGCGATTCAAAACAGAATGAAGTGATTGGGTGTACTATTCGTCAAGACCCTTCGTAAGACGTCCTACACTTATCAACCGCAGAGGAGTAAGAAATCGACGGGCTAATTCTCTGCGGTTCCGGAACGAGCCGTCCCAACAAAATCTCTTCAAGGTAGTGCTCAGGACTTGATGCCCGTGGCTTTCTGCACAGGATTAAGAGGCTATAGCACCTCACTCGCATTCTCGCAATGACGTATCGCATCCGAACCGGTAACGTTACCAGTAGACCATAGCTGTGCCAGTATAGCAGTCGTCGGTGCCACTTATTGGTGCTGAGACTTTTGTTGATTAGAAAAGATATGCCCTCCGGCTGGTACCAAACATCCAGGGGCTGTATACAATTTATAACCGGCCGAGTCTTTCTAAGCTCACTTGCAGAAACTGCGCACTGATTAACCGGTAACGTTACCAGTAGACCATAGCTGTGCCAGTATAGCAGTCGTCGGTGCCACTTATTGGTGCTGAGTCGCGTAGCTTAACAGAAGAAGCCGCGATTGTCCAATAAAGCTCACAGGGCGCTTCCCTTGGTTAACCTGAGTACGGACGCCGGTTAGAGGGCACTGCCGGGGCCTCAGCTAACATTATTGCTATTGACATGATACCTTGCAGATGTAGCCACTTGCACCGTGCACCGTGGGGAAGGACGCAACGTTCCACGAGGCTGTGGTATGAATATAAACAACTTCACGCCTCTGGGAAACTGACTGTACAATTAACTAAGAGTGAGAACGTTCTGAGATACCAACACACCTCAACGACAAGTGTTAC\n"));
    }
}
